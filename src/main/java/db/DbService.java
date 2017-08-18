package db;

import title.RatingSnapshot;
import title.Title;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DbService {

    private final TitleDao titleDao;
    private final RatingDao ratingDao;
    private final GenreDao genreDao;

    public DbService(TitleDao titleDao,
                     RatingDao ratingDao,
                     GenreDao genreDao) {
        this.titleDao = titleDao;
        this.ratingDao = ratingDao;
        this.genreDao = genreDao;
    }

    public void saveTitleMap(Map<Integer, Title> titleMap) {
        titleDao.saveTitles(toTitleDtos(titleMap));

        ratingDao.saveRatings(toRatingDtos(titleMap));

        //TODO: save genres
    }

    private Set<TitleDto> toTitleDtos(Map<Integer, Title> titleMap) {
        return titleMap.values()
                .stream()
                .map(this::toTitleDto)
                .collect(Collectors.toSet());
    }

    private Set<RatingDto> toRatingDtos(Map<Integer, Title> titleMap) {
        return titleMap.values()
                .stream()
                .flatMap(this::parseRatingSnapshots)
                .collect(Collectors.toSet());
    }

    private TitleDto toTitleDto(Title title) {
        TitleDto dto = new TitleDto();
        dto.setImdbId(title.getImdbId());
        dto.setName(title.getName());
        dto.setReleaseTimestamp(title.getReleaseTimestamp());
        dto.setBudget(title.getBudget());
        return dto;
    }

    private Stream<RatingDto> parseRatingSnapshots(Title title) {
        return title.getRatingSnapshots()
                .stream()
                .map(ratingSnapShot ->
                        toRatingDto(title.getImdbId(), ratingSnapShot));

    }

    private RatingDto toRatingDto(Integer titleId, RatingSnapshot ratingSnapshot) {
        RatingDto ratingDto = new RatingDto();
        ratingDto.setTitleId(titleId);
        ratingDto.setRating(ratingSnapshot.getRating());
        ratingDto.setTimestamp(ratingDto.getTimestamp());
        return ratingDto;
    }
}
