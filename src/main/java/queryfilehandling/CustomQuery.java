package queryfilehandling;

import org.neo4j.driver.Result;
import org.neo4j.driver.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public record CustomQuery(String description, String query) {
    private static final Logger LOG = LoggerFactory.getLogger(CustomQuery.class);
    public static final CustomQuery QUERY_1 = new CustomQuery("Count all direct neighbors",
            "MATCH (s:Station)-[:Type]->(n) RETURN s.StationName AS `station name`, COUNT(n) AS `neighbors` ORDER BY `neighbors` DESC;"
    );

    public static final CustomQuery QUERY_2 = new CustomQuery("Show all direct neighbors",
            "MATCH (s:Station)-[:Type]->(n) RETURN s.StationName AS `station name`, COUNT(n) AS `neighbor count`, COLLECT(n.StationName) AS `neighbors`  ORDER BY `neighbor count` DESC;"
    );
    public static final CustomQuery QUERY_3 = new CustomQuery("Show all neighbors within 5 hops",
            "MATCH (s:Station)-[:Type*1..5]->(n) RETURN s.StationName AS `station name`, COUNT(DISTINCT n) AS `reachable within 5 hops`, COLLECT(DISTINCT n.StationName) AS `station names` ORDER BY `reachable within 5 hops` DESC;"
    );
    public static final CustomQuery QUERY_4 = new CustomQuery("Show route costs",
            "MATCH (s1:Station)-[r:Type]->(s2:Station) RETURN s1.StationName AS `Station 1`, s2.StationName AS `Station 2`, r.cost AS Cost ORDER BY r.cost;"
    );
    private static final List<CustomQuery> QUERIES = List.of(QUERY_1, QUERY_2, QUERY_3, QUERY_4);

    public static List<CustomQuery> definedQueries() {
        return QUERIES;
    }

    public Result run(Transaction tx) {
        boolean shouldNotBeExecuted = query == null || query.isEmpty();
        if (!shouldNotBeExecuted) {
            LOG.info("Executing query: {}", this);
            return tx.run(query);
        } else {
            throw new RuntimeException(String.format("Running this query is not allowed -> null or blank: %s", query));
        }
    }
}
