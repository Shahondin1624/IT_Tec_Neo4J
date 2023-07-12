package connection;

import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;

import java.util.HashMap;
import java.util.List;

public class SessionHelper {
    private final HashMap<Session, List<Transaction>> map = new HashMap<>();
}
