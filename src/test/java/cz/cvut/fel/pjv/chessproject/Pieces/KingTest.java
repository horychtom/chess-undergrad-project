package cz.cvut.fel.pjv.chessproject.Pieces;

import cz.cvut.fel.pjv.chessproject.Model.Board;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * King moves and check test.
 */
public class KingTest {

    public KingTest() {
    }

    /**
     * Test of getPossibleMoves method, of class King.
     */
    @Test
    public void testGetPossibleMoves() {
        //symmetric test

        Board b = new Board();

        //testBlack
        List<int[]> list = b.getBox(4, 0).getCurrentPiece().getPossibleMoves(b);
        assertTrue(list.isEmpty());

        //testWhite
        list = b.getBox(4, 7).getCurrentPiece().getPossibleMoves(b);
        assertTrue(list.isEmpty());

    }

    /**
     * Test of isThreatened method, of class King.
     */
    @Test
    public void testIsThreatened() {

    }

}
