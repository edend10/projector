package pagesource;

import extractor.*;
import title.Title;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wayback.WaybackApiClient;
import wayback.WaybackApiResponse;
import wayback.WaybackApiService;

import java.util.Arrays;
import java.util.List;

public class TitlePageParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(TitlePageParser.class);

    private List<Extractor> extractors;

    public TitlePageParser() {
        this.extractors = Arrays.asList(
                new TitleNameExtractor(),
                new ReleaseDateExtractor(),
                new GenreExtractor(),
                new BudgetParser(),
                new RatersExtractor()
        );
    }

    public Title parseFeaturesFromTitlePageSource(String pageSource, Integer imdbId) {
        Title title = new Title();

        title.setImdbId(imdbId);
        extractors.forEach(extractor ->
                extractor.extract(pageSource, title));

        return title;
    }
}
