import elastic.ElasticSearchService;
import pagesource.PageSourceService;
import pagesource.TitlePageParser;
import pagesource.YearPageParser;
import title.Title;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Scraper {

    private static final Logger LOGGER = LoggerFactory.getLogger(Scraper.class);

    private static final int MAX_PAGES_TO_TRY = 1;

    private PageSourceService pageSourceService;
    private YearPageParser yearPageParser;
    private TitlePageParser titlePageParser;
    private RatingService ratingService;
    private ElasticSearchService elasticSearchService;

    //private DbService dbService;

    public Scraper(PageSourceService pageSourceService, YearPageParser yearPageParser,
                   TitlePageParser titlePageParser, RatingService ratingService,
                   ElasticSearchService elasticSearchService) {
        this.pageSourceService = pageSourceService;
        this.yearPageParser = yearPageParser;
        this.titlePageParser = titlePageParser;
        this.ratingService = ratingService;
        this.elasticSearchService = elasticSearchService;
    }

    public void scrape(List<Integer> yearsToParse) {
        Set<Integer> imdbIds = new HashSet<>();

        scrapeYearPagesForTitles(yearsToParse, imdbIds);

        Map<Integer, Title> titleMap = scrapeTitlePages(imdbIds);

        scrapeForRatingSnapshots(titleMap);

        //dbService.saveTitleMap(titleMap);
    }

    private void scrapeForRatingSnapshots(Map<Integer, Title> titleMap) {
        titleMap.forEach((imdbId, title) -> {
            try {
                ratingService.addRatingSnapshots(title);
                elasticSearchService.updateTitleWithRatingSnapshots(title.getImdbId(), title.getRatingSnapshots());
            } catch (Exception e) {
                LOGGER.error("Problem adding rating snapshots for title {}", imdbId, e);
            }
        });
    }

    private Map<Integer, Title> scrapeTitlePages(Set<Integer> imdbIds) {
        Map<Integer, Title> titleMap = new HashMap<>();

        //TODO: remove
        int counter = 0;

        for (Integer imdbId : imdbIds) {
            if (counter++ > 100) {
                break;
            }
            try {
                String pageSource = pageSourceService.getTitlePageSource(imdbId);
                if (!pageSource.isEmpty()) {
                    Title title = titlePageParser.parseFeaturesFromTitlePageSource(pageSource, imdbId);
                    elasticSearchService.insertTitle(title, pageSource);
                    titleMap.put(imdbId, title);
                    //TODO: dump title page to sql and purge title map? maybe no need to return it then
                } else {
                    LOGGER.error("page source for title {} is empty. skipping", imdbId);
                }
            } catch (Exception e) {
                LOGGER.error("Problem parsing title page for title: {}", imdbId, e);
            }
        }

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
