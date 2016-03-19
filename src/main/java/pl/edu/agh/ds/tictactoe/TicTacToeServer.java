package pl.edu.agh.ds.tictactoe;

import pl.edu.agh.ds.tictactoe.noteboard.PropertiesLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Properties;

public class TicTacToeServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(TicTacToeServer.class);
    private static final String PROPERTIES_FILEPATH = "src/main/resources/server.properties";

    public static void main(String[] args) {
        String propertiesFilepath;
        if (args.length == 0) {
            propertiesFilepath = PROPERTIES_FILEPATH;
            System.out.println("You can specify a properties file by providing the file path as an argument.");
            System.out.println("Using default filepath for properties file: " + propertiesFilepath);
        } else {
            propertiesFilepath = args[0];
            System.out.println("Using properties file: " + propertiesFilepath);
        }

        int port = 0;
        String gameServiceName = "";
        try {
            Properties properties = PropertiesLoader.loadProperties(propertiesFilepath);
            port = Integer.valueOf(properties.getProperty("rmiRegistryPort"));
            gameServiceName = properties.getProperty("gameServiceName");
        } catch (IOException e) {
            LOGGER.error("Error while loading properties from: " + propertiesFilepath, e);
            System.exit(1);
        }

        try {
            Registry registry = LocateRegistry.createRegistry(port);
            IGame gameImpl = new Game();
            IGame game = (IGame) UnicastRemoteObject.exportObject(gameImpl, port);
            registry.rebind(gameServiceName, game);

        } catch (RemoteException e) {
            LOGGER.error("Error: ", e);
        }
    }
}
