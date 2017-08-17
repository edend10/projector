import extractor.RatingExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pagesource.PageSourceClient;
import pagesource.PageSourceService;
import pagesource.TitlePageParser;
import pagesource.YearPageParser;
import wayback.WaybackApiClient;
import wayback.WaybackApiService;

import java.util.Arrays;
import java.util.List;

public class ScraperRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScraperRunner.class);

    public static void main(String[] args) {

        List<Integer> yearsToParse = Arrays.asList(2016); //TODO: get from args

        PageSourceClient pageSourceClient = new PageSourceClient();
        WaybackApiClient waybackApiClient = new WaybackApiClient();
        WaybackApiService waybackApiService = new WaybackApiService(waybackApiClient);
        RatingExtractor ratingExtractor = new RatingExtractor();

        Scraper scraper = new Scraper(
                new PageSourceService(pageSourceClient),
                new YearPageParser(),
                new TitlePageParser(),
                new RatingService(waybackApiService,
                        pageSourceClient,
                        ratingExtractor));

        scraper.scrape(yearsToParse);

    }
}
