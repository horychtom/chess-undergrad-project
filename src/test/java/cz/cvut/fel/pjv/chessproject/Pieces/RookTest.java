package cz.cvut.fel.pjv.chessproject.Pieces;

import cz.cvut.fel.pjv.chessproject.Model.Board;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Rook moves test.
 */
public class RookTest {

    public RookTest() {
    }

    /**
     * Test of getPossibleMoves method, of class Rook.
     */
    @Test
    public void testGetPossibleMoves() {
        //symmetric test

        Board b = new Board();

        //testBlack
        List<int[]> list = b.getBox(0, 0).getCurrentPiece().getPossibleMoves(b);
        assertTrue(list.isEmpty());

        //testWhite
        list = b.getBox(0, 7).getCurrentPiece().getPossibleMoves(b);
        assertTrue(list.isEmpty());

    }

}
