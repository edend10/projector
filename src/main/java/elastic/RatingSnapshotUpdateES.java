package elastic;

import java.util.List;

public class RatingSnapshotUpdateES {
    private RatingSnapshotUpdateESDoc doc;

    public RatingSnapshotUpdateES(List<RatingSnapshotES> ratingSnapshots) {
        this.doc = new RatingSnapshotUpdateESDoc(ratingSnapshots);
    }

    public RatingSnapshotUpdateESDoc getDoc() {
        return doc;
    }

    public void setDoc(RatingSnapshotUpdateESDoc doc) {
        this.doc = doc;
    }
}
