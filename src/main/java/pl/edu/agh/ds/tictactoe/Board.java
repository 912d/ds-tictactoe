package pl.edu.agh.ds.tictactoe;


import java.io.*;
import java.util.Arrays;

public class Board implements Serializable {

    public static final long serialVersionUID = 1234567890L;
    public static final int BOARD_SIZE = 3;
    private final BoardSquare[][] board;
    private BoardSquare winner = BoardSquare.EMPTY;


    public Board() {
        this.board = new BoardSquare[BOARD_SIZE][BOARD_SIZE];
        for (int y = 0; y < BOARD_SIZE; y++) {
            for (int x = 0; x < BOARD_SIZE; x++) {
                board[y][x] = BoardSquare.EMPTY;
            }
        }
    }

    public String printBoard() {
        StringBuilder builder = new StringBuilder();
        for (BoardSquare[] boardLine : board) {
            for (BoardSquare square : boardLine) {
                if (square == BoardSquare.EMPTY) {
                    builder.append('_');
                } else {
                    builder.append(square.name().toCharArray());
                }
            }
            builder.append('\n');
        }
        return builder.toString();
    }

    public boolean isFinished() {
        if (winner != BoardSquare.EMPTY) {
            return true;
        }
        for (BoardSquare[] boardLine : board) {
            for (BoardSquare square : boardLine) {
                if (squareEmpty(square)) {
                    return false;
                }
            }
        }
        return true;
    }

    public BoardSquare getWinner() {
        return winner;
    }

    /**
     * Coordinates {@code x}, {@code y} in human-friendly format ({@code 1 .. {@link Board#BOARD_SIZE}}).
     */
    public boolean applyMove(int x, int y, BoardSquare boardSquareType) {
        if (!moveAllowed(x, y)) {
            throw new IllegalArgumentException(String.format("Illegal move (%d, %d).", x, y));
        }
        x--;
        y--;
        board[y][x] = boardSquareType;
        return checkIfMoveWinning(x, y);
    }

    /**
     * Coordinates {@code x}, {@code y} in human-friendly format ({@code 1 .. {@link Board#BOARD_SIZE}}).
     */
    public boolean moveAllowed(int x, int y) {
        return indicesInBounds(x - 1, y - 1) && squareEmpty(board[y - 1][x - 1]);
    }

    /**
     * Indices {@code x}, {@code y} in array index format ({@code 0 .. {@link Board#BOARD_SIZE} - 1}).
     */
    private boolean indicesInBounds(int x, int y) {
        return x >= 0 && y >= 0 && x < BOARD_SIZE & y < BOARD_SIZE;
    }

    private boolean squareEmpty(BoardSquare boardSquare) {
        return boardSquare == BoardSquare.EMPTY;
    }

    /**
     * Indices {@code x}, {@code y} in array index format ({@code 0 .. {@link Board#BOARD_SIZE} - 1}).
     */
    private boolean checkIfMoveWinning(int x, int y) {
        BoardSquare lastMove = board[y][x];
        // check horizontal
        boolean won = true;
        for (BoardSquare square : board[y]) {
            if (square != lastMove) {
                won = false;
                break;
            }
        }
        if (won) {
            this.winner = board[y][x];
            return true;
        }
        // check vertical
        won = true;
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (board[i][x] != lastMove) {
                won = false;
                break;
            }
        }
        if (won) {
            this.winner = board[y][x];
            return true;
        }
        //check diagonals
        if ((x == 1 && y == 1) || (x != 1 && y != 1)) {
            won = true;
            for (int i = 0; i <BOARD_SIZE; i++) {
                if (board[i][i] != lastMove) {
                    won = false;
                    break;
                }
            }
            if (won) {
                this.winner = board[y][x];
                return true;
            }
            won = true;
            for (int i = 0; i < BOARD_SIZE; i++) {
                if (board[i][BOARD_SIZE - i - 1] != lastMove) {
                    won = false;
                    break;
                }
            }
        }
        return won;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Board board1 = (Board) o;

        if (!Arrays.deepEquals(board, board1.board)) return false;
        return winner == board1.winner;
    }

    @Override
    public int hashCode() {
        int result = Arrays.deepHashCode(board);
        result = 31 * result + winner.hashCode();
        return result;
    }
}
