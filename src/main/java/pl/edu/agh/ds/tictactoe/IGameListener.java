package pl.edu.agh.ds.tictactoe;

import javafx.util.Pair;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IGameListener extends Remote {

    Pair<Integer, Integer> onTakeTurn(Board board) throws IOException;

    void onWonGame() throws IOException;

    void onLostGame() throws RemoteException, IOException;

    void onDrawGame() throws IOException;

    void onWaitForOpponent() throws IOException;
}
