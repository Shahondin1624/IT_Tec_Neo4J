package connection.pipeline;

import org.neo4j.driver.AuthToken;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.UnknownHostException;

public class ConnectionBuilderAuthenticationImpl implements ConnectionBuilder_Authentication {
    private final String protocol;
    private final String ip;
    private final int port;

    ConnectionBuilderAuthenticationImpl(String protocol, String ip, int port) {
        this.protocol = protocol;
        this.ip = ip;
        this.port = port;
    }

    @Override
    public CustomNeo4JConnection withoutAuthentication() {
        AuthToken authToken = AuthTokens.none();
        return new CustomNeo4JConnection(createDriver(authToken));
    }

    @Override
    public CustomNeo4JConnection withUsernameAndPassword(String username, String password) {
        AuthToken authToken = AuthTokens.basic(username, password);
        return new CustomNeo4JConnection(createDriver(authToken));
    }

    protected Driver createDriver(AuthToken token) {
        return GraphDatabase.driver(formatUri(), token);
    }

    //protocol://ip:port
    protected String formatUri() {
        validateIp(ip);
        validatePort(port);
        return String.format("%s://%s:%d", protocol, ip, port);
    }

    protected void validateIp(String ip) {
        try {
            var __ = Inet4Address.getByName(ip);
        } catch (UnknownHostException unknownHostException) {
            try {
                var __ = Inet6Address.getByName(ip);
            } catch (UnknownHostException e) {
                throw new RuntimeException("Could not resolve ip address: " + ip, e);
            }
        }
    }

    protected void validatePort(int port) {
        if (port <= 0 || port >= 65536) {
            throw new RuntimeException("Invalid port: " + port);
        }
    }
}
