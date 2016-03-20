package pl.edu.agh.ds.tictactoe;

import javafx.util.Pair;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IGameListener extends Remote {

    Pair<Integer, Integer> onTakeTurn(Board board) throws RemoteException;

    void onWonGame() throws RemoteException;

    void onLostGame() throws RemoteException;

    void onDrawGame() throws RemoteException;

    void onWaitForOpponent() throws RemoteException;

    void onPrintBoard(Board board) throws RemoteException;

    void onPlayerRejected(String message) throws RemoteException;
}
