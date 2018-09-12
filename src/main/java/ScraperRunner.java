import com.wix.mysql.EmbeddedMysql;
import com.wix.mysql.config.MysqldConfig;
import db.*;
import elastic.ElasticSearchService;
import extractor.RatingExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import pagesource.PageSourceClient;
import pagesource.PageSourceService;
import pagesource.TitlePageParser;
import pagesource.YearPageParser;
import title.Title;
import wayback.WaybackApiClient;
import wayback.WaybackApiService;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;

import static com.wix.mysql.EmbeddedMysql.anEmbeddedMysql;
import static com.wix.mysql.ScriptResolver.classPathScript;
import static com.wix.mysql.config.MysqldConfig.aMysqldConfig;
import static com.wix.mysql.distribution.Version.v5_6_latest;

public class ScraperRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScraperRunner.class);
    private static final String USER = "hibernate.connection.username";
    private static final String PASSWORD = "hibernate.connection.password";
    private static final String MAX_PAGES = "max.year.pages";
    private static final String MAX_RATINGS_SNAPS = "max.rating.snaps";
    private static final String ELASTICSEARCH_URL = "elasticsearch.url";

    public static void main(String[] args) throws IOException {

        Properties properties = new Properties();
        properties.load(new FileInputStream("src/main/resources/projector.properties"));

        int maxPagesToTry = Integer.parseInt(properties.getProperty(MAX_PAGES));
        int maxRatingSnapsToQuery = Integer.parseInt(properties.getProperty(MAX_RATINGS_SNAPS));
        String elasticSearchUrl = properties.getProperty(ELASTICSEARCH_URL);

        boolean dev = false;
        EmbeddedMysql embeddedMysql = null;

        if (dev) {
            MysqldConfig config = aMysqldConfig(v5_6_latest)
                    .withPort(3306)
                    .withUser(properties.getProperty(USER),
                            properties.getProperty(PASSWORD))
                    .build();

           embeddedMysql = anEmbeddedMysql(config)
                    .addSchema("imdb", classPathScript("db/sql/create-db.sql"))
                    .start();
             }

        List<Integer> yearsToParse = Arrays.asList(2016); //TODO: get from args

        PageSourceClient pageSourceClient = new PageSourceClient();
        WaybackApiClient waybackApiClient = new WaybackApiClient();
        WaybackApiService waybackApiService = new WaybackApiService(waybackApiClient);
        RatingExtractor ratingExtractor = new RatingExtractor();

        Scraper scraper = new Scraper(
                new PageSourceService(pageSourceClient),
                new YearPageParser(),
                new TitlePageParser(),
                new RatingService(waybackApiService,
                        pageSourceClient,
                        ratingExtractor,
                        maxRatingSnapsToQuery),
                new ElasticSearchService(elasticSearchUrl),
                maxPagesToTry);

        scraper.scrape(yearsToParse);

        if (dev) {
            embeddedMysql.stop();
        }
    }
}
