package elastic;

import title.RatingSnapshot;

import java.util.List;
import java.util.Set;

public class TitleES {
    private String name;
    private Integer releaseTimestamp;
    private Set<String> genres;
    private List<RatingSnapshotES> ratingSnapshots;
    private Integer raters;
    private String pageSource;

    public TitleES(String name, Integer releaseTimestamp,
            Set<String> genres, List<RatingSnapshotES> ratingSnapshots,
            Integer raters, String pageSource) {
        this.name = name;
        this.releaseTimestamp = releaseTimestamp;
        this.genres = genres;
        this.ratingSnapshots = ratingSnapshots;
        this.raters = raters;
        this.pageSource = pageSource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getReleaseTimestamp() {
        return releaseTimestamp;
    }

    public void setReleaseTimestamp(Integer releaseTimestamp) {
        this.releaseTimestamp = releaseTimestamp;
    }

    public Set<String> getGenres() {
        return genres;
    }

    public void setGenres(Set<String> genres) {
        this.genres = genres;
    }

    public List<RatingSnapshotES> getRatingSnapshots() {
        return ratingSnapshots;
    }

    public void setRatingSnapshots(List<RatingSnapshotES> ratingSnapshots) {
        this.ratingSnapshots = ratingSnapshots;
    }

    public Integer getRaters() {
        return raters;
    }

    public void setRaters(Integer raters) {
        this.raters = raters;
    }

    public String getPageSource() {
        return pageSource;
    }

    public void setPageSource(String pageSource) {
        this.pageSource = pageSource;
    }
}
