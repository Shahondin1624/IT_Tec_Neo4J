import connection.pipeline.ConnectionBuilder;
import connection.pipeline.CustomNeo4JConnection;
import console.ConsoleInterface;
import org.neo4j.driver.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import queryfilehandling.CustomQuery;
import queryfilehandling.QueryFileReader;

import java.lang.invoke.MethodHandles;
import java.util.List;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static void main(String[] args) {
        try (CustomNeo4JConnection connection = initializeConnection(args)) {
            loadInitialData(connection);
            if (System.in == null) {
                while (true) {
                    //let this application run indefinitely
                }
            } else {
                ConsoleInterface consoleInterface = new ConsoleInterface(connection, System.in);
                consoleInterface.loopAndAwaitInput();
            }
        } catch (Exception e) {
            log.error("{}", CustomNeo4JConnection.getStackTrace(e));
            throw new RuntimeException(e);
        }
    }

    private static void loadInitialData(CustomNeo4JConnection connection) {
        log.info("Reading file with db-schema-init queries...");
        List<CustomQuery> creationQueries = readFile("Project/creation.cypher");
        Transaction tx = connection.beginTransaction();
        log.info("Running queries...");
        creationQueries.forEach(customQuery -> customQuery.run(tx));
        tx.commit();
    }

    private static CustomNeo4JConnection initializeConnection(String[] args) {
        if (args.length != 5) {
            throw new IllegalArgumentException("Not 5 arguments provided!");
        }
        String protocol = args[0];
        String ip = args[1];
        String port = args[2];
        String username = args[3];
        String password = args[4];
        log.info("protocol: {}", protocol);
        log.info("ip: {}", ip);
        log.info("port: {}", port);
        log.info("username: {}", username);
        log.info("password: {}", password);
        int retries = 0;
        while (retries < 10) {
            try {
                return ConnectionBuilder.newConnection()
                        .withProtocol(protocol)
                        .withIp(ip)
                        .withPort(Integer.parseInt(port))
                        .withUsernameAndPassword(username, password);
            } catch (Exception e) {
                int waitTime = retries * 2000;
                log.warn("Server not responding, waiting for {}ms...", waitTime);
                try {
                    Thread.sleep(retries);
                } catch (InterruptedException ex) {
                    log.error("{}", CustomNeo4JConnection.getStackTrace(ex));
                    throw new RuntimeException(ex);
                }
            }
            retries++;
        }
        IllegalStateException ex = new IllegalStateException("Was not able to connect to database!");
        log.error("{}", CustomNeo4JConnection.getStackTrace(ex));
        throw ex;
    }

    private static List<CustomQuery> readFile(String fileName) {
        QueryFileReader reader = new QueryFileReader(fileName);
        return reader.buildQueries();
    }


}
