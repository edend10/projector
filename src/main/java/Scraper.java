import model.Title;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Scraper {

    private static final Logger LOGGER = LoggerFactory.getLogger(Scraper.class);

    private static final int MAX_PAGES_TO_TRY = 1;

    private PageSourceService pageSourceService;
    private YearPageParser yearPageParser;
    private TitlePageParser titlePageParser;

    public Scraper(PageSourceService pageSourceService, YearPageParser yearPageParser, TitlePageParser titlePageParser) {
        this.pageSourceService = pageSourceService;
        this.yearPageParser = yearPageParser;
        this.titlePageParser = titlePageParser;
    }

    public void scrape(List<Integer> yearsToParse) {
        Set<Integer> imdbIds = new HashSet<>();

        scrapeYearPagesForTitles(yearsToParse, imdbIds);

        Map<Integer, Title> titleMap = scrapeTitlePages(imdbIds);
    }

    private Map<Integer, Title> scrapeTitlePages(Set<Integer> imdbIds) {
        Map<Integer, Title> titleMap = new HashMap<>();

        imdbIds.stream().forEach( imdbId -> {
            try {
                String pageSource = pageSourceService.getTitlePageSource(imdbId);
                if (!pageSource.isEmpty()) {
                    Title title = titlePageParser.parseFeaturesFromTitlePageSource(pageSource);
                    title.setImdbId(imdbId);
                    titleMap.put(imdbId, title);
                } else {
                    LOGGER.error("page source for title {} is empty. skipping", imdbId);
                }
            } catch (Exception e) {
                LOGGER.error("Problem parsing title page for {}", imdbId, e);
            }
        });

        return titleMap;
    }

    private void scrapeYearPagesForTitles(List<Integer> yearsToParse, Set<Integer> imdbIds) {
        for (Integer year : yearsToParse) {
            for (int pageNumber = 1; pageNumber <= MAX_PAGES_TO_TRY; pageNumber++) {
                String yearPageSource = pageSourceService.getYearPageSource(year, pageNumber);

                if (yearPageSource.isEmpty()) {
                    break;
                }

                yearPageParser.addTitlesFromPageToSet(yearPageSource, imdbIds);
            }
        }
    }
}
