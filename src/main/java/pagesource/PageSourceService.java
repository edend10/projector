package pagesource;

public class PageSourceService {
    private static final String YEAR_PAGE_URL_FORMAT = "http://www.imdb.com/search/title?year=%d,%d&page=%d&title_type=feature";
    private static final String TITLE_PAGE_URL_FORMAT = "http://www.imdb.com/title/tt%d";
    private static final String WAYBACK_URL_FORMAT = "";

    private PageSourceClient client;

    public PageSourceService(PageSourceClient client) {
        this.client = client;
    }

    public String getYearPageSource(int year, int pageNumber) {
        return client.getWebPageSource(String.format(YEAR_PAGE_URL_FORMAT, year, year, pageNumber));
    }

    public String getTitlePageSource(int imdbId) {
        return client.getWebPageSource(String.format(TITLE_PAGE_URL_FORMAT, imdbId));
    }

    public String getTitlePageSourceFromWayback(int imdbId, int timestamp) {
        String titleUrl = String.format(TITLE_PAGE_URL_FORMAT, imdbId);
        return client.getWebPageSource(String.format(WAYBACK_URL_FORMAT, timestamp, titleUrl));
    }
}
