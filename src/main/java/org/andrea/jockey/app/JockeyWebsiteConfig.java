package org.andrea.jockey.app;

import lombok.Data;

@Data
public class JockeyWebsiteConfig {

    private final String NEWRACE_URL="https://racing.hkjc.com/racing/Info/meeting/RaceCard/English/";

    private final String URL="http://racing.hkjc.com/racing/Info/meeting/Results/English/Local/";
    private final String HOMEURL="http://racing.hkjc.com";
    private final String DATE_AFTER="20181112";
    private final String DATE_BEFORE="20181107";
    //，20181018
    public String getUrlForNewRace(){
        return this.NEWRACE_URL;
    }
    public String getUrl(){
        return this.URL;
    }
    public String getStartDate(){
        return this.DATE_AFTER;
    }

    public String getEndDate(){
        return this.DATE_BEFORE;
    }
}
