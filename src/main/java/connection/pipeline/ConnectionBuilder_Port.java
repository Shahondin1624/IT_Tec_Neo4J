package connection.pipeline;

@FunctionalInterface
public interface ConnectionBuilder_Port {
    ConnectionBuilder_Authentication withPort(int port);
}
