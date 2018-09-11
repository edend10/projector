package elastic;

import java.util.List;

public class RatingSnapshotUpdateESDoc {
    private List<RatingSnapshotES> ratingSnapshots;

    public RatingSnapshotUpdateESDoc(List<RatingSnapshotES> ratingSnapshots) {
        this.ratingSnapshots = ratingSnapshots;
    }

    public List<RatingSnapshotES> getRatingSnapshots() {
        return ratingSnapshots;
    }

    public void setRatingSnapshots(List<RatingSnapshotES> ratingSnapshots) {
        this.ratingSnapshots = ratingSnapshots;
    }
}
