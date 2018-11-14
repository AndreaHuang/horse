package org.andrea.jockey.crawler;

import org.andrea.jockey.app.JockeyWebsiteConfig;
import org.andrea.jockey.app.SeleniumConfig;
import org.andrea.jockey.jdbc.RecordCardDAO;
import org.andrea.jockey.model.RaceCardItem;
import org.andrea.jockey.model.RaceCardResult;
import org.andrea.jockey.model.RaceInfo;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;

@Component
public class DataCrawler {

    private static final SimpleDateFormat SDF_Original = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat SDF_Original_NewRace = new SimpleDateFormat("MMMM dd, yyyy");
    private static final SimpleDateFormat SDF_Update = new SimpleDateFormat("yyyyMMdd");

    @Autowired
    JockeyWebsiteConfig jockeyWebsiteConfig;

    @Autowired
    SeleniumConfig seleniumConfig;
    @Autowired
    RecordCardDAO dao;

    public void getNewRaceCard(){


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
       // results.add(url + "/1");
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
    public void getRecord() {

        List<String> links = listLinksOfRaceDaysInDateRange(jockeyWebsiteConfig.getUrl(),
                jockeyWebsiteConfig.getStartDate(), jockeyWebsiteConfig.getEndDate());

        for (String urlOfARaceDay : links) {
            getRecordsByUrlOfDay(urlOfARaceDay);
        }

        return;
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
            }catch(Exception e){
                e.printStackTrace();
                dao.insertUrls(urlOfARace.substring(urlOfARace.indexOf("Local")));
            }
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
        List<WebElement> date_options = getDriver().findElement(By.id("raceDateSelect")).findElements(By.tagName("option"));

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
            }
        }

        return result;

    }


    private List<String> listLinksOfOneRaceDay(String url) {
        List<String> results = new ArrayList<String>();
        results.add(url + "/1");
        getDriver().get(url);
        List<WebElement> allLinksElements = getDriver().findElements(By.xpath("//*[@id=\"results\"]/div[2]/table/tbody/tr/td/a"));

        for (WebElement aLinkElement : allLinksElements) {
            String link = aLinkElement.getAttribute("href");
            if (!link.contains("ResultsAll") && !link.contains("Simulcast")) {
                results.add(link);
            }
        }
        return results;
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

        WebElement resultEle = waitForPresence(By.id("results"));
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
            resultEle = getDriver().findElement(By.id("results"));

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


        Elements infoList = doc.select("div");

        String info = infoList.get(3).select("table > tbody > tr > td").first().text();
       // String info = resultEle.findElement(By.xpath("//div[3]/table/tbody/tr/td[1]")).getText();


        try {
            String raceDate = SDF_Update.format(SDF_Original.parse(info.split(":")[1].trim().split(" ")[0]));
            raceInfo.setRateDate(raceDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String place = info.split(":")[1].trim().split(" ")[1];
        if (place.toUpperCase().startsWith("SHA")) {
            raceInfo.setRacePlace("ST");
        } else {
            raceInfo.setRacePlace("HV");
        }


        /* Race ID */
        info = doc.select("div").get(6).getElementsByTag("div").get(0).text();
        System.out.println(info);
        raceInfo.setRaceSeqOfDay(Integer.parseInt(info.substring(0, info.indexOf("(")).trim().split(" ")[1]));
        raceInfo.setRaceId(info.substring(info.indexOf("(") + 1, info.indexOf(")")).trim());
        /*Class and distance*/
        Elements eleList = doc.select("div").get(6).select("div > table > tbody > tr");
        info = eleList.get(0).select("td").get(0).text();
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
        raceInfo.setGoing(eleList.get(0).select("td").get(2).text());
        /* Course */
        raceInfo.setCourse(eleList.get(1).select("td").get(2).text());


        List<WebElement> trList = getDriver().findElements(By.xpath("//*[@id=\"results\"]/div[6]/table/tbody/tr"));

        int rows = trList.size();


        List<WebElement> tdList = getDriver().findElements(By.xpath("//*[@id=\"results\"]/div[6]/table/tbody/tr/td"));

        int cells = tdList.size();

        int columns = cells / rows;

        System.out.println(String.format("Total %d rows %d cells", rows,cells));
        RaceCardResult item = null;
        for (int i = 0; i < rows; i++) {


            item = new RaceCardResult(raceInfo);
            /* Place*/
            String placeOfTheHorse = tdList.get(i * 12).getText().trim();
            try {
                item.setPlace(Integer.parseInt(placeOfTheHorse));
            } catch (Exception e) {
               continue;
            }


            /* Horse No.*/
            item.setHorseNo(tdList.get(i * 12 + 1).getText().trim());
            /* Horse Name.*/
            String horseName = tdList.get(i * 12 + 2).getText().trim();
            item.setHorseName(horseName);
            item.setHorseId(this.parseHorseId(horseName));
            /* Jockey.*/
            item.setJockey(tdList.get(i * 12 + 3).getText().trim());
            /* Trainer.*/
            item.setTrainer(tdList.get(i * 12 + 4).getText().trim());
            /* Actual Weight.*/
            item.setAddedWeight(Integer.parseInt(tdList.get(i * 12 + 5).getText().trim()));
            /* Declare Horse Weight */
            item.setDeclaredHorseWeight(Integer.parseInt(tdList.get(i * 12 + 6).getText().trim()));
            /* Draw */
            try {
                item.setDraw(Integer.parseInt(tdList.get(i * 12 + 7).getText().trim()));
            }catch(Exception e){
              /*  skip this row */
               continue;
            }
            /* LBW */
            String lbw_String = tdList.get(i * 12 + 8).getText().trim();
            item.setLbwString(lbw_String);
            item.setLbw(parseLBW(lbw_String));


            /* Running Position*/
            item.setRunningPosition(tdList.get(i * 12 + 9).getText().trim());
            /* Finish Time*/
            String finishTime_String = tdList.get(i * 12 + 10).getText().trim();
            item.setFinishTimeString(finishTime_String);
            item.setFinishTime(this.parseFinishTime(finishTime_String));
            /* Win Odds*/
            item.setWinOdds(Double.parseDouble(tdList.get(i * 12 + 11).getText().trim()));

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
        /*race seq*/
        raceInfo.setRaceSeqOfDay(Integer.parseInt(infoArray[0].split(" ")[1]));
        /*race date*/
        String raceDateOrigin = infoArray[1].split(",")[1]+","+infoArray[1].split(",")[2];

        try {
            String raceDate = SDF_Update.format(SDF_Original_NewRace.parse(raceDateOrigin.trim()));
            raceInfo.setRateDate(raceDate);
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
        String course = infoArray[2].split(",")[0]+" -"+infoArray[2].split(",")[1];
        raceInfo.setCourse(course.toUpperCase());
        /*Distance*/
        String distance = infoArray[2].split(",")[2];
        distance = distance.toUpperCase().replace("M","");
        raceInfo.setDistance(Integer.parseInt(distance.trim()));
        /*Going*/
        if(infoArray[2].split(",").length >3){
            raceInfo.setGoing(infoArray[2].split(",")[3].toUpperCase().trim());
        }
        /*Class*/
        String classString = infoArray[3].trim().substring(infoArray[3].trim().lastIndexOf(" "));

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
            item.setAddedWeight(Integer.parseInt(tdList.get(i * columns + 5).getText().trim()));
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
            item.setRatingDelta(Integer.parseInt(tdList.get(i * columns + 11).getText().trim()));

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
        String[] splitted = finishTime_String.split("\\.", -1);

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
