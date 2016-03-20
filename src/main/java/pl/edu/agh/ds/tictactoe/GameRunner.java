package pl.edu.agh.ds.tictactoe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;

class GameRunner implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameRunner.class);
    private Board board;
    private String player1;
    private IGameListener listener1;
    private String player2;
    private IGameListener listener2;

    GameRunner(Board board, String player1, IGameListener listener1, String player2, IGameListener listener2) {
        this.board = board;
        this.player1 = player1;
        this.listener1 = listener1;
        this.player2 = player2;
        this.listener2 = listener2;
    }

    @Override
    public void run() {
        try {
            startGame(board, player1, listener1, player2, listener2);
        } catch (RemoteException e) {
            LOGGER.error("Error: ", e);
        }
    }

    private void startGame(Board board, String player1, IGameListener listener1, String player2, IGameListener listener2) throws RemoteException {

        while (!board.isFinished()) {
            Pair<Integer, Integer> move;

            move = getPlayersMove(board, player1, listener1);
            board.applyMove(move.getKey(), move.getValue(), BoardSquare.O);
            listener1.onPrintBoard(board);
            if (!board.isFinished()) {
                move = getPlayersMove(board, player2, listener2);
                board.applyMove(move.getKey(), move.getValue(), BoardSquare.X);
                listener2.onPrintBoard(board);
            }
        }
        BoardSquare winner = board.getWinner();
        if (winner == BoardSquare.O) {
            listener2.onPrintBoard(board);
            listener1.onWonGame();
            listener2.onLostGame();
            LOGGER.info("GameServer finished: {} won.", player1);
        } else if (winner == BoardSquare.X) {
            listener1.onPrintBoard(board);
            listener2.onWonGame();
            listener1.onLostGame();
            LOGGER.info("GameServer finished: {} won.", player2);
        } else {
            listener2.onPrintBoard(board);
            listener2.onDrawGame();
            listener1.onDrawGame();
            LOGGER.info("GameServer finished: draw.");
        }
    }

    private Pair<Integer, Integer> getPlayersMove(Board board, String player, IGameListener listener) throws RemoteException {
        Pair<Integer, Integer> move = listener.onTakeTurn(board);
        LOGGER.info("Player {} move: {} {}", player, move.getKey(), move.getValue());
        return move;
    }
}
