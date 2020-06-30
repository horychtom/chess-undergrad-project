package cz.cvut.fel.pjv.chessproject.Model;

import cz.cvut.fel.pjv.chessproject.Pieces.Piece;

/**
 * Class for storing basic information about specific place on board
 */
public class Box {

    private final int position_x;
    private final int position_y;
    private Piece currentPiece = null;

    public Box(int position_x, int position_y) {
        this.position_x = position_x;
        this.position_y = position_y;
    }

    public int getPosition_x() {
        return position_x;
    }

    public int getPosition_y() {
        return position_y;
    }

    public Piece getCurrentPiece() {
        return currentPiece;
    }

    public void setCurrentPiece(Piece currentPiece) {
        this.currentPiece = currentPiece;
    }

}
