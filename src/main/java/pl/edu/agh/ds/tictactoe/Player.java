package pl.edu.agh.ds.tictactoe;

import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Player extends UnicastRemoteObject implements IPlayer, IGameListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(Player.class);

    private final String nick;
    private BufferedReader in;
    private Writer out;


    public Player(String nick, Reader in, Writer out) throws RemoteException {
        super();
        this.nick = nick;
        this.in = new BufferedReader(in);
        this.out = new BufferedWriter(out);
    }

    @Override
    public String getNick() {
        return this.nick;
    }

    // IGameListener methods
    @Override
    public synchronized Pair<Integer, Integer> onTakeTurn(Board board) throws IOException {
        board.printBoard(out);
        out.write("Your turn!");
        try {
            int x = -1;
            int y = -1;
            boolean moveOk = false;
            while (!moveOk) {
                String[] move = in.readLine().split(" ");
                if (move.length >= 2) {
                    x = Integer.parseInt(move[0]);
                    y = Integer.parseInt(move[1]);
                    moveOk = board.moveAllowed(x, y);
                    out.write("This move is not allowed.");
                    out.write(String.format("Please specify correct coordinates between %d and %d", 1, Board.BOARD_SIZE));
                } else {
                    out.write(String.format("Please specify two integer numbers between %d and %d", 1, Board.BOARD_SIZE));
                    out.write("separated with a whitespace.");
                    moveOk = false;
                }
            }
            return new Pair<>(x, y);
        } catch (IOException e) {
            LOGGER.error("General IO error: ", e);
        }
        return null;
    }

    @Override
    public void onWonGame() throws IOException {
        out.write("CONGRATS!!! YOU WON!!!");
        exitGame();
    }

    @Override
    public void onLostGame() throws IOException {
        out.write("Sorry, you lost!");
        exitGame();
    }

    @Override
    public void onDrawGame() throws IOException {
        out.write("It's a draw!");
        exitGame();
    }

    @Override
    public void onWaitForOpponent() throws IOException {
        out.write("Waiting for opponent...");
    }

    private void exitGame() {
        try {
            UnicastRemoteObject.unexportObject(this, true);
        } catch (NoSuchObjectException e) {
            LOGGER.error("Error while unexporting remote object.", e);
        }
    }
}
