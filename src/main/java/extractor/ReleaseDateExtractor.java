package extractor;

import title.Title;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReleaseDateExtractor implements Extractor {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReleaseDateExtractor.class);

    private static final String RATING_START_STR = "<h4 class=\"inline\">Release Date:</h4>";
    private static final String RATING_END_STR = "<span";
    private static final String REGEX_STRING = Pattern.quote(RATING_START_STR) + "(.*?)" + Pattern.quote(RATING_END_STR);
    private static final Pattern PATTERN = Pattern.compile(REGEX_STRING);

    @Override
    public void extract(String pageSource, Title title) {
        Matcher matcher = PATTERN.matcher(pageSource);

        try {
            if (matcher.find()) {
                Integer releaseDate = formatReleaseDate(matcher.group(1));
                title.setReleaseTimestamp(releaseDate);
            } else {
                LOGGER.warn("No release date found for for title: {}", title.getImdbId());
            }
        } catch (Exception e) {
            LOGGER.warn("Exception while parsing release date for title: {}. -> {}", title.getImdbId(), e.getClass());
        }
    }

    private Integer formatReleaseDate(String releaseDateRawStr) throws ParseException {
        String releaseDateStr = releaseDateRawStr.replace("\"", "").replace("(USA)", "").trim();
        DateFormat strFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
        Date date = strFormat.parse(releaseDateStr);

        DateFormat timestampFormat = new SimpleDateFormat("yyyyMMdd");

        return Integer.parseInt(timestampFormat.format(date));
    }
}
