package cz.cvut.fel.pjv.chessproject.Model;

import cz.cvut.fel.pjv.chessproject.Enums.GameStatus;
import cz.cvut.fel.pjv.chessproject.Pieces.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for all public methods of the Game class.
 */
public class GameTest {

    public GameTest() {

    }

    /**
     * Tests if player tries to move with his piece or not.
     */
    @Test
    public void createMoveFrom() {
        Game game = new Game(false, new Board());
        assertFalse(game.createMoveFrom(8, 8));
        assertFalse(game.createMoveFrom(-4, -4));
        assertFalse(game.createMoveFrom(0, 3));
        assertFalse(game.createMoveFrom(0, 2));
    }

    /**
     * Tests if all wrong moves are denied.
     */
    @Test
    public void createMoveTo() {
        Game game = new Game(false, new Board());
        game.createMoveFrom(0, 6);
        assertFalse(game.createMoveTo(0, 7));
        assertFalse(game.createMoveTo(0, 6));
        assertFalse(game.createMoveTo(-2, -2));

    }

    /**
     * Tests methods capability of deciding if move is possible by creating
     * scenarios which do or does not lead to legit move possibility.
     */
    @Test
    public void createAImove() {
        Game game = new Game(true, new Board());
        game.swapTurns();
        assertTrue(game.createAImove());

        //scenario when AI is in check but have one possibility of move
        Board cB = new Board(true);
        Piece RookW = new Rook(true, 0, 0);
        Piece RookW2 = new Rook(true, 0, 0);
        Piece KingW = new King(true, 0, 0);
        Piece PawnW = new Pawn(true, 0, 0);
        Piece KingB = new King(false, 0, 0);
        cB.setPiece(KingW, 7, 7);
        cB.setPiece(RookW, 7, 0);
        cB.setPiece(RookW2, 2, 7);
        cB.setPiece(PawnW, 0, 2);
        cB.setPiece(KingB, 1, 0);
        game = new Game(true, cB);
        game.swapTurns();
        assertTrue(game.createAImove());

        //scenarion when AI is in checkmate hence cannot move
        cB = new Board(true);
        RookW = new Rook(true, 0, 0);
        RookW2 = new Rook(true, 0, 0);
        KingW = new King(true, 0, 0);
        PawnW = new Pawn(true, 0, 0);
        KingB = new King(false, 0, 0);
        Piece BishopW = new Bishop(true, 0, 0);
        cB.setPiece(KingW, 7, 7);
        cB.setPiece(RookW, 7, 0);
        cB.setPiece(RookW2, 2, 7);
        cB.setPiece(PawnW, 0, 2);
        cB.setPiece(BishopW, 3, 4);
        cB.setPiece(KingB, 1, 0);
        game = new Game(true, cB);
        game.swapTurns();
        assertFalse(game.createAImove());

    }

    /**
     * tests correct status evaluating with prepared scenarios. One is leading
     * to stalemate the other to checkmate.
     */
    @Test
    public void performGameCycle() {
        //stalemate detection
        Board cB = new Board(true);
        Piece QueenW = new Queen(true, 0, 0);
        Piece KingW = new King(true, 0, 0);
        Piece KingB = new King(false, 0, 0);
        cB.setPiece(KingB, 7, 7);
        cB.setPiece(KingW, 5, 6);
        cB.setPiece(QueenW, 2, 5);
        Game game = new Game(true, cB);
        game.createMoveFrom(2, 5);
        game.createMoveTo(6, 5);
        GameStatus result = game.performGameCycle();
        GameStatus expResult = GameStatus.STALEMATE;
        assertEquals(expResult, result);

        //checkmate detection
        cB = new Board(true);
        QueenW = new Queen(true, 0, 0);
        KingW = new King(true, 0, 0);
        KingB = new King(false, 0, 0);
        cB.setPiece(KingB, 7, 7);
        cB.setPiece(KingW, 5, 6);
        cB.setPiece(QueenW, 2, 5);
        game = new Game(true, cB);
        game.createMoveFrom(2, 5);
        game.createMoveTo(6, 7);
        result = game.performGameCycle();
        expResult = GameStatus.CHECKMATE;
        assertEquals(expResult, result);

    }

}
