package extractor;

import model.Title;

public interface Extractor {
    void extract(String pageSource, Title title);
}
