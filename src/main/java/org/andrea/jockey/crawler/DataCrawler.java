package org.andrea.jockey.crawler;

import org.andrea.jockey.app.JockeyWebsiteConfig;
import org.andrea.jockey.app.SeleniumConfig;
import org.andrea.jockey.jdbc.RecordCardDAO;
import org.andrea.jockey.model.Dividend;
import org.andrea.jockey.model.RaceCardItem;
import org.andrea.jockey.model.RaceCardResult;
import org.andrea.jockey.model.RaceInfo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.jsoup.nodes.Document;


@Component
public class DataCrawler {

    private static final SimpleDateFormat SDF_Original = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat SDF_Original_Dividend = new SimpleDateFormat("yyyy/MM/dd");
    private static final SimpleDateFormat SDF_Original_NewRace = new SimpleDateFormat("MMMM dd, yyyy");
    private static final SimpleDateFormat SDF_Update = new SimpleDateFormat("yyyyMMdd");


    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,###.##");
    private static final By ByXPath_resultEle = By.xpath("//div[contains(@class, 'localResults')]");
    private static final By ByXPath_dividendEle = By.xpath("//div[contains(@class, 'dividend_tab')]");
    static  {
        DECIMAL_FORMAT.setParseBigDecimal(true);
    }


    @Autowired
    JockeyWebsiteConfig jockeyWebsiteConfig;

    @Autowired
    SeleniumConfig seleniumConfig;
    @Autowired
    RecordCardDAO dao;

    public void getNewRaceCard(){

        dao.deleteNewRace();
        List<String> allNewUrls = listLinksOfNewRaces(jockeyWebsiteConfig.getUrlForNewRace());

        for (String urlOfARace : allNewUrls) {

            System.out.println("Checking " + urlOfARace);
            try {
                List<RaceCardItem> raceCardResultOfARace = checkDetailsOfANewRace(urlOfARace);
                // results.addAll(raceCardResultOfARace);
                dao.batchInsertNewRace(raceCardResultOfARace);
            }catch(Exception e){
                e.printStackTrace();

            }
        }

    }
    private List<String> listLinksOfNewRaces(String url) {
        List<String> results = new ArrayList<String>();
        //System.out.println(url);
        results.add(url);
        getDriver().get(url);
        List<WebElement> allLinksElements = getDriver().findElements(By.xpath("//*[@id=\"racecard\"]/div[3]/table/tbody/tr/td/a"));

        for (WebElement aLinkElement : allLinksElements) {
            String link = aLinkElement.getAttribute("href");
            if (!link.contains("ResultsAll") && !link.contains("Simulcast")) {
                results.add(link);
            }
        }
        return results;
    }
    public String[] getRecord() {

        int maxDateOfExistingRecords = dao.getMaxDate();
        System.out.println("Max Date of Existing Records: "+maxDateOfExistingRecords);

      //
        List<String> links = listLinksOfRaceDaysInDateRange(jockeyWebsiteConfig.getUrl(),
                Integer.toString(maxDateOfExistingRecords+1)  , jockeyWebsiteConfig.getEndDate());

        for (String urlOfARaceDay : links) {
            getRecordsByUrlOfDay(urlOfARaceDay);
        }
        List<String> raceDate=new ArrayList<>();
        for (String urlOfARaceDay : links) {
            String[] array =  urlOfARaceDay.split("/");
            String date =array [array.length-2];
            raceDate.add(date);
        }
        Collections.sort(raceDate);
        if(raceDate.size()>0){
            //Get dividends
            String startDate = raceDate.get(0);
            String endDate = raceDate.get(raceDate.size()-1);
            this.getDividends(Integer.parseInt(startDate),Integer.parseInt(endDate));

            return new String[]{startDate,endDate};
        } else {
            return new String[]{};
        }

    }
    public void getRecordsByUrlOfDay(String urlOfARaceDay){
        List<String> linksOfEachRace =new ArrayList<>();
        List<String> urlsOfRacesOnOneRaceDay = listLinksOfOneRaceDay(urlOfARaceDay);
        linksOfEachRace.addAll(urlsOfRacesOnOneRaceDay);
        System.out.println(String.format("Date %s has %d races", urlOfARaceDay, urlsOfRacesOnOneRaceDay.size()));
        for (String urlOfARace : urlsOfRacesOnOneRaceDay) {

            System.out.println("Checking " + urlOfARace);
            try {
                List<RaceCardResult> raceCardResultOfARace = checkDetailsOfARace(urlOfARace);

                // results.addAll(raceCardResultOfARace);
                dao.batchInsertResults(raceCardResultOfARace);
                dao.batchInsertDividend(checkDividendsOfARace(urlOfARace));
            }catch(Exception e){
                e.printStackTrace();

            }
        }
    }


