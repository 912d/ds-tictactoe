package pl.edu.agh.ds.tictactoe;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IGameServer extends Remote {

    void startGameWithPlayer(String nick) throws RemoteException;

    boolean registerPlayer(String nick, IGameListener listener) throws RemoteException;

    void startGameWithAI(String nick, IGameListener listener) throws RemoteException;

    void unregisterPlayer(String nick, IGameListener listener) throws RemoteException;
}
