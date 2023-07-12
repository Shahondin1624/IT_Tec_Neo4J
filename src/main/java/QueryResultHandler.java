import org.neo4j.driver.Result;

@FunctionalInterface
public interface QueryResultHandler<R> {
    R handle(Result result);
}
