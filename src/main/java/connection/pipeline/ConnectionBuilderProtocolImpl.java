package connection.pipeline;

public class ConnectionBuilderProtocolImpl implements ConnectionBuilder_Protocol {

    ConnectionBuilderProtocolImpl() {
    }

    @Override
    public ConnectionBuilder_Ip withProtocol(String protocol) {
        return new ConnectionBuilderIpImpl(protocol);
    }
}
