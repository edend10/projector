package model;

public class RatingSnapshot {

    private int timestamp;
    private float rating;

    public RatingSnapshot(int timestamp, float rating) {
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
