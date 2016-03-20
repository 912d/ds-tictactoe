package pl.edu.agh.ds.tictactoe;

import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class Player extends UnicastRemoteObject implements IPlayer, IGameListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(Player.class);

    private final String nick;
    private Scanner in;

    public Player(String nick) throws RemoteException {
        super();
        this.nick = nick;
        this.in = new Scanner(System.in);
    }

    @Override
    public String getNick() {
        return this.nick;
    }

    // IGameListener methods
    @Override
    public synchronized Pair<Integer, Integer> onTakeTurn(Board board) throws RemoteException {
        System.out.println(board.printBoard());
        System.out.println("Your turn!");
        int x = -1;
        int y = -1;
        boolean moveOk = false;
        while (!moveOk) {
            String[] move = in.nextLine().split(" ");
            if (move.length >= 2) {
                x = Integer.parseInt(move[0]);
                y = Integer.parseInt(move[1]);
                moveOk = board.moveAllowed(x, y);
                if (!moveOk) {
                    System.out.println("This move is not allowed.");
                    System.out.println(String.format("Please specify correct coordinates between %d and %d", 1, Board.BOARD_SIZE));
                }
            } else {
                System.out.println(String.format("Please specify two integer numbers between %d and %d", 1, Board.BOARD_SIZE));
                System.out.println("separated with a whitespace.");
                moveOk = false;
            }
        }
        return new Pair<>(x, y);
    }

    @Override
    public void onWonGame() throws RemoteException {
        System.out.println("CONGRATS!!! YOU WON!!!");
        synchronized (this) {
            this.notify();
        }
    }

    @Override
    public void onLostGame() throws RemoteException {
        System.out.println("Sorry, you lost!");
        synchronized (this) {
            this.notify();
        }
    }

    @Override
    public void onDrawGame() throws RemoteException {
        System.out.println("It's a draw!");
        synchronized (this) {
            this.notify();
        }
    }

    @Override
    public void onWaitForOpponent() throws RemoteException {
        System.out.println("Waiting for opponent...");
    }

    @Override
    public void onPrintBoard(Board board) throws RemoteException {
        System.out.println(board.printBoard());
    }

    @Override
    public void onPlayerRejected(String message) throws RemoteException {
        System.out.println(String.format("Player rejected: %s", message));
    }

    private void unexportPlayer() {
        try {
            UnicastRemoteObject.unexportObject(this, true);
        } catch (NoSuchObjectException e) {
            LOGGER.error("Error while unexporting remote object.", e);
        }
    }
}
