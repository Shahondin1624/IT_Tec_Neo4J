package connection.pipeline;

@FunctionalInterface
public interface ConnectionBuilder_Ip {
    ConnectionBuilder_Port withIp(String ip);
}
