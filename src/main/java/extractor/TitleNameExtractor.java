package extractor;

import title.Title;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TitleNameExtractor implements Extractor {
    private static final Logger LOGGER = LoggerFactory.getLogger(TitleNameExtractor.class);

    private static final String RATING_START_STR = "<title>";
    private static final String RATING_END_STR = "</title>";
    private static final String REGEX_STRING = Pattern.quote(RATING_START_STR) + "(.*?)" + Pattern.quote(RATING_END_STR);
    private static final Pattern PATTERN = Pattern.compile(REGEX_STRING);

    @Override
    public void extract(String pageSource, Title title) {
        Matcher matcher = PATTERN.matcher(pageSource);

        if (matcher.find()) {
            String name = formatTitle(matcher.group(1));
            title.setName(name);
        } else {
            LOGGER.warn("No name found for title: {}", title.getImdbId());
        }
    }

    private String formatTitle(String rawTitle) {
        return rawTitle.toLowerCase().replace(" - imdb", "").replaceAll("(\\()[^&]*(\\))", "").trim();
    }
}
