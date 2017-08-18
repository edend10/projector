package db;

import title.RatingSnapshot;

import java.util.Set;

public interface RatingDao {
    void saveRatings(Set<RatingDto> ratings);
}
