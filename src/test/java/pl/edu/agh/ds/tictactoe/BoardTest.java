package pl.edu.agh.ds.tictactoe;


import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class BoardTest {

    @Test
    public void testCheckDiagonalWin() throws Exception {
        Board board = new Board();
        boolean won = board.applyMove(1, 1, BoardSquare.O);
        assertThat(won).isFalse();
        won = board.applyMove(2, 2, BoardSquare.O);
        assertThat(won).isFalse();
        won = board.applyMove(3, 3, BoardSquare.O);
        assertThat(won).isTrue();
    }

    @Test
    public void testCheckDiagonalWin2() throws Exception {
        Board board = new Board();
        boolean won = board.applyMove(2, 2, BoardSquare.O);
        assertThat(won).isFalse();
        won = board.applyMove(1, 3, BoardSquare.O);
        assertThat(won).isFalse();
        won = board.applyMove(3, 1, BoardSquare.O);
        assertThat(won).isTrue();
    }

    @Test
    public void testCheckDiagonalWin3() throws Exception {
        Board board = new Board();
        boolean won;
        won = board.applyMove(1, 3, BoardSquare.O);
        assertThat(won).isFalse();
        won = board.applyMove(2, 2, BoardSquare.O);
        assertThat(won).isFalse();
        won = board.applyMove(3, 1, BoardSquare.O);
        assertThat(won).isTrue();
    }

    @Test
    public void testCheckDiagonalWin4() throws Exception {
        Board board = new Board();
        boolean won;
        won = board.applyMove(1, 3, BoardSquare.O);
        assertThat(won).isFalse();
        won = board.applyMove(3, 1, BoardSquare.O);
        assertThat(won).isFalse();
        won = board.applyMove(2, 2, BoardSquare.O);
        assertThat(won).isTrue();
    }

    @Test
    public void testCheckVerticalWin() throws Exception {
        Board board = new Board();
        boolean won = board.applyMove(1, 1, BoardSquare.O);
        assertThat(won).isFalse();
        won = board.applyMove(1, 2, BoardSquare.O);
        assertThat(won).isFalse();
        won = board.applyMove(1, 3, BoardSquare.O);
        assertThat(won).isTrue();
    }

    @Test
    public void testCheckVerticalWin2() throws Exception {
        Board board = new Board();
        boolean won = board.applyMove(2, 3, BoardSquare.O);
        assertThat(won).isFalse();
        won = board.applyMove(2, 1, BoardSquare.O);
        assertThat(won).isFalse();
        won = board.applyMove(2, 2, BoardSquare.O);
        assertThat(won).isTrue();
    }

    @Test
    public void testCheckVerticalWin3() throws Exception {
        Board board = new Board();
        boolean won = board.applyMove(3, 1, BoardSquare.O);
        assertThat(won).isFalse();
        won = board.applyMove(3, 3, BoardSquare.O);
        assertThat(won).isFalse();
        won = board.applyMove(3, 2, BoardSquare.O);
        assertThat(won).isTrue();
    }

    @Test
    public void testCheckHorizontalWin() throws Exception {
        Board board = new Board();
        boolean won = board.applyMove(1, 1, BoardSquare.O);
        assertThat(won).isFalse();
        won = board.applyMove(2, 1, BoardSquare.O);
        assertThat(won).isFalse();
        won = board.applyMove(3, 1, BoardSquare.O);
        assertThat(won).isTrue();
    }

    @Test
    public void testCheckHorizontalWin2() throws Exception {
        Board board = new Board();
        boolean won = board.applyMove(1, 2, BoardSquare.O);
        assertThat(won).isFalse();
        won = board.applyMove(2, 2, BoardSquare.O);
        assertThat(won).isFalse();
        won = board.applyMove(3, 2, BoardSquare.O);
        assertThat(won).isTrue();
    }

    @Test
    public void testCheckHorizontalWin3() throws Exception {
        Board board = new Board();
        boolean won = board.applyMove(1, 3, BoardSquare.O);
        assertThat(won).isFalse();
        won = board.applyMove(3, 3, BoardSquare.O);
        assertThat(won).isFalse();
        won = board.applyMove(2, 3, BoardSquare.O);
        assertThat(won).isTrue();
    }
}