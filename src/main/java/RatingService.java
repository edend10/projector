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
    private static final int EXTRA_DAYS_OFFSET = 10;

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
        int daysSinceRelease = subtractDatesForDays(
                Integer.parseInt(LocalDate.now().format(DATE_FORMATTER)),
                title.getReleaseTimestamp(),
                EXTRA_DAYS_OFFSET);

        //TODO: remove this
        daysSinceRelease = daysSinceRelease > 5 ? 5 : daysSinceRelease;

        IntStream.range(0, daysSinceRelease).forEach(offset -> {
            int timestamp = addDays(title.getReleaseTimestamp(), offset);
            String waybackUrl = waybackApiService.getWaybackUrl(title.getImdbId(), timestamp);
            if (!waybackUrl.isEmpty()) {
                String pageSource = pageSourceClient.getWebPageSource(waybackUrl);
                if (!pageSource.isEmpty()) {
                    LOGGER.info("Extracting ratings for imdbId: {}, offset: {}, timestamp: {}",
                            title.getImdbId(), offset, timestamp);
                    ratingExtractor.extractRating(pageSource, title, timestamp);
                } else {
                    LOGGER.info("Empty page source for url: {}, imdbId: {}", waybackUrl, title.getImdbId());
                }
            } else {
                LOGGER.debug("No wayback url found for imdbId: {}, offset: {}, timestamp: {}",
                        title.getImdbId(), offset, timestamp);
            }
        });
    }

    private int subtractDatesForDays(int todayTimestamp, int releaseTimestamp, int extraDaysOffset) {
        return (int) (formatDate(todayTimestamp).toEpochDay() - formatDate(releaseTimestamp).toEpochDay()) + extraDaysOffset;
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
