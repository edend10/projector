package extractor;

import title.Title;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReleaseDateExtractor implements Extractor {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReleaseDateExtractor.class);

    private static final String RATING_START_STR = "itemprop=\"datePublished\" content=\"";
    private static final String RATING_END_STR = "\" />";
    private static final String REGEX_STRING = Pattern.quote(RATING_START_STR) + "(.*?)" + Pattern.quote(RATING_END_STR);
    private static final Pattern PATTERN = Pattern.compile(REGEX_STRING);

    @Override
    public void extract(String pageSource, Title title) {
        Matcher matcher = PATTERN.matcher(pageSource);

        if (matcher.find()) {
            String releaseDate = matcher.group(1);
            title.setReleaseTimestamp(Integer.parseInt(releaseDate.replace("-", "")));
        } else {
            LOGGER.error("No release date found for page");
        }
    }
}
