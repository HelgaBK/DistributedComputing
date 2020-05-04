package sample.server;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;

public class Server {
    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {
        RemoteServerImpl server = new RemoteServerImpl();
        Registry registry = LocateRegistry.createRegistry(65000);
        registry.rebind("Server", server);
    }
}
