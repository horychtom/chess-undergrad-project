package cz.cvut.fel.pjv.chessproject.Pieces;

import cz.cvut.fel.pjv.chessproject.Model.Board;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Knight moves test.
 */
public class KnightTest {

    public KnightTest() {
    }

    /**
     * Test of getPossibleMoves method, of class Knight.
     */
    @Test
    public void testGetPossibleMoves() {
        //symmetric test

        Board b = new Board();

        //testBlack
        List<int[]> list = b.getBox(1, 0).getCurrentPiece().getPossibleMoves(b);
        assertFalse(list.isEmpty());

        //testWhite
        list = b.getBox(1, 7).getCurrentPiece().getPossibleMoves(b);
        assertFalse(list.isEmpty());

    }

}
