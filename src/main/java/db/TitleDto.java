package db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="title")
public class TitleDto {
    @Id
    @Column(name = "id")
    private Integer imdbId;
    @Column(name = "name")
    private String name;
    @Column(name="release_timestamp")
    private Integer releaseTimestamp;
    @Column(name="budget")
    private Integer budget;
    @Column(name="raters")
    private Integer raters;

    public Integer getImdbId() {
        return imdbId;
    }

    public void setImdbId(Integer imdbId) {
        this.imdbId = imdbId;
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

    public Integer getBudget() {
        return budget;
    }

    public void setBudget(Integer budget) {
        this.budget = budget;
    }

    public Integer getRaters() {
        return raters;
    }

    public void setRaters(Integer raters) {
        this.raters = raters;
    }
}
