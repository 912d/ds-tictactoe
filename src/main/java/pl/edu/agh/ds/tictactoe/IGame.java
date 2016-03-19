package pl.edu.agh.ds.tictactoe;

import java.io.IOException;
import java.rmi.Remote;

public interface IGame extends Remote {

    void startGameWithPlayer(String player, IGameListener listener) throws IOException;
}
