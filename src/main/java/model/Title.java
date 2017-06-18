package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Title {

    private int imdbId;
    private String name;
    private int releaseTimestamp;
    private Set<String> genres;
    private List<RatingSnapshot> ratingSnapshots;

    public Title() {
        this.ratingSnapshots = new ArrayList<>();
    }

    public int getImdbId() {
        return imdbId;
    }

    public void setImdbId(int imdbId) {
        this.imdbId = imdbId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getReleaseTimestamp() {
        return releaseTimestamp;
    }

    public void setReleaseTimestamp(int releaseTimestamp) {
        this.releaseTimestamp = releaseTimestamp;
    }

    public Set<String> getGenres() {
        return genres;
    }

    public void setGenres(Set<String> genres) {
        this.genres = genres;
    }

    public List<RatingSnapshot> getRatingSnapshots() {
        return ratingSnapshots;
    }

    public void addRatingSnapshot(RatingSnapshot ratingSnapshots) {
        this.ratingSnapshots.add(ratingSnapshots);
    }

}
