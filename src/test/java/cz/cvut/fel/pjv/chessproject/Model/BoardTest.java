package cz.cvut.fel.pjv.chessproject.Model;

import cz.cvut.fel.pjv.chessproject.Model.Mocks.KingMock;
import cz.cvut.fel.pjv.chessproject.Pieces.*;
import cz.cvut.fel.pjv.chessproject.Players.Player;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Testing board properties and methods.
 */
public class BoardTest {

    public BoardTest() {
    }

    /**
     * Test of setPiece method, of class Board. Test places one new instance of
     * piece and then checks if its accessible from the outside.
     */
    @Test
    public void testSetPiece() {
        Piece piece = new King(true, 3, 3);
        Board tB = new Board();
        tB.setPiece(piece, 3, 3);
        assertEquals(piece, tB.getBox(3, 3).getCurrentPiece());
    }

    /**
     * Test of isOutOfBounds method, of class Board. Tests rules for staying in
     * board dimensions.
     */
    @Test
    public void testIsOutOfBounds() {
        Board tB = new Board();
        assertFalse(tB.isOutOfBounds(3, 5));
        assertFalse(tB.isOutOfBounds(0, 0));
        assertTrue(tB.isOutOfBounds(-6, -6));
    }

    /**
     * Test of willBeChecked method via Mock class representing kings method.
     * result1 is when king IS checked, result2 when he is not.
     */
    @Test
    public void testWillBeChecked() {
        Board tB = new Board(true);
        Piece KingMockW = new KingMock(true, 0, 0, true);
        Piece KingMockB = new KingMock(false, 7, 7, false);
        tB.setPiece(KingMockB, 7, 7);
        tB.setPiece(KingMockW, 0, 0);
        Game tG = new Game(false, tB);
        boolean result1 = tB.willBeChecked(new Move(0, 0, 1, 0), tG.getCurrentPlayer());
        tG.swapTurns();
        boolean result2 = tB.willBeChecked(new Move(7, 7, 6, 7), tG.getCurrentPlayer());
        assertTrue(result1);
        assertFalse(result2);
    }

    /**
     * Test of makeChange method, of class Board. Tests if everything is up to
     * date after board change.
     */
    @Test
    public void makeChange() {
        Game g = new Game(false, new Board());
        Piece moving = g.getBoard().getBox(0, 6).getCurrentPiece();
        //moving with Pawn
        g.createMoveFrom(0, 6);
        g.createMoveTo(0, 4);
        g.performGameCycle();
        Piece moved = g.getBoard().getBox(0, 4).getCurrentPiece();
        assertEquals(moving, moved);
        assertTrue(moving.isAlive());
        assertEquals(moving.getX(), moved.getX());
        assertEquals(moving.getY(), moved.getY());
        assertEquals(moving.IsWhite(), moved.IsWhite());
    }

    /**
     * Test of setPlayerPieces method, of class Board. Tests if all pieces on
     * the board will appear in each players list of pieces.
     */
    @Test
    public void testSetPlayerPieces() {
        Board customBoard = new Board(true);
        Player playerW = new Player(true, false);
        Player playerB = new Player(false, false);

        Piece KingW = new King(true, 0, 0);
        Piece QueenW = new Queen(true, 0, 0);

        Piece KingB = new King(false, 0, 0);
        Piece QueenB = new Queen(false, 0, 0);

        customBoard.setPiece(KingW, 0, 0);
        customBoard.setPiece(KingB, 7, 7);
        customBoard.setPiece(QueenW, 0, 1);
        customBoard.setPiece(QueenB, 7, 6);

        customBoard.setPlayerPieces(playerW, playerB);
        assertTrue(playerW.getMyPieces().contains(KingW) && playerW.getMyPieces().contains(QueenW));
        assertTrue(playerB.getMyPieces().contains(KingB) && playerB.getMyPieces().contains(QueenB));

    }

    /**
     * Test of promote method, of class Board. Tests if promoting pieces
     * actually swaps and null case.
     */
    @Test
    public void testPromote() {
        //setup
        Board testBoard = new Board();
        Game testGame = new Game(false, testBoard);

        //set new piece and check if its placed
        Piece promPiece = new Bishop(true, 0, 0);
        Player p = new Player(true, true);
        testBoard.promote(testGame.getBoard().getBox(0, 1).getCurrentPiece(), promPiece,p);
        assertEquals(testGame.getBoard().getBox(0, 1).getCurrentPiece(), promPiece);

    }

}