    public void getDividends() {

        int maxDateOfExistingRecords = dao.getMaxDate();
        System.out.println("Max Date of Existing Records: "+maxDateOfExistingRecords);
        //
        List<String> links = listLinksOfRaceDaysInDateRange(jockeyWebsiteConfig.getUrl(),
                Integer.toString(20170101)  , jockeyWebsiteConfig.getEndDate());


        for (String urlOfARaceDay : links) {
            getDividendsByUrlOfDay(urlOfARaceDay);
        }

        return;
    }
    public void getDividends(int raceDateStart,int raceDateEnd) {

        List<String> links = listLinksOfRaceDaysInDateRange(jockeyWebsiteConfig.getUrl(),
                Integer.toString(raceDateStart)  ,  Integer.toString(raceDateEnd));


        for (String urlOfARaceDay : links) {
            getDividendsByUrlOfDay(urlOfARaceDay);
        }

        return;
    }
    public void getDividends(int raceDate) {

        List<String> links = listLinksOfRaceDaysInDateRange(jockeyWebsiteConfig.getUrl(),
                Integer.toString(raceDate)  ,  Integer.toString(raceDate));


        for (String urlOfARaceDay : links) {
            getDividendsByUrlOfDay(urlOfARaceDay);
        }

        return;
    }

    public void getDividendsByUrlOfDay(String urlOfARaceDay){
        List<Dividend> dividendsList= new ArrayList<>();
        List<String> linksOfEachRace =new ArrayList<>();
        List<String> urlsOfRacesOnOneRaceDay = listLinksOfOneRaceDay(urlOfARaceDay);
        linksOfEachRace.addAll(urlsOfRacesOnOneRaceDay);
        System.out.println(String.format("Date %s has %d races", urlOfARaceDay, urlsOfRacesOnOneRaceDay.size()));
        for (String urlOfARace : urlsOfRacesOnOneRaceDay) {

            System.out.println("Checking " + urlOfARace);
            try {
                dividendsList.addAll(checkDividendsOfARace(urlOfARace));

            }catch(Exception e){
                e.printStackTrace();

            }
        }
        dao.batchInsertDividend(dividendsList);
    }
    public void getDividendsByUrlOfRace(String urlOfARace){
        try {
            List<Dividend> dividendsList = checkDividendsOfARace(urlOfARace);
            dao.batchInsertDividend(dividendsList);
        }catch(Exception e) {
            e.printStackTrace();

        }

    }
    public void getRecordOfARace(String urlOfARace) {


        List<RaceCardResult> raceCardResultOfARace = checkDetailsOfARace(urlOfARace);
        if(raceCardResultOfARace.size()>0) {
            dao.batchInsertResults(raceCardResultOfARace);
        }
        getDriver().close();
        getDriver().quit();
        return;
    }

