package cz.cvut.fel.pjv.chessproject.Pieces;

import cz.cvut.fel.pjv.chessproject.Model.Board;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Queen moves test.
 */
public class QueenTest {

    public QueenTest() {
    }

    /**
     * Test of getPossibleMoves method, of class Queen.
     */
    @Test
    public void testGetPossibleMoves() {
        //symmetric test

        Board b = new Board();

        //testBlack
        List<int[]> list = b.getBox(3, 0).getCurrentPiece().getPossibleMoves(b);
        assertTrue(list.isEmpty());

        //testWhite
        list = b.getBox(3, 7).getCurrentPiece().getPossibleMoves(b);
        assertTrue(list.isEmpty());

    }

}
