package pl.edu.agh.ds.tictactoe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class GameServer implements IGameServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameServer.class);

    private String waitingPlayer;
    private Map<String, IGameListener> players = new HashMap<>();

    @Override
    public synchronized boolean registerPlayer(String nick, IGameListener listener) throws RemoteException {
        IGameListener remoteListener = players.get(nick);
        if (remoteListener == null) {
            addPlayer(nick, listener);
            return true;
        } else if (remoteListener.equals(listener)) {
            LOGGER.info("Attempt to registerPlayer already registered player.", nick);
            listener.onPlayerRejected("Player already registered.");
        } else {
            LOGGER.info("Attempt to registerPlayer already registered player.", nick);
            listener.onPlayerRejected("This nick is already taken.");
        }
        return false;
    }

    @Override
    public void startGameWithPlayer(String nick) throws RemoteException {
        if (isPlayerRegistered(nick)) {
            IGameListener listener = players.get(nick);
            if (nick.equals(waitingPlayer)) {
                LOGGER.info(String.format("Player already registered and waiting: %s", nick));
                listener.onPlayerRejected("Player exists.");
            }
            if (waitingPlayer != null) {
                LOGGER.info("Starting game for: " + nick + " and " + waitingPlayer);
                startGameInNewThread(waitingPlayer, nick, new Board());
                waitingPlayer = null;
            } else {
                this.waitingPlayer = nick;
                LOGGER.info("Player waiting: " + nick);
                listener.onWaitForOpponent();
            }
        }
    }

    @Override
    public void startGameWithAI(String nick, IGameListener listener) throws RemoteException {
        if (isPlayerRegistered(nick)) {
            startGameWithAIInNewThread(nick, new Board());
        }
    }

    @Override
    public void unregisterPlayer(String nick, IGameListener listener) throws RemoteException {
        IGameListener remoteListener = players.get(nick);
        if (remoteListener != null && remoteListener.equals(listener)) {
            players.remove(nick);
        } else {
            LOGGER.info("Attempt to unregister not registered player {}", nick);
        }
    }

    private void startGameWithAIInNewThread(String nick, Board board) {
        new Thread(new GameRunner(board, nick, players.get(nick), "AI_John", new AIGameListener())).start();
    }

    private void startGameInNewThread(String nick1, String nick2, Board board) {
        new Thread(new GameRunner(board, nick1, players.get(nick1), nick2, players.get(nick2))).start();
    }

    private void addPlayer(String nick, IGameListener listener) {
        players.put(nick, listener);
        LOGGER.info("Player {} registered.", nick);
    }

    private boolean isPlayerRegistered(String nick) {
        return players.containsKey(nick);
    }
}