    private List<String> listLinksOfRaceDaysInDateRange(String url, String startDate, String endDate) {
        List<String> result = new ArrayList<String>();
        getDriver().get(url);
        List<WebElement> date_options = getDriver().findElement(By.id("selectId")).findElements(By.tagName("option"));

        for (WebElement aDate : date_options) {
            //getDriver().navigate().refresh();

            String path = aDate.getAttribute("value");
            int length = "Local".length();
            if (path.startsWith("Local")) {
                String date = path.split("/")[1];
                if (date.compareTo(startDate) >= 0 && date.compareTo(endDate) <= 0) {
                    String urlOfARaceDay = url + path.substring(length);

                    result.add(urlOfARaceDay);
                }
            } else {
                //path is like 10/04/2019
                //https://racing.hkjc.com/racing/information/English/Racing/LocalResults.aspx?RaceDate=2019/04/10
                //url: http://racing.hkjc.com/racing/Info/meeting/Results/English/Local/
                try {
                    String currentUrl=getDriver().getCurrentUrl();
                    //https://racing.hkjc.com/racing/information/English/racing/LocalResults.aspx
                    String date = SDF_Update.format(SDF_Original.parse(path));
                    if (date.compareTo(startDate) >= 0 && date.compareTo(endDate) <= 0) {
                        String urlOfARaceDay = currentUrl+ "?RaceDate=" + path;
                        result.add(urlOfARaceDay);
                    }
                }catch(java.text.ParseException e){
                    System.err.println("Skipped " +path);
                    e.printStackTrace();
                }

            }
        }

        return result;

    }


    private List<String> listLinksOfOneRaceDay(String url) {
        List<String> results = new ArrayList<String>();
        getDriver().get(url);
        List<WebElement> allLinksElements = getDriver().findElements(By.xpath("//div[contains(@class, 'localResults')]/div[2]/table/tbody/tr/td/a"));

        for (WebElement aLinkElement : allLinksElements) {
            String link = aLinkElement.getAttribute("href");
            if (!link.contains("ResultsAll") && !link.contains("Simulcast")) {
                if(link.endsWith("2")){
                    String firstRace =link.substring(0,link.length()-1)+"1";
                    results.add(firstRace);
                }
                results.add(link);
            }
        }
        return results;

    }

    private List<Dividend> checkDividendsOfARace(String url){
        String[] splitted= url.split("\\?")[1].split("&");
        String raceSeq = splitted[2].split("=")[1];
        String raceDate = splitted[0].split("=")[1];

        try {
            raceDate = SDF_Update.format(SDF_Original_Dividend.parse(raceDate));

        }catch(java.text.ParseException e){
            System.err.println("Skipped " +raceDate);
            e.printStackTrace();
        }
        List<Dividend> results = new ArrayList<>();
        getDriver().get(url);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        waitForPageLoaded();

        WebElement resultEle = waitForPresence(ByXPath_resultEle);
        // WebElement resultEle = getDriver().findElement(By.id("results"));

        String a = resultEle.getAttribute("outerHTML");

        //System.out.println("AndreaTest:"+a.length());
        int count=0;
        while(a.length() < 3000 && count <10){
            count++;
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            resultEle = getDriver().findElement(ByXPath_resultEle);

            a = resultEle.getAttribute("outerHTML");
        }
        Document doc = Jsoup.parse(a);
        Elements infoList = doc.select("div");


        for(Element div: infoList){
           // Elements  headers = div.select("table > tbody tr > td > table > tbody tr > td");
            Elements  headers = div.select("table > thead > tr > td");
           if(headers.size()>0) {
               if("Dividend".equals(headers.first().ownText())){
                   Element dividendTable= headers.first().parent().parent().parent();
                   Elements dividendRows =  dividendTable.select("tbody > tr");
                   //System.out.println(dividendRows.size());
                   int rowCount=0;
                   int rowspan=0;
                   String lastPool=null;
                   for(Element row:dividendRows) {
                       rowCount++;
                      // if(rowCount==1 || rowCount==2) {
                      //     continue;//skip header row
                      // }
                       Elements td_List = row.select("td");
                     //  System.out.println(rowCount +" : "+td_List.size()+" ="+td_List.get(0).ownText());
                       Dividend dividend = new Dividend(raceDate,Integer.parseInt(raceSeq));


                       if(rowspan==0) {

                           dividend.setPool(td_List.get(0).ownText());
                           dividend.setWinning(td_List.get(1).ownText());
                           dividend.setDividend(this.parseDividend(td_List.get(2).ownText()));
                           if(dividend.getDividend() !=null) {
                               results.add(dividend);
                           }
                           //check if there is new rowspan
                           String rowspan_Str = td_List.get(0).attr("rowspan");
                           if (rowspan_Str != null && !rowspan_Str.trim().isEmpty()) {
                               rowspan = Integer.parseInt(rowspan_Str);
                               rowspan--;
                               lastPool = td_List.get(0).ownText();

                           }
                       } else{
                           rowspan--;
                           dividend.setPool(lastPool);
                           dividend.setWinning(td_List.get(0).ownText());
                           dividend.setDividend(this.parseDividend(td_List.get(1).ownText()));
                           if(dividend.getDividend() !=null) {
                               results.add(dividend);
                           }
                       }
                       System.out.println(rowCount +" : "+ dividend.toString());
                   }
               }
           }
        }

        return results;

    }

