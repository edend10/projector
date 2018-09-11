package extractor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import title.Title;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RatersExtractor implements Extractor {
    private static final Logger LOGGER = LoggerFactory.getLogger(GenreExtractor.class);

    private static final String RATING_START_STR = "<span class=\"small\">";
    private static final String RATING_END_STR = "</span>";
    private static final String REGEX_STRING = Pattern.quote(RATING_START_STR) + "(.*?)" + Pattern.quote(RATING_END_STR);
    private static final Pattern PATTERN = Pattern.compile(REGEX_STRING);

    @Override
    public void extract(String pageSource, Title title) {
        Matcher matcher = PATTERN.matcher(pageSource);

        while (matcher.find()) {
            String ratersRaw = matcher.group(1).replace(" ", "").replace(",", "");
            if (!ratersRaw.equals("")) {
                Integer raters = Integer.parseInt(ratersRaw);
                title.setRaters(raters);
            }
        }

        if (title.getGenres().size() == 0) {
            LOGGER.warn("No raters found for title {}", title);
        }
    }
}
