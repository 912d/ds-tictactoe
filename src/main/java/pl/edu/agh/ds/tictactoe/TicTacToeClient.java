package pl.edu.agh.ds.tictactoe;

import pl.edu.agh.ds.tictactoe.noteboard.PropertiesLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Properties;
import java.util.Scanner;

import static java.lang.Thread.sleep;

public class TicTacToeClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(TicTacToeClient.class);

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
        String gameServiceName = "";
        try {
            properties = PropertiesLoader.loadProperties(propertiesFilepath);
            rmiRegistryIP = properties.getProperty("rmiRegistryIP");
            rmiRegistryPort = properties.getProperty("rmiRegistryPort");
            gameServiceName = properties.getProperty("gameServiceName");
        } catch (IOException e) {
            LOGGER.error(String.format("Error while loading properties from: %s", propertiesFilepath), e);
            System.exit(1);
        }

        try {
            IGame game = (IGame) Naming.lookup(String.format("rmi://%s:%s/%s", rmiRegistryIP, rmiRegistryPort,
                    gameServiceName));

            Scanner scanner = new Scanner(System.in);
            System.out.print("Type your nickname: ");
            InputStreamReader in = new InputStreamReader(System.in);
            OutputStreamWriter out = new OutputStreamWriter(System.out);
            Player player =
                    new Player(scanner.nextLine(), in, out);

            // I want to play with player
            game.startGameWithPlayer(player.getNick(), player);

            while (true) {
                sleep(1);
            }

        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            LOGGER.error("Error: ", e);
        } catch (InterruptedException e) {
            LOGGER.error("Thread was interrupted.", e);
        } catch (IOException e) {
            LOGGER.error("IO Error: ", e);
        }
    }
}
