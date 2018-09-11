package db;

import org.springframework.data.jpa.repository.JpaRepository;
import title.RatingSnapshot;

import java.util.Set;

public interface RatingDao extends JpaRepository<RatingDto, Integer> {
    void saveRatings(Set<RatingDto> ratings);
}
