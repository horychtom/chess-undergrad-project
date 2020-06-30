package cz.cvut.fel.pjv.chessproject.Data;

import cz.cvut.fel.pjv.chessproject.Enums.GameStatus;
import cz.cvut.fel.pjv.chessproject.Model.Board;
import cz.cvut.fel.pjv.chessproject.Enums.PieceTypes;
import cz.cvut.fel.pjv.chessproject.Pieces.Bishop;
import cz.cvut.fel.pjv.chessproject.Pieces.King;
import cz.cvut.fel.pjv.chessproject.Pieces.Knight;
import cz.cvut.fel.pjv.chessproject.Pieces.Pawn;
import cz.cvut.fel.pjv.chessproject.Pieces.Piece;
import cz.cvut.fel.pjv.chessproject.Pieces.Queen;
import cz.cvut.fel.pjv.chessproject.Pieces.Rook;
import java.util.ArrayList;
import java.util.List;

/**
 * Manipulation over GameData data.
 */
public class DataParser {

    private GameData data;

    public DataParser(GameData data) {
        this.data = data;
    }

    /**
     * Stores the data into GameData class. Extracts piece TYPE, piece [x,y]
     * coordinates dead/alive status and "has moved" status from each players
     * pieces list.
     */
    public void setData(List<Piece> Wpieces, List<Piece> Bpieces) {
        List<PieceTypes> wTypes = new ArrayList<>();
        List<PieceTypes> bTypes = new ArrayList<>();
        List<int[]> wData = new ArrayList<>();
        List<int[]> bData = new ArrayList<>();

        for (Piece p : Wpieces) {
            wTypes.add(p.getType());
            int alive = p.isAlive() ? 1 : 0;
            int moved = p.didNotMove() ? 1 : 0;
            int[] pieceData = {p.getX(), p.getY(), alive, moved};
            wData.add(pieceData);
        }

        for (Piece p : Bpieces) {
            bTypes.add(p.getType());
            int alive = p.isAlive() ? 1 : 0;
            int moved = p.didNotMove() ? 1 : 0;
            int[] pieceData = {p.getX(), p.getY(), alive, moved};
            bData.add(pieceData);
        }

        data.setbData(bData);
        data.setbTypes(bTypes);
        data.setwData(wData);
        data.setwTypes(wTypes);

    }

    /**
     * Sets data into the board which is taken as an argument.
     *
     * @param board instance we want to put data into.
     * @return same board instance but filled with data.
     */
    public Board setBoard(Board board) {
        //for whites
        List<PieceTypes> whites = data.getwTypes();
        List<int[]> whiteCoords = data.getwData();

        //iteration through all players pieces
        for (int i = 0; i < data.getwTypes().size(); i++) {

            Piece next = null;
            int[] coords = whiteCoords.get(i);
            //skip "alive==0"
            if (coords[2] == 0) {
                continue;
            }

            if (null != whites.get(i)) //Creating and setting the piece{
            {
                switch (whites.get(i)) {
                    case PAWN: {
                        next = new Pawn(true, coords[0], coords[1]);
                        break;
                    }
                    case KING: {
                        next = new King(true, coords[0], coords[1]);
                        break;
                    }
                    case KNIGHT: {
                        next = new Knight(true, coords[0], coords[1]);
                        break;
                    }
                    case BISHOP: {
                        next = new Bishop(true, coords[0], coords[1]);
                        break;
                    }
                    case QUEEN: {
                        next = new Queen(true, coords[0], coords[1]);
                        break;
                    }
                    case ROOK: {
                        next = new Rook(true, coords[0], coords[1]);
                        break;
                    }
                    default:
                        break;
                }
            }

            next.setDidNotMove(coords[3] == 1);
            board.setPiece(next, next.getX(), next.getY());
        }

        //for blacks
        List<PieceTypes> blacks = data.getbTypes();
        List<int[]> blackCoords = data.getbData();

        //iteration through all players pieces
        for (int i = 0; i < data.getbTypes().size(); i++) {

            Piece next = null;
            int[] coords = blackCoords.get(i);
            //skip "alive==0"
            if (coords[2] == 0) {
                continue;
            }

            if (null != blacks.get(i)) //Creating and setting the piece{
            {
                switch (blacks.get(i)) {
                    case PAWN: {
                        next = new Pawn(false, coords[0], coords[1]);
                        break;
                    }
                    case KING: {
                        next = new King(false, coords[0], coords[1]);
                        break;
                    }
                    case KNIGHT: {
                        next = new Knight(false, coords[0], coords[1]);
                        break;
                    }
                    case BISHOP: {
                        next = new Bishop(false, coords[0], coords[1]);
                        break;
                    }
                    case QUEEN: {
                        next = new Queen(false, coords[0], coords[1]);
                        break;
                    }
                    case ROOK: {
                        next = new Rook(false, coords[0], coords[1]);
                        break;
                    }
                    default:
                        break;
                }
            }

            next.setDidNotMove(coords[3] == 1);
            board.setPiece(next, next.getX(), next.getY());
        }

        if (data.getStatus() == GameStatus.PROMOTING) {
            data.setStatus(GameStatus.IS_RUNNING);
        }
        board.setHistoryOfMoves(data.getHistoryOfMoves());
        return board;
    }

}
