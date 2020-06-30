package cz.cvut.fel.pjv.chessproject.Pieces;

import cz.cvut.fel.pjv.chessproject.Model.Board;
import cz.cvut.fel.pjv.chessproject.Enums.PieceTypes;
import java.util.ArrayList;

/**
 * Pawn class containing its method for possible moves generation.
 */
public class Pawn extends Piece {

    public Pawn(boolean isWhite, int x, int y) {
        super(isWhite, x, y);
        this.type = PieceTypes.PAWN;
        imageName = isWhite ? "PawnW.png" : "Pawn.png";

    }

    /* here I implemented it both for black n white in parallel loop via vectors
       vectors because it works equally for both sides */
    @Override
    public ArrayList<int[]> getPossibleMoves(Board board) {
        ArrayList<int[]> moves = new ArrayList<>();
        int[][] vecBlackMoves = {{0, 1}, {0, 2}}; // white move for 2 or 1
        int[][] vecWhiteMoves = {{0, -1}, {0, -2}};
        int[][] vecBlackAttack = {{-1, 1}, {1, 1}}; // white attacking
        int[][] vecWhiteAttack = {{-1, -1}, {1, -1}};
        int vectorNumber = 0; //counting for loop to stop if pawn is not on his first position

        //choosing the right directions
        int[][] vectors = vecWhiteMoves;

        if (!this.IsWhite()) {
            vectors = vecBlackMoves;
        }

        //just moving forwards..
        for (int[] vec : vectors) {
            int xTmp = x; //tmp so i can change it below
            int yTmp = y;

            if (!board.isOutOfBounds(xTmp + vec[0], yTmp + vec[1])) {

                //if we go for second (jumping) vector but he is not on start - end
                if (this.IsWhite() && vectorNumber == 1 && y != 6) {
                    break;
                } else if (!this.IsWhite() && vectorNumber == 1 && y != 1) {
                    break;
                }

                //no piece no problem
                if (board.getBox(xTmp + vec[0], yTmp + vec[1]).getCurrentPiece() == null) {
                    int[] move = {xTmp + vec[0], yTmp + vec[1]};
                    moves.add(move);
                } //this is finding my piece so this direction is busted
                //both my or enemy piece IN FRONT OF ME is a problem
                else {
                    break;
                }
                vectorNumber++;
            }
        }

        vectors = vecWhiteAttack;

        if (!this.IsWhite()) {
            vectors = vecBlackAttack;
        }

        //checking if can attack
        for (int[] vec : vectors) {
            int xTmp = x; //tmp so i can change it below
            int yTmp = y;

            if (!board.isOutOfBounds(xTmp + vec[0], yTmp + vec[1])) {

                //if there is a piece and i can capture it then its valid move
                if (board.getBox(xTmp + vec[0], yTmp + vec[1]).getCurrentPiece() != null) {
                    if (board.getBox(xTmp + vec[0], yTmp + vec[1]).getCurrentPiece().IsWhite() != this.IsWhite()) {
                        int[] move = {xTmp + vec[0], yTmp + vec[1]};
                        moves.add(move);
                    }
                }
            }

        }

        //skipping enPassant move ( first move )
        if (board.getLastMove() == null) {
            return moves;
        } else {

            //checking for en passant
            //vectors of both sides
            int[][] enVec = {{1, 0}, {-1, 0}};

            for (int[] vec : enVec) {
                int xTmp = x + vec[0];
                int yTmp = y + vec[1];
                Piece tmp = board.getBox(board.getLastMove().getTo_x(), board.getLastMove().getTo_y()).getCurrentPiece();
                if (!board.isOutOfBounds(xTmp, yTmp)) {
                    //this means piece next to me moved last round
                    if (board.getBox(xTmp, yTmp).getCurrentPiece() == tmp) {
                        if (tmp.getType() == PieceTypes.PAWN && tmp.IsWhite() != this.IsWhite()) {
                            int y1 = board.getLastMove().getFrom_y();
                            int y2 = board.getLastMove().getTo_y();
                            //this means he moved 2 (only first turn possible) 
                            if (Math.abs(y1 - y2) == 2) {
                                int[] move = {board.getLastMove().getFrom_x(), (y1 + y2) / 2};
                                moves.add(move);
                            }
                        }
                    }
                }

            }
        }

        return moves;
    }

}
