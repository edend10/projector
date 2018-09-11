package db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface TitleDao extends JpaRepository<TitleDto, Integer> {
    void saveTitles(Set<TitleDto> titles);
}
