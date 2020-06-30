package cz.cvut.fel.pjv.chessproject.Data;

import cz.cvut.fel.pjv.chessproject.Enums.GameStatus;
import cz.cvut.fel.pjv.chessproject.Enums.PieceTypes;
import cz.cvut.fel.pjv.chessproject.Model.Move;
import java.util.List;

/**
 * Data class to store current games necessary information.
 */
public class GameData {

    private GameStatus status;
    private boolean currentPLayerIsWhite;
    private boolean isAIGame;
    //stores positions, if the piece is alive and if it has moved
    private List<int[]> wData;
    private List<int[]> bData;
    //stores the type, information in Data nad Types are linked via indexes
    private List<PieceTypes> wTypes;
    private List<PieceTypes> bTypes;
    private List<Move> historyOfMoves;

    public GameData() {
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public void setCurrentPLayerIsWhite(boolean currentPLayerIsWhite) {
        this.currentPLayerIsWhite = currentPLayerIsWhite;
    }

    public void setIsAIGame(boolean isAIGame) {
        this.isAIGame = isAIGame;
    }

    public void setwData(List<int[]> wData) {
        this.wData = wData;
    }

    public void setbData(List<int[]> bData) {
        this.bData = bData;
    }

    public void setwTypes(List<PieceTypes> wTypes) {
        this.wTypes = wTypes;
    }

    public void setbTypes(List<PieceTypes> bTypes) {
        this.bTypes = bTypes;
    }

    public GameStatus getStatus() {
        return status;
    }

    public boolean isCurrentPLayerIsWhite() {
        return currentPLayerIsWhite;
    }

    public boolean isIsAIGame() {
        return isAIGame;
    }

    public List<int[]> getwData() {
        return wData;
    }

    public List<int[]> getbData() {
        return bData;
    }

    public List<PieceTypes> getwTypes() {
        return wTypes;
    }

    public List<PieceTypes> getbTypes() {
        return bTypes;
    }

    public void setHistoryOfMoves(List<Move> historyOfMoves) {
        this.historyOfMoves = historyOfMoves;
    }

    public List<Move> getHistoryOfMoves() {
        return historyOfMoves;
    }

}
