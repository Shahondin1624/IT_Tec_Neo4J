package queryfilehandling;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class QueryFileReader {
    private final List<QueryBuilder> queryBuilders = new ArrayList<>();
    private final String filePath;

    public QueryFileReader(String filePath) {
        this.filePath = filePath;
    }

    public List<CustomQuery> buildQueries() {
        String workDir = System.getProperty("user.dir");
        Path path = Path.of(workDir).resolve(filePath);
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("creation.cypher");
             BufferedReader lines = new BufferedReader(new InputStreamReader(Objects.requireNonNull(is)))) {
            String line;
            while ((line = lines.readLine()) != null) {
                handleLine(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return queryBuilders.stream()
                .map(queryBuilder -> new CustomQuery(queryBuilder.description, queryBuilder.queryBuilder.toString()))
                .filter(query -> !isNullOrBlank(query.query()))
                .collect(Collectors.toList());
    }

    private Optional<QueryBuilder> latest() {
        int size = queryBuilders.size();
        if (size > 0) {
            return Optional.of(queryBuilders.get(--size));
        } else {
            return Optional.empty();
        }
    }

    private void handleLine(String line) {
        String trimmed = line.trim();
        QueryBuilder queryBuilder = latest().orElseGet(this::addNewBuilder);
        if (trimmed.startsWith("//")) {
            queryBuilder.description = trimmed.replaceAll("//", "");
        } else {
            queryBuilder.queryBuilder.add(trimmed);
        }
        if (trimmed.endsWith(";")) {
            addNewBuilder();
        }
    }

    private QueryBuilder addNewBuilder() {
        QueryBuilder builder = new QueryBuilder();
        queryBuilders.add(builder);
        return builder;
    }

    private boolean isNullOrBlank(String str) {
        return str == null || str.isEmpty();
    }

    private static class QueryBuilder {
        protected String description;
        protected StringJoiner queryBuilder = new StringJoiner("\n");


    }
}
