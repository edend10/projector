package elastic;

public class RatingSnapshotES {
    private Integer timestamp;
    private float rating;

    public RatingSnapshotES(int timestamp, float rating) {
        this.timestamp = timestamp;
        this.rating = rating;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
