import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class ScraperRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScraperRunner.class);

    public static void main(String[] args) {

        List<Integer> yearsToParse = Arrays.asList(2016); //TODO: get from args

        Scraper scraper = new Scraper(
                new PageSourceService(new PageSourceClient()),
                new YearPageParser(),
                new TitlePageParser());

        scraper.scrape(yearsToParse);

    }
}
