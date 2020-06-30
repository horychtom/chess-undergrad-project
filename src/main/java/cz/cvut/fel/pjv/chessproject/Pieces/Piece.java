package cz.cvut.fel.pjv.chessproject.Pieces;

import cz.cvut.fel.pjv.chessproject.Model.Board;
import cz.cvut.fel.pjv.chessproject.Enums.PieceTypes;
import java.util.ArrayList;

/**
 * Class to represent shared properties of all pieces.
 */
public abstract class Piece {

    protected String imageName;
    private final boolean isWhite;
    protected boolean isAlive = true;
    protected int x; //every piece saves its current position, it has to be re-set when moving
    protected int y;
    protected PieceTypes type;
    protected boolean didNotMove;

    public Piece(boolean isWhite, int x, int y) {
        this.isWhite = isWhite;
        this.x = x;
        this.y = y;
        this.didNotMove = true;
    }

    public boolean didNotMove() {
        return didNotMove;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public boolean IsWhite() {
        return isWhite;
    }

    public boolean isThreatened(Board board) {
        return false;
    }

    public void setDidNotMove(boolean didNotMove) {
        this.didNotMove = didNotMove;
    }

    public void setIsAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getImageName() {
        return imageName;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public PieceTypes getType() {
        return type;
    }

    /**
     * Iterates through all possible directions via vector adding and seeks
     * possible moves. If found - adds to list.
     *
     * @param board instance of board used to determine possible moves on
     * @return list of available moves
     */
    public ArrayList<int[]> getPossibleMoves(Board board) {
        ArrayList<int[]> moves = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                int[] move = {i, j};
                moves.add(move);

            }
        }

        return moves;
    }

}
