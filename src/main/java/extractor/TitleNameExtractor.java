package extractor;

import extractor.Extractor;
import model.Title;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TitleNameExtractor implements Extractor {
    private static final Logger LOGGER = LoggerFactory.getLogger(RatingExtractor.class);

    private static final String RATING_START_STR = "<h1 itemprop=\"name\" class=\"\">";
    private static final String RATING_END_STR = "&nbsp;<span id=\"titleYear\">";
    private static final String REGEX_STRING = Pattern.quote(RATING_START_STR) + "(.*?)" + Pattern.quote(RATING_END_STR);
    private static final Pattern PATTERN = Pattern.compile(REGEX_STRING);

    @Override
    public void extract(String pageSource, Title title) {
        Matcher matcher = PATTERN.matcher(pageSource);

        if (matcher.find()) {
            String name = matcher.group(1);
            title.setName(name);
        } else {
            LOGGER.error("No rating found for page");
        }
    }
}
