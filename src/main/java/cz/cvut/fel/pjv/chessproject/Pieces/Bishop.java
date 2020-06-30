package cz.cvut.fel.pjv.chessproject.Pieces;

import cz.cvut.fel.pjv.chessproject.Model.Board;
import cz.cvut.fel.pjv.chessproject.Enums.PieceTypes;
import java.util.ArrayList;

/**
 * Bishop class containing its method for possible moves.
 */
public class Bishop extends Piece {

    public Bishop(boolean isWhite, int x, int y) {
        super(isWhite, x, y);
        this.type = PieceTypes.BISHOP;
        imageName = isWhite ? "bishopW.png" : "bishop.png";
    }

    @Override
    public ArrayList<int[]> getPossibleMoves(Board board) {
        ArrayList<int[]> moves = new ArrayList<>();
        int[][] vectors = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

        //going in all directions
        for (int[] vec : vectors) {

            int xTmp = x; //tmp so i can change it below
            int yTmp = y;

            //not going out of board
            while (!board.isOutOfBounds(xTmp + vec[0], yTmp + vec[1])) {

                //no piece no problem
                if (board.getBox(xTmp + vec[0], yTmp + vec[1]).getCurrentPiece() == null) {
                    int[] move = {xTmp + vec[0], yTmp + vec[1]};
                    moves.add(move);
                    xTmp = xTmp + vec[0];
                    yTmp = yTmp + vec[1];
                } //here i found my piece so this direction is busted
                else if (board.getBox(xTmp + vec[0], yTmp + vec[1]).getCurrentPiece().IsWhite() == this.IsWhite()) {
                    break;
                } //this is finding opponents piece and capturing it
                else {
                    int[] move = {xTmp + vec[0], yTmp + vec[1]};
                    moves.add(move);
                    break;
                }
            }

        }

        return moves;

    }

}
