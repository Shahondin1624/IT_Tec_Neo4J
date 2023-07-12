package connection.pipeline;

import org.neo4j.driver.Record;
import org.neo4j.driver.*;
import org.neo4j.driver.exceptions.Neo4jException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

public class CustomNeo4JConnection implements AutoCloseable {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final Driver driver;
    private final List<Session> sessions = new ArrayList<>();

    CustomNeo4JConnection(Driver driver) {
        this.driver = driver;
    }

    @Override
    public void close() throws Exception {
        if (driver != null) {
            driver.close();
        } else {
            throw new Exception(new Neo4jException("Could not close connection because driver was null"));
        }
    }

    public Session openNewSession() {
        Session session = driver.session();
        sessions.add(session);
        return session;
    }

    public Session existingSession() {
        if (sessions.isEmpty()) {
            return openNewSession();
        } else {
            return sessions.iterator().next();
        }
    }

    public boolean closeSession(Session session) {
        if (sessions.contains(session)) {
            return sessions.remove(session);
        } else return false;
    }

    public Transaction beginTransaction() {
        Session session = existingSession();
        return session.beginTransaction();
    }

    public void runQuery(String line) {
        try (Transaction tx = beginTransaction();) { //outer try block for automatic commit or rollback
            try {
                Result result = tx.run(line);
                handleQueryResult(result, line);
                tx.commit();
            } catch (Exception e) {
                logStackTrace(e);
                log.info("Rolling back transaction...");
                tx.rollback();
            }
        } catch (Exception e) {
            logStackTrace(e);
        }
    }

    public void handleQueryResult(Result result, String query) {
        log.info("Showing the result for query: {}", query);
        while (result.hasNext()) {
            Record next = result.next();
            log.info("{}", next.asMap());
        }
        log.info("Finished printing results");
    }

    public static String getStackTrace(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        return stringWriter.toString();
    }

    private static void logStackTrace(Exception e) {
        log.error("{}", getStackTrace(e));
    }
}
