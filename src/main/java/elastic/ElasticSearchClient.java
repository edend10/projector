package elastic;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ElasticSearchClient {
    @POST("/imdb_test3/title/{imdbId}")
    Call<ElasticSearchResponse> insertTitle(@Path("imdbId") Integer imdbId, @Body TitleES titleES);

    @POST("/imdb_test3/title/{imdbId}/_update")
    Call<ElasticSearchResponse> updateTitleWithRatingSnapshots(@Path("imdbId") Integer imdbId, @Body RatingSnapshotUpdateES ratingSnapshots);
}
