package pl.edu.agh.ds.tictactoe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.ds.tictactoe.noteboard.PropertiesLoader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Properties;
import java.util.Scanner;

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
            IGameServer game = (IGameServer) Naming.lookup(String.format("rmi://%s:%s/%s",
                    rmiRegistryIP, rmiRegistryPort, gameServiceName));

            Scanner scanner = new Scanner(System.in);
            System.out.print("Type your nickname: ");

            Player player = new Player(scanner.nextLine());
            boolean registeredSuccessfully = game.registerPlayer(player.getNick(), player);
            while (!registeredSuccessfully) {
                System.out.println("Please try again.");
                System.out.print("Type your nickname: ");
                player = new Player(scanner.nextLine());
                registeredSuccessfully = game.registerPlayer(player.getNick(), player);
            }

            while (true) {
                System.out.println("Would you like to play with AI or another player? (ai/pl)");
                boolean playWithAI = scanner.nextLine().equals("ai");
                System.out.println("Starting game with: " + (playWithAI ? "AI" : "player"));

                if (playWithAI) {
                    game.startGameWithAI(player.getNick(), player);
                } else {
                    game.startGameWithPlayer(player.getNick());
                }
                synchronized (player) {
                    player.wait();
                }
                System.out.println("Play again? (y/n)");
                if (!scanner.nextLine().equals("y")) {
                    break;
                }
            }
            game.unregisterPlayer(player.getNick(), player);
            System.exit(0);

        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            LOGGER.error("Error: ", e);
        } catch (InterruptedException e) {
            LOGGER.error("Thread interrupted.", e);
        }
    }
}
