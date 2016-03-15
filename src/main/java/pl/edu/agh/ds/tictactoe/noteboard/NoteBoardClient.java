package pl.edu.agh.ds.tictactoe.noteboard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Properties;

public class NoteBoardClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoteBoardServer.class);

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
        Properties properties;
        String rmiRegistryIP = "";
        String rmiRegistryPort = "";
        try {
            properties = PropertiesLoader.loadProperties(propertiesFilepath);
            rmiRegistryIP = properties.getProperty("rmiRegistryIP");
            rmiRegistryPort = properties.getProperty("rmiRegistryPort");
        } catch (IOException e) {
            LOGGER.error("Error while loading properties from: " + propertiesFilepath, e);
            System.exit(1);
        }

        try {
            INoteBoard nb = (INoteBoard) Naming.lookup("rmi://" + rmiRegistryIP + ":" + rmiRegistryPort + "/note");
            System.err.println("dodajemy: aqq1, aqq2, aqq3");
            nb.appendText("aqq1");
            nb.appendText("aqq2");
            nb.appendText("aqq3");
            System.err.println("Sprawdzamy, co jest w srodku...");
            System.err.println(nb.getText());
        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            LOGGER.error("Error: ", e);
        }
    }


}
