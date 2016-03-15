package pl.edu.agh.ds.tictactoe.noteboard;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface INoteBoard extends Remote {
    String getText() throws RemoteException;

    void appendText(String newNote) throws RemoteException;

    void clean() throws RemoteException;
}
