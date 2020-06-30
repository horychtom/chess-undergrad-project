package cz.cvut.fel.pjv.chessproject.Pieces;

import cz.cvut.fel.pjv.chessproject.Model.Board;
import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Pawn moves test.
 */
public class PawnTest {

    public PawnTest() {
    }

    /**
     * Test of getPossibleMoves method, of class Pawn.
     */
    @Test
    public void testGetPossibleMoves() {
        //symmetric test

        Board b = new Board();

        //testBlack
        List<int[]> list = b.getBox(0, 1).getCurrentPiece().getPossibleMoves(b);
        assertFalse(list.isEmpty());

        //testWhite
        list = b.getBox(0, 6).getCurrentPiece().getPossibleMoves(b);
        assertFalse(list.isEmpty());

    }

}
