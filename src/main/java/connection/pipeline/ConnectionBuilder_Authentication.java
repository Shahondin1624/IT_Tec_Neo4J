package connection.pipeline;

public interface ConnectionBuilder_Authentication {
    CustomNeo4JConnection withoutAuthentication();
    CustomNeo4JConnection withUsernameAndPassword(String username, String password);
}
