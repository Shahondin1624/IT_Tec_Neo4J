package connection.pipeline;

public class ConnectionBuilderPortImpl implements ConnectionBuilder_Port {
    private final String protocol;
    private final String ip;

    ConnectionBuilderPortImpl(String protocol, String ip) {
        this.protocol = protocol;
        this.ip = ip;
    }

    @Override
    public ConnectionBuilder_Authentication withPort(int port) {
        return new ConnectionBuilderAuthenticationImpl(protocol, ip, port);
    }
}