    private BigDecimal parseDividend(String dividendString){
        // System.out.println(dividendString);
        if(dividendString==null || dividendString.trim().isEmpty()) {
            return null;
        }else if("NOT WIN".equals(dividendString)) {
            return BigDecimal.ZERO;
        }
        else if("REFUND".equals(dividendString)){
                return BigDecimal.ZERO;
        } else if("Detail".equals(dividendString)){
            return null;
        } else {
            try {
                return (BigDecimal)(DECIMAL_FORMAT.parse(dividendString));

            }catch(ParseException e){
                e.printStackTrace();
                throw new RuntimeException( e.getCause());
            }
        }
    }
    private List<RaceCardResult> checkDetailsOfARace(String url) {
        List<RaceCardResult> results = new ArrayList<>();
        System.out.println(url);
        getDriver().get(url);
        //getDriver().navigate().refresh();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        waitForPageLoaded();

        WebElement resultEle = waitForPresence(ByXPath_resultEle);
       // WebElement resultEle = getDriver().findElement(By.id("results"));

        String a = resultEle.getAttribute("outerHTML");

        //System.out.println("AndreaTest:"+a.length());
        int count=0;
        while(a.length() < 3000 && count <10){
            count++;
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            resultEle = getDriver().findElement(ByXPath_resultEle);

           a = resultEle.getAttribute("outerHTML");
        }
        Document doc = Jsoup.parse(a);

//        Document doc = null;
//        try {
//            doc = Jsoup.connect(url).get();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        /*Race Meeting */
        RaceInfo raceInfo = new RaceInfo();


        Elements infoList = doc.select("html > body > div.localResults.commContent > div");

       // String info = infoList.get(3).select("table > tbody > tr > td").first().text();
       // String info = resultEle.findElement(By.xpath("//div[3]/table/tbody/tr/td[1]")).getText();
        String info = infoList.get(2).select("p").first().select("span").first().text();

        try {
            String raceDate = SDF_Update.format(SDF_Original.parse(info.split(":")[1].trim().split(" ")[0]));
            raceInfo.setRaceDate(raceDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String place = info.split(":")[1].trim().split(" ")[1];
        if (place.toUpperCase().startsWith("SHA")) {
            raceInfo.setRacePlace("ST");
        } else if(place.toUpperCase().startsWith("HAPP")){
            raceInfo.setRacePlace("HV");
        } else {
            return results; //Skip Conghua race
        }


        /* Race ID */
        info = infoList.get(3).select("table > thead > tr > td").first().text();
        System.out.println(info);
        raceInfo.setRaceSeqOfDay(Integer.parseInt(info.substring(0, info.indexOf("(")).trim().split(" ")[1]));
        raceInfo.setRaceId(info.substring(info.indexOf("(") + 1, info.indexOf(")")).trim());
        /*Class and distance*/
        //Elements eleList = doc.select("div").get(6).select("div > table > tbody > tr");
        //info = eleList.get(0).select("td").get(0).text();
        Elements eleList = infoList.get(3).select("table > tbody > tr");
        info = eleList.get(1).select("td").first().text();

        String classString = info.split("-")[0].trim().split(" ")[1];
        try {
            int classInt = Integer.parseInt(classString);
            raceInfo.setRaceClass(classInt);
        } catch (Exception e) {
            switch (classString.toUpperCase()) {
                case "ONE":
                    raceInfo.setRaceClass(1);
                    break;
                case "TWO":
                    raceInfo.setRaceClass(2);
                    break;
                case "THREE":
                    raceInfo.setRaceClass(3);
                    break;
                case "FOUR":
                    raceInfo.setRaceClass(4);
                    break;
                case "FIVE":
                    raceInfo.setRaceClass(5);
                    break;
                default:
                    e.printStackTrace();
                    System.out.println(classString);


            }
        }

        raceInfo.setDistance(Integer.parseInt(info.split("-")[1].trim().split("M")[0]));
        /* Going */
        raceInfo.setGoing(eleList.get(1).select("td").get(2).text());
        /* Course */
        raceInfo.setCourse(eleList.get(2).select("td").get(2).text());


        //List<WebElement> trList = getDriver().findElements(By.xpath("//*[@id=\"results\"]/div[6]/table/tbody/tr"));

        Elements trList = infoList.get(4).select("table > tbody > tr");
        int rows = trList.size();

        //List<WebElement> tdList = getDriver().findElements(By.xpath("//*[@id=\"results\"]/div[6]/table/tbody/tr/td"));
        Elements tdList = infoList.get(4).select("table > tbody > tr > td");
        int cells = tdList.size();

        int columns = cells / rows;

        System.out.println(String.format("Total %d rows %d cells", rows,cells));
        RaceCardResult item = null;
        for (int i = 0; i < rows; i++) {


            item = new RaceCardResult(raceInfo);
            /* Place*/
            String placeOfTheHorse = tdList.get(i * 12).text().trim();
            try {
                item.setPlace(Integer.parseInt(placeOfTheHorse));
            } catch (Exception e) {
               continue;
            }


            /* Horse No.*/
            item.setHorseNo(tdList.get(i * 12 + 1).text().trim());
            /* Horse Name.*/
            String horseName = tdList.get(i * 12 + 2).text().trim();
            item.setHorseName(horseName);
            item.setHorseId(this.parseHorseId(horseName));
            /* Jockey.*/
            item.setJockey(tdList.get(i * 12 + 3).text().trim());
            /* Trainer.*/
            item.setTrainer(tdList.get(i * 12 + 4).text().trim());
            /* Actual Weight.*/
            item.setAddedWeight(Integer.parseInt(tdList.get(i * 12 + 5).text().trim()));
            /* Declare Horse Weight */
            item.setDeclaredHorseWeight(Integer.parseInt(tdList.get(i * 12 + 6).text().trim()));
            /* Draw */
            try {
                item.setDraw(Integer.parseInt(tdList.get(i * 12 + 7).text().trim()));
            }catch(Exception e){
              /*  skip this row */
               continue;
            }
            /* LBW */
            String lbw_String = tdList.get(i * 12 + 8).text().trim();
            item.setLbwString(lbw_String);
            item.setLbw(parseLBW(lbw_String));


            /* Running Position*/
            item.setRunningPosition(tdList.get(i * 12 + 9).text().trim());
            /* Finish Time*/
            String finishTime_String = tdList.get(i * 12 + 10).text().trim();
            item.setFinishTimeString(finishTime_String);
            item.setFinishTime(this.parseFinishTime(finishTime_String));
            /* Win Odds*/
            item.setWinOdds(Double.parseDouble(tdList.get(i * 12 + 11).text().trim()));

            System.out.println(i+":"+item);

            results.add(item);

        }


        return results;

    }

    private List<RaceCardItem> checkDetailsOfANewRace(String url) {
        List<RaceCardItem> results = new ArrayList<>();
        System.out.println(url);
        getDriver().get(url);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        waitForPageLoaded();

        WebElement resultEle = waitForPresence(By.id("racecard"));
        // WebElement resultEle = getDriver().findElement(By.id("results"));

        String a = resultEle.getAttribute("outerHTML");

        //System.out.println("AndreaTest:"+a.length());
        int count=0;
        while(a.length() < 3000 && count <10){
            count++;
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            resultEle = getDriver().findElement(By.id("racecard"));

            a = resultEle.getAttribute("outerHTML");
        }



        /*Race Meeting */
        RaceInfo raceInfo = new RaceInfo();

        String info = getDriver().findElement(By.xpath("//*[@id=\"racecard\"]/div[4]/div/table/tbody/tr/td")).getText().trim();

        String[] infoArray = info.split("\n");
        String[] array =null;
        /*race seq*/
        raceInfo.setRaceSeqOfDay(Integer.parseInt(infoArray[0].split(" ")[1]));
        /*race date*/
        String raceDateOrigin = infoArray[1].split(",")[1]+","+infoArray[1].split(",")[2];

        try {
            String raceDate = SDF_Update.format(SDF_Original_NewRace.parse(raceDateOrigin.trim()));
            raceInfo.setRaceDate(raceDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        /*race place*/
        String place = infoArray[1].split(",")[3];
        if (place.toUpperCase().startsWith("SHA")) {
            raceInfo.setRacePlace("ST");
        } else {
            raceInfo.setRacePlace("HV");
        }

        /* Course */
        array = infoArray[2].split(",");
        String course =null;
        if(array.length >2) {
            course = array[0] + " -" + array[1];
        } else{
            course = array[0];
        }
        raceInfo.setCourse(course.toUpperCase());
        /*Distance*/
        String distance =null;
        if(array.length >2){
            distance = array[2];
        } else{
            distance = array[1];
        }
        //distance = array[array.length-1];

        distance = distance.toUpperCase().replace("M","");
        System.out.println(distance);
        raceInfo.setDistance(Integer.parseInt(distance.trim()));
        /*Going*/
        if(array.length >3){
            raceInfo.setGoing(infoArray[2].split(",")[3].toUpperCase().trim());
        }
        /*Class*/
        //System.out.println(infoArray[3].trim());
        array = infoArray[3].split(",");
        String classString = array[array.length-1].trim();
        classString = classString.substring("CLASS".length()).trim();

        if(classString.contains(" ")){//Class 4 (Restricted)
            classString = classString.substring(0, classString.indexOf("("));
        }
        try {
            int classInt = Integer.parseInt(classString.trim());
            raceInfo.setRaceClass(classInt);
        } catch (Exception e) {
            switch (classString.toUpperCase()) {
                case "ONE":
                    raceInfo.setRaceClass(1);
                    break;
                case "TWO":
                    raceInfo.setRaceClass(2);
                    break;
                case "THREE":
                    raceInfo.setRaceClass(3);
                    break;
                case "FOUR":
                    raceInfo.setRaceClass(4);
                    break;
                case "FIVE":
                    raceInfo.setRaceClass(5);
                    break;
                default:
                    e.printStackTrace();
                    System.out.println(classString);


            }
        }



        System.out.println(raceInfo);

         WebElement table = getDriver().findElement(By.xpath("//*[@id=\"racecard\"]/div[8]/table/tbody/tr/td/table/tbody"));


        List<WebElement> trList = table.findElements(By.xpath(".//tr"));
        int rows = trList.size();


        List<WebElement> tdList = table.findElements(By.xpath(".//tr/td"));

        int cells = tdList.size();

        int columns = cells / rows;


        RaceCardItem item = null;
        for (int i = 0; i < rows; i++) {


            item = new RaceCardItem(raceInfo);

            /* Horse No.*/

            item.setHorseNo(tdList.get(i * columns + 0).getText().trim());
            /* Horse Name.*/
            String horseName = tdList.get(i * columns + 3).getText().trim();
            item.setHorseName(horseName);

            item.setHorseId(tdList.get(i * columns + 4).getAttribute("innerHTML").trim());
            /* Actual Weight.*/
            try {
                item.setAddedWeight(Integer.parseInt(tdList.get(i * columns + 5).getText().trim()));
            }catch(Exception e){
                /*  skip this row */
                continue;
            }
            /* Jockey.*/
            item.setJockey(tdList.get(i * columns + 6).getText().trim());
            /* Draw */
            try {
                item.setDraw(Integer.parseInt(tdList.get(i * columns + 8).getText().trim()));
            }catch(Exception e){
                /*  skip this row */
                continue;
            }
            /* Trainer.*/
            item.setTrainer(tdList.get(i * columns + 9).getText().trim());

            /*Rating*/
            item.setRating(Integer.parseInt(tdList.get(i * columns + 10).getText().trim()));
            try {
                item.setRatingDelta(Integer.parseInt(tdList.get(i * columns + 11).getText().trim()));
            }catch(Exception e){
                item.setRatingDelta(0);
            }

            /* Declare Horse Weight */
            item.setDeclaredHorseWeight(Integer.parseInt(tdList.get(i * columns + 12).getText().trim()));

            System.out.println(i+":"+item);

            results.add(item);

        }


        return results;

    }
    private WebDriver getDriver() {
        return seleniumConfig.getDriver();
    }

    private String parseHorseId(String horseName) {

        return horseName.substring(horseName.indexOf("(") + 1).replace(")", "");
    }

    private double parseFinishTime(String finishTime_String) {
        // System.out.println(finishTime_String);
        String[] splitted =finishTime_String.replace(":",".").split("\\.", -1);

        int mininutes = Integer.parseInt(splitted[0]);
        int seconds = Integer.parseInt(splitted[1]);
        int milliseconds = Integer.parseInt(splitted[2]);


        return mininutes * 60 + seconds + ((double) milliseconds / 100);

    }


    private double parseLBW(String lbw_String) {

        double lbw = 0.0;
        if ("-".equals(lbw_String)) {
            lbw = 0.0;
        } else if ("NOSE".equals(lbw_String) || ("+NOSE".equals(lbw_String))) {
            lbw = 0.05;
        } else if ( "N".equals(lbw_String)) {
            lbw = 0.1;
        } else if ("HD".equals(lbw_String) || "SH".equals(lbw_String) || "+SH".equals(lbw_String)) {
            lbw = 0.15;
        } else if ("ML".equals(lbw_String)) {
            lbw = 10;
        }
        else {
            String[] splitted = lbw_String.split("-");
            if (splitted.length == 1) {
                if (splitted[0].contains("/")) {
                    String[] splitted2 = splitted[0].split("/");
                    lbw = Double.parseDouble(splitted2[0]) / Double.parseDouble(splitted2[1]);
                } else {
                    lbw = Double.parseDouble(splitted[0]);
                }
            } else {
                if (splitted[1].contains("/")) {
                    String[] splitted2 = splitted[1].split("/");
                    lbw = Double.parseDouble(splitted2[0]) / Double.parseDouble(splitted2[1]);
                } else {
                    lbw = Double.parseDouble(splitted[1]);
                }
                lbw = lbw + Double.parseDouble(splitted[0]);
            }
        }
        return lbw;
    }

    private WebElement waitForPresence(By by) {


        WebDriverWait wait = new WebDriverWait(getDriver(), 10);

        return  wait.until(ExpectedConditions.visibilityOfElementLocated(by));

    }
    private void waitForPageLoaded(){
        new WebDriverWait(getDriver(), 10).until(
                webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
    }


}
