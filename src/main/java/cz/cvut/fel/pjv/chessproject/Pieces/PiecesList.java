package cz.cvut.fel.pjv.chessproject.Pieces;

import java.util.ArrayList;
import java.util.List;

/**
 * data class for providing simple list of all possible black and white pieces.
 * Main purpose is in custom board creation but can be useful for variety of
 * features.
 */
public class PiecesList {

    private List<Piece> Pieces;

    public PiecesList() {
        Pieces = new ArrayList<>();
        //whites
        for (int i = 0; i < 8; i++) {
            Pieces.add(new Pawn(true, 0, 0));
        }
        Pieces.add(new King(true, 0, 0));
        Pieces.add(new Queen(true, 0, 0));
        Pieces.add(new Rook(true, 0, 0));
        Pieces.add(new Rook(true, 0, 0));
        Pieces.add(new Knight(true, 0, 0));
        Pieces.add(new Knight(true, 0, 0));
        Pieces.add(new Bishop(true, 0, 0));
        Pieces.add(new Bishop(true, 0, 0));
        
        //blacks
        for (int i = 0; i < 8; i++) {
            Pieces.add(new Pawn(false, 0, 0));
        }
        Pieces.add(new King(false, 0, 0));
        Pieces.add(new Queen(false, 0, 0));
        Pieces.add(new Rook(false, 0, 0));
        Pieces.add(new Rook(false, 0, 0));
        Pieces.add(new Knight(false, 0, 0));
        Pieces.add(new Knight(false, 0, 0));
        Pieces.add(new Bishop(false, 0, 0));
        Pieces.add(new Bishop(false, 0, 0));
    }

    public List<Piece> getPieces() {
        return Pieces;
    }

}
