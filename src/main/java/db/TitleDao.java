package db;

import java.util.Set;

public interface TitleDao {
    void saveTitles(Set<TitleDto> titles);
}
