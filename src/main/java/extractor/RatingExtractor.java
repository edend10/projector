package extractor;

import title.RatingSnapshot;
import title.Title;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RatingExtractor {

    private static final Logger LOGGER = LoggerFactory.getLogger(RatingExtractor.class);

    private static final String RATING_START_STR1 = "<strong title=\"";
    private static final String RATING_END_STR1 = " based on";
    private static final String REGEX_STRING1 = Pattern.quote(RATING_START_STR1) + "(.*?)" + Pattern.quote(RATING_END_STR1);
    private static final Pattern PATTERN1 = Pattern.compile(REGEX_STRING1);

    private static final String RATING_START_STR2 = "<div class=\"titlePageSprite star-box-giga-star\">";
    private static final String RATING_END_STR2 = "</div>";
    private static final String REGEX_STRING2 = Pattern.quote(RATING_START_STR2) + "(.*?)" + Pattern.quote(RATING_END_STR2);
    private static final Pattern PATTERN2 = Pattern.compile(REGEX_STRING2);

    private static final String RATING_START_STR3 = "<span itemprop=\"ratingValue\">";
    private static final String RATING_END_STR3 = "</span>";
    private static final String REGEX_STRING3 = Pattern.quote(RATING_START_STR3) + "(.*?)" + Pattern.quote(RATING_END_STR3);
    private static final Pattern PATTERN3 = Pattern.compile(REGEX_STRING3);

    public void extractRating(String pageSource, Title title, int timestamp) {
        List<Pattern> patterns = Arrays.asList(PATTERN1, PATTERN2, PATTERN3);

        for (int i = 0; i < patterns.size(); i++) {
            Pattern pattern = patterns.get(i);
            Matcher matcher = pattern.matcher(pageSource);
            try {
                if (matcher.find()) {
                    String ratingAsString = matcher.group(1).replaceAll("[^\\d.]", "").replace(" ", "");
                    float rating = Float.parseFloat(ratingAsString);
                    title.addRatingSnapshot(new RatingSnapshot(timestamp, rating));

                    return;
                } else {
                    LOGGER.warn("No rating found for title: {} ({}) with matcher #{}", title.getImdbId(), title.getName(), i);
                }
            } catch (Exception e) {
                LOGGER.error("Exception while extracting rating from page for title: {} ({}) with matcher #{}", title.getImdbId(), title.getName(), i, e);
            }
        }
    }
}
