package elastic;

import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import title.RatingSnapshot;
import title.Title;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ElasticSearchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchService.class);

    private final ElasticSearchClient elasticSearchClient;

    public ElasticSearchService() {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl("http://192.168.0.2:9200/")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        elasticSearchClient = retrofit.create(ElasticSearchClient.class);
    }

    public void insertTitle(Title title, String pageSourceStr) {

        TitleES titleES = toTitleES(title, pageSourceStr);

        elasticSearchClient.insertTitle(title.getImdbId(), titleES).enqueue(new Callback<ElasticSearchResponse>() {

            @Override
            public void onResponse(Call<ElasticSearchResponse> call, Response<ElasticSearchResponse> response) {
                if (response.body() != null) {
                    LOGGER.info("Successfully saved title: {} ({})", title.getImdbId(), title.getName());
                } else {
                    String errBody = "";
                    try {
                        errBody = response.errorBody().string();
                    } catch (IOException e) {

                    }
                    LOGGER.error("Failed to communicate with elasticsearch over http for inserting title: {} ({}). Got code {}. Message: {}",
                            title.getImdbId(), title.getName(), response.code(), errBody);
                }
            }

            @Override
            public void onFailure(Call<ElasticSearchResponse> call, Throwable t) {
                LOGGER.error("Exception while communicating with elasticsearch over http for inserting title: {} ({})", title.getImdbId(), title.getName(), t);
            }
        });
    }

    public void updateTitleWithRatingSnapshots(Integer imdbId, List<RatingSnapshot> ratingSnapshots) {
        elasticSearchClient.updateTitleWithRatingSnapshots(
                imdbId, new RatingSnapshotUpdateES(toRatingSnapshotES(ratingSnapshots)))
                .enqueue(new Callback<ElasticSearchResponse>() {
                    @Override
                    public void onResponse(Call<ElasticSearchResponse> call, Response<ElasticSearchResponse> response) {
                        if (response.body() != null) {
                            LOGGER.info("Successfully updated rating snapshots for title: {}", imdbId);
                        } else {
                            String errBody = "";
                            try {
                                errBody = response.errorBody().string();
                            } catch (IOException e) {

                            }
                            LOGGER.error("Failed to communicate with elasticsearch over http for updating rating snapshots for title: {}). Got code {}. Message: {}",
                                    imdbId, response.code(), errBody);
                        }
            }

            @Override
            public void onFailure(Call<ElasticSearchResponse> call, Throwable t) {
                LOGGER.error("Exception while communicating with elasticsearch over http for updating rating snapshots for title: {}", imdbId, t);
            }
        });
    }

    private TitleES toTitleES(Title title, String pageSourceStr) {
        List<RatingSnapshotES> snapshotESList = toRatingSnapshotES(title.getRatingSnapshots());

        return new TitleES(title.getName(), title.getReleaseTimestamp(),
                title.getGenres(), snapshotESList,
                title.getRaters(), pageSourceStr);
    }

    private List<RatingSnapshotES> toRatingSnapshotES(List<RatingSnapshot> ratingSnapshots) {
        return ratingSnapshots
                .stream()
                .map(ratingSnapshot -> new RatingSnapshotES(ratingSnapshot.getTimestamp(), ratingSnapshot.getRating()))
                .collect(Collectors.toList());
    }
}
