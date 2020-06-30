package cz.cvut.fel.pjv.chessproject.Pieces;

import cz.cvut.fel.pjv.chessproject.Model.Board;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Bishop moves test.
 */
public class BishopTest {

    public BishopTest() {
    }

    /**
     * Test of getPossibleMoves method, of class Bishop.
     */
    @Test
    public void testGetPossibleMoves() {
        //symmetric test

        Board b = new Board();

        //testBlack
        List<int[]> list = b.getBox(2, 0).getCurrentPiece().getPossibleMoves(b);
        assertTrue(list.isEmpty());

        //testWhite
        list = b.getBox(2, 7).getCurrentPiece().getPossibleMoves(b);
        assertTrue(list.isEmpty());
    }

}
