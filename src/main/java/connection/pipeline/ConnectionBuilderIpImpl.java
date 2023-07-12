package connection.pipeline;

public class ConnectionBuilderIpImpl implements ConnectionBuilder_Ip {
    private final String protocol;

    ConnectionBuilderIpImpl(String protocol) {
        this.protocol = protocol;
    }

    @Override
    public ConnectionBuilder_Port withIp(String ip) {
        return new ConnectionBuilderPortImpl(protocol, ip);
    }
}
