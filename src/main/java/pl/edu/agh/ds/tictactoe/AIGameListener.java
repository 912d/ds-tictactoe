package pl.edu.agh.ds.tictactoe;


import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AIGameListener implements IGameListener {

    @Override
    public Pair<Integer, Integer> onTakeTurn(Board board) throws RemoteException {
        List<Pair<Integer, Integer>> availableMoves = new ArrayList<>();

        for (int i = 1; i <= Board.BOARD_SIZE; i++) {
            for (int j = 1; j <= Board.BOARD_SIZE; j++) {
                if (board.moveAllowed(i, j)) {
                    availableMoves.add(new Pair<>(i, j));
                }
            }
        }
        return availableMoves.get(new Random(System.currentTimeMillis()).nextInt(availableMoves.size()));
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
