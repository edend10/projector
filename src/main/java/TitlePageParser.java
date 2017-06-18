import extractor.Extractor;
import extractor.ReleaseDateExtractor;
import extractor.TitleNameExtractor;
import model.Title;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class TitlePageParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(TitlePageParser.class);

    private List<Extractor> extractors;

    public TitlePageParser() {
        this.extractors = Arrays.asList(
                new TitleNameExtractor(),
                new ReleaseDateExtractor()
        );
    }

    public Title parseFeaturesFromTitlePageSource(String pageSource) {
        Title title = new Title();

        extractors.forEach(extractor ->
                extractor.extract(pageSource, title)
        );

        System.out.println(title.getReleaseTimestamp());

        return title;
    }
}
