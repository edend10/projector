package extractor;

import title.Title;

public interface Extractor {
    void extract(String pageSource, Title title);
}
