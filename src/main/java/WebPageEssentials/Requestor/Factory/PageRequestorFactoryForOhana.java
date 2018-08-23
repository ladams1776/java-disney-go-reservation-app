package WebPageEssentials.Requestor.Factory;

import WebPageEssentials.Requestor.PageRequestor;
import com.gargoylesoftware.htmlunit.WebClient;

public class PageRequestorFactoryForOhana {

    public PageRequestor createPageRequestor() {
        String url = "https://disneyworld.disney.go.com/dining/polynesian-resort/ohana/";

        WebClient webClient = new WebClient();

        PageRequestor pageRequestor = new PageRequestor(url, webClient);

        return pageRequestor;
    }
}
