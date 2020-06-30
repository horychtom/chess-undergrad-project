package cz.cvut.fel.pjv.chessproject.Pieces;

import cz.cvut.fel.pjv.chessproject.Model.Board;
import cz.cvut.fel.pjv.chessproject.Enums.PieceTypes;
import java.util.ArrayList;

/**
 * Knight class containing its method for possible moves generation.
 */
public class Knight extends Piece {

    public Knight(boolean isWhite, int x, int y) {
        super(isWhite, x, y);
        this.type = PieceTypes.KNIGHT;
        imageName = isWhite ? "KnightW.png": "Knight.png";
    }

    @Override
    public ArrayList<int[]> getPossibleMoves(Board board) {
        ArrayList<int[]> moves = new ArrayList<>();
        int[][] vectors = {{2, 1}, {2, -1}, {-2, 1}, {-2, -1}, {1, 2}, {1, -2}, {-1, 2}, {-1, -2}};

        //going in all directions direction
        for (int[] vec : vectors) {
            int xTmp = x; //tmp so i can change it below
            int yTmp = y;

            //not going out of board
            if (!board.isOutOfBounds(xTmp + vec[0], yTmp + vec[1])) {
                //no piece no problem
                if (board.getBox(xTmp + vec[0], yTmp + vec[1]).getCurrentPiece() == null) {
                    int[] move = {xTmp + vec[0], yTmp + vec[1]};
                    moves.add(move);
                } //here capturing NOT my piece
                else if (board.getBox(xTmp + vec[0], yTmp + vec[1]).getCurrentPiece().IsWhite() != this.IsWhite()) {
                    int[] move = {xTmp + vec[0], yTmp + vec[1]};
                    moves.add(move);
                }

            }

        }

        return moves;

    }

}
