package connection.pipeline;

public class ConnectionBuilder {

    private ConnectionBuilder() {

    }

    public static ConnectionBuilder_Protocol newConnection() {
        return new ConnectionBuilderProtocolImpl();
    }
}
