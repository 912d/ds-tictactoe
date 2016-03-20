package pl.edu.agh.ds.tictactoe;

import javafx.util.Pair;

import java.rmi.RemoteException;

public class AIGameListener implements IGameListener {

    @Override
    public Pair<Integer, Integer> onTakeTurn(Board board) throws RemoteException {
        for (int i = 1; i <= Board.BOARD_SIZE; i++) {
            for (int j = 1; j <= Board.BOARD_SIZE; j++) {
                if (board.moveAllowed(i, j)) {
                    return new Pair<>(i, j);
                }
            }
        }
        return new Pair<>(-1, -1);
    }

    @Override
    public void onWonGame() throws RemoteException {
    }

    @Override
    public void onLostGame() throws RemoteException {
    }

    @Override
    public void onDrawGame() throws RemoteException {
    }

    @Override
    public void onWaitForOpponent() throws RemoteException {
    }

    @Override
    public void onPrintBoard(Board board) throws RemoteException {
    }

    @Override
    public void onPlayerRejected(String message) throws RemoteException {
    }
}
