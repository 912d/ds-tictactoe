package pl.edu.agh.ds.tictactoe;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IGame extends Remote {

    boolean startGameWithPlayer(String player, IGameListener listener) throws RemoteException;
}
