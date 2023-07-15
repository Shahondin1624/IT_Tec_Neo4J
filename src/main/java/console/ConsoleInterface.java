package console;

import connection.pipeline.CustomNeo4JConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandles;

public class ConsoleInterface {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final CustomNeo4JConnection connection;
    private final BufferedReader input;

    public ConsoleInterface(CustomNeo4JConnection connection, InputStream in) {
        this.connection = connection;
        this.input = new BufferedReader(new InputStreamReader(in));
    }

    public void loopAndAwaitInput() throws IOException {
        log.info("Starting interactive interface. Type in your unfiltered queries to run against db or ':STOP' to exit...");
        String line;
        while (true) {
            line = input.readLine();
            if (line == null) continue; //dirty hack to avoid container shutting down
            switch (handleLine(line)) {
                case EXIT -> {
                    log.info("Shutting down console interface...");
                    return;
                }
                case QUERY -> connection.runQuery(line);
            }
        }
    }

    protected Action handleLine(String line) { //Could be extended later on
        if (line.startsWith(":STOP")) {
            return Action.EXIT;
        } else {
            return Action.QUERY;
        }
    }

}
