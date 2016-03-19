package pl.edu.agh.ds.tictactoe;

import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Game implements IGame {
    private static final Logger LOGGER = LoggerFactory.getLogger(Game.class);

    private String waitingPlayer;
    private Map<String, IGameListener> players = new HashMap<>();

    @Override
    public void startGameWithPlayer(String player, IGameListener listener) throws IOException {
        registerPlayer(player, listener);

        if (waitingPlayer != null) {
            Board board = new Board();
            startGame(board, player, waitingPlayer);
            LOGGER.info("Starting game for: " + player + " and " + waitingPlayer);
        } else {
            this.waitingPlayer = player;
            listener.onWaitForOpponent();
            LOGGER.info("Player waiting: " + player);
        }
    }

    private void startGame(Board board, String player1, String player2) throws IOException {

        while (!board.isFinished()) {
            getAndApplyPlayersMove(board, player1, BoardSquare.O);
            if (!board.isFinished()) {
                getAndApplyPlayersMove(board, player2, BoardSquare.X);
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

    private void getAndApplyPlayersMove(Board board, String player, BoardSquare boardSquare) throws IOException {
        Pair<Integer, Integer> move = players.get(player).onTakeTurn(board);
        LOGGER.info("Player {} move: {} {}", player, move.getKey(), move.getValue());
        board.applyMove(move.getKey(), move.getValue(), boardSquare);
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
