package wayback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

public class WaybackApiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WaybackApiService.class);

    private static final long DAY_DIFF_THRESHOLD = 14;
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final String WAYBACK_API_AVAILABILITY_URL_FORMAT = "http://archive.org/wayback/available?url=http://www.imdb.com/title/tt%d&timestamp=%d";

    private WaybackApiClient client;

    public WaybackApiService(WaybackApiClient client) {
        this.client = client;
    }

    public String getWaybackUrl(int imdbId, int targetTimestamp) {
        String url = String.format(WAYBACK_API_AVAILABILITY_URL_FORMAT, imdbId, targetTimestamp);
        WaybackApiResponse response = client.getWaybackResponse(url);

        if (response != null &&
                response.getArchivedSnapshots() != null &&
                response.getArchivedSnapshots().getClosest() != null &&
                response.getArchivedSnapshots().getClosest().isAvailable()) {
            if (dayDifference(targetTimestamp, response.getArchivedSnapshots().getClosest().getTimestamp()) > DAY_DIFF_THRESHOLD) {
                LOGGER.debug("wayback url too far from target date imdbId: {} targetTimestamp: {}", imdbId, targetTimestamp);
                return "";
            }
            return response.getArchivedSnapshots().getClosest().getUrl();
        } else {
            LOGGER.debug("wayback url unavailable for imdbId: {} targetTimestamp: {}", imdbId, targetTimestamp);
            return "";
        }
    }

    private long dayDifference(int targetTimestamp, String timestamp) {
        LocalDate date1 = LocalDate.parse(String.valueOf(targetTimestamp), DATE_FORMAT);
        LocalDate date2 = LocalDate.parse(timestamp.substring(0, 8), DATE_FORMAT);

        return Math.abs(ChronoUnit.DAYS.between(date1, date2));
    }
}
