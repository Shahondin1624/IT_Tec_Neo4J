package connection.pipeline;

@FunctionalInterface
public interface ConnectionBuilder_Protocol {
    ConnectionBuilder_Ip withProtocol(String protocol);
}
