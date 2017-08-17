import extractor.RatingExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pagesource.PageSourceClient;
import title.Title;
import wayback.WaybackApiService;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class RatingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RatingService.class);

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final List<Integer> OFFSETS = Arrays.asList(0, 7, 30, 90, 180, 365);

    private WaybackApiService waybackApiService;
    private PageSourceClient pageSourceClient;
    private RatingExtractor ratingExtractor;

    public RatingService(WaybackApiService waybackApiService,
                         PageSourceClient pageSourceClient,
                         RatingExtractor ratingExtractor) {
        this.waybackApiService = waybackApiService;
        this.pageSourceClient = pageSourceClient;
        this.ratingExtractor = ratingExtractor;
    }

    public void addRatingSnapshots(Title title) {
        int daysSinceRelease = subtractDates(Integer.parseInt(LocalDate.now().format(DATE_FORMATTER)),
                title.getReleaseTimestamp());
        IntStream.of(daysSinceRelease).forEach(offset -> {
            int timestamp = addDays(title.getReleaseTimestamp(), offset);
            String waybackUrl = waybackApiService.getWaybackUrl(title.getImdbId(), timestamp);
            if (!waybackUrl.isEmpty()) {
                String pageSource = pageSourceClient.getWebPageSource(waybackUrl);
                if (!pageSource.isEmpty()) {
                    ratingExtractor.extractRating(pageSource, title, timestamp);
                } else {
                    LOGGER.error("Empty page source for url: {}, imdbId: {}", waybackUrl, title.getImdbId());
                }
            } else {
                LOGGER.error("No wayback url found for imdbId: {} timestamp: {}", title.getImdbId(), timestamp);
            }
        });
    }

    private int subtractDates(int todayTimestamp, int releaseTimestamp) {
        Period period = Period.between(formatDate(todayTimestamp), formatDate(releaseTimestamp));
        return period.getDays();
    }

    private LocalDate formatDate(int todayTimestamp) {
        return LocalDate.parse(String.valueOf(todayTimestamp), DATE_FORMATTER);
    }

    private int addDays(int timestamp, int numOfDays) {
        return Integer.valueOf(LocalDate.parse(String.valueOf(timestamp), DATE_FORMATTER)
                .plusDays(numOfDays)
                .format(DATE_FORMATTER));

    }
}
