package pl.edu.agh.ds.tictactoe;

import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public class Game implements IGame {
    private static final Logger LOGGER = LoggerFactory.getLogger(Game.class);

    private String waitingPlayer;
    private Map<String, IGameListener> players = new HashMap<>();

    @Override
    public void startGameWithPlayer(String player, IGameListener listener) throws RemoteException {
        registerPlayer(player, listener);

        if (player.equals(waitingPlayer)) {
            LOGGER.info(String.format("Player already registered and waiting: %s", player));
            listener.onPlayerRejected("Player exists.");
        }
        if (waitingPlayer != null) {
            Board board = new Board();
            LOGGER.info("Starting game for: " + player + " and " + waitingPlayer);
            startGame(board, waitingPlayer, player);
        } else {
            this.waitingPlayer = player;
            LOGGER.info("Player waiting: " + player);
            listener.onWaitForOpponent();
        }
    }

    private void startGame(Board board, String player1, String player2) throws RemoteException {

        while (!board.isFinished()) {
            Pair<Integer, Integer> move;

            move = getPlayersMove(board, player1);
            board.applyMove(move.getKey(), move.getValue(), BoardSquare.O);
            players.get(player1).onPrintBoard(board);
            if (!board.isFinished()) {
                move = getPlayersMove(board, player2);
                board.applyMove(move.getKey(), move.getValue(), BoardSquare.X);
                players.get(player2).onPrintBoard(board);
            }
        }
        BoardSquare winner = board.getWinner();
        if (winner == BoardSquare.O) {
            players.get(player1).onWonGame();
            players.get(player2).onLostGame();
            LOGGER.info("Game finished: {} won.", player1);
        } else if (winner == BoardSquare.X) {
            players.get(player2).onWonGame();
            players.get(player1).onLostGame();
            LOGGER.info("Game finished: {} won.", player2);
        } else {
            players.get(player2).onDrawGame();
            players.get(player1).onDrawGame();
            LOGGER.info("Game finished: draw.");
        }
        unregister(player1);
        unregister(player2);
    }

    private Pair<Integer, Integer> getPlayersMove(Board board, String player) throws RemoteException {
        Pair<Integer, Integer> move = players.get(player).onTakeTurn(board);
        LOGGER.info("Player {} move: {} {}", player, move.getKey(), move.getValue());
        return move;
    }

    private void unregister(String player) {
        this.players.remove(player);
        LOGGER.info("Player {} unregistered.", player);
    }

    private void registerPlayer(String player, IGameListener listener) {
        this.players.put(player, listener);
        LOGGER.info("Player {} registered.", player);
    }
}
