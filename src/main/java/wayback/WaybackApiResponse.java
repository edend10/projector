package wayback;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WaybackApiResponse {

    @JsonProperty("archived_snapshots")
    ArchivedSnapshots archivedSnapshots;

    public ArchivedSnapshots getArchivedSnapshots() {
        return archivedSnapshots;
    }

    public void setArchivedSnapshots(ArchivedSnapshots archivedSnapshots) {
        this.archivedSnapshots = archivedSnapshots;
    }
}
