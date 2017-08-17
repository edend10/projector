package extractor;

import title.RatingSnapshot;
import title.Title;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RatingExtractor {

    private static final Logger LOGGER = LoggerFactory.getLogger(RatingExtractor.class);

    private static final String RATING_START_STR = "<span itemprop=\"ratingValue\">";
    private static final String RATING_END_STR = "</span>";
    private static final String REGEX_STRING = Pattern.quote(RATING_START_STR) + "(.*?)" + Pattern.quote(RATING_END_STR);
    private static final Pattern PATTERN = Pattern.compile(REGEX_STRING);

    public void extractRating(String pageSource, Title title, int timestamp) {
        Matcher matcher = PATTERN.matcher(pageSource);

        try {
            if (matcher.find()) {
                String ratingAsString = matcher.group(1);
                float rating = Float.parseFloat(ratingAsString);
                title.addRatingSnapshot(new RatingSnapshot(timestamp, rating));
            } else {
                LOGGER.error("No rating found for page");
            }
        } catch (Exception e) {
            LOGGER.error("Exception while extracting rating from page", e);
        }
    }
}
