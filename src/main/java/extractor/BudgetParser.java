package extractor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import title.Title;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BudgetParser implements Extractor {
    private static final Logger LOGGER = LoggerFactory.getLogger(BudgetParser.class);

    private static final String RATING_START_STR = "<h4 class=\"inline\">Budget:</h4>";
    private static final String RATING_END_STR = "<span";
    private static final String REGEX_STRING = Pattern.quote(RATING_START_STR) + "(.*?)" + Pattern.quote(RATING_END_STR);
    private static final Pattern PATTERN = Pattern.compile(REGEX_STRING);

    @Override
    public void extract(String pageSource, Title title) {
        Matcher matcher = PATTERN.matcher(pageSource);

        if (matcher.find()) {
            String budgetStr = matcher.group(1);
            try {
                int budget = formatBudget(budgetStr);
                title.setBudget(budget);
            } catch (NumberFormatException e) {
                LOGGER.warn("problem parsing budget string: '{}'. For title: {}", budgetStr, title.getImdbId());
            }
        } else {
            LOGGER.warn("No budget found for page for title: {}", title.getImdbId());
        }
    }

    private int formatBudget(String budgetRawStr) {
        return Integer.valueOf(budgetRawStr
                .replaceAll("[^\\d.]", ""));
    }
}
