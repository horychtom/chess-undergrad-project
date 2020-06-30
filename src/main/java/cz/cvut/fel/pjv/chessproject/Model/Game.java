package cz.cvut.fel.pjv.chessproject.Model;

import cz.cvut.fel.pjv.chessproject.Data.GameData;
import cz.cvut.fel.pjv.chessproject.Enums.GameStatus;
import cz.cvut.fel.pjv.chessproject.Enums.PieceTypes;
import cz.cvut.fel.pjv.chessproject.Pieces.King;
import cz.cvut.fel.pjv.chessproject.Pieces.Piece;
import cz.cvut.fel.pjv.chessproject.Players.Player;
import java.util.ArrayList;
import com.google.gson.*;
import cz.cvut.fel.pjv.chessproject.Data.DataParser;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.logging.*;

/**
 * Main game model class which holds all necessary information about players,
 * current state and board. Provides methods for players to create moves and
 * interact with the board.
 */
public class Game {

    private final Board board;
    private final Player player1;
    private final Player player2;
    private GameStatus currentStatus;
    private Player currentPlayer;
    private Move currentMove;
    private Piece promoting;
    private boolean isRunning;
    private List<int[]> possibleMoves;

    private static final Logger lgr = Logger.getLogger(Game.class.getName());

    /**
     * Constructor sets the necessary data for game to be playable and decides
     * if such game is playable (feature for the custom games).
     *
     * @param AIgame boolean variable to set game against computer true/false
     * @param board board created outside of this class
     */
    public Game(boolean AIgame, Board board) {
        this.currentStatus = GameStatus.IS_RUNNING;
        this.board = board;
        this.player1 = new Player(true, false);
        this.player2 = AIgame ? new Player(false, true) : new Player(false, false);

        player1.setMyKing(findKing(true));
        player2.setMyKing(findKing(false));
        this.currentPlayer = player1;

        //this checks for init statement of the board (if its nonsense set unplayable)
        if (player1.getMyKing() == null || player2.getMyKing() == null
                || player2.getMyKing().isThreatened(board)) {
            this.currentStatus = GameStatus.UNPLAYABLE;
        }
        //set players pieces according to the board
        board.setPlayerPieces(player1, player2);
        this.isRunning = true;
    }

    public List<int[]> getPossibleMoves() {
        return possibleMoves;
    }

    public boolean IsRunning() {
        return isRunning;
    }

    public void setIsRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    public GameStatus getCurrentStatus() {
        return currentStatus;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Board getBoard() {
        return board;
    }

    public Piece getPromotingPiece() {
        return promoting;
    }

    //REAL PLAYER
    /**
     * Checks if chosen starting position [x,y] is valid.
     */
    public boolean createMoveFrom(int x, int y) {
        if (board.isOutOfBounds(x, y)) {
            return false;
        }
        Piece movingPiece = board.getBox(x, y).getCurrentPiece();
        if (movingPiece == null) {
            return false;
        }
        if (movingPiece.IsWhite() != currentPlayer.isWhite()) {
            return false;
        }
        currentMove = new Move(x, y);
        possibleMoves = findPossibleMoves(movingPiece);
        return true;
    }

    /**
     * Checks if chosen ending position [x,y] is valid. Includes check
     * protection.
     */
    public boolean createMoveTo(int x, int y) {
        currentMove.setTo(x, y);
        return isInList(possibleMoves, x, y);
    }

    //AI PLAYER
    /**
     * Creates AI random move from possible moves.
     */
    public boolean createAImove() {
        currentMove = new Move();
        return currentPlayer.getRandomMove(currentMove, board);
    }

    /**
     * Change board with current move and checks consequences.
     */
    public GameStatus performGameCycle() {
        currentStatus = GameStatus.IS_RUNNING;
        board.makeChange(currentMove);

        //checks for NEW turn player (since the one who did the move was able to create it)
        swapTurns();
        if (currentPlayer.getMyKing().isThreatened(board)) {
            currentStatus = GameStatus.CHECK;
            ((King) currentPlayer.getMyKing()).setChecked(true);
        } else {
            ((King) currentPlayer.getMyKing()).setChecked(false);
        }
        currentStatus = checkStatus();
        if (currentStatus != GameStatus.CHECKMATE && currentStatus != GameStatus.STALEMATE) {
            currentStatus = checkPromoting();
        }

        board.getLastMove().setResultIn(currentStatus);
        return currentStatus;
    }

    //NECESSARY UTILS
    /**
     * Simple util to change current player
     */
    public void swapTurns() {
        if (currentPlayer == player1) {
            currentPlayer = player2;
        } else {
            currentPlayer = player1;
        }
    }

    /**
     * Finds possible moves to selected piece. Check resulting moves are
     * excluded.
     *
     * @param piece
     * @return usable list raw list from piece without check resulting moves.
     */
    public List<int[]> findPossibleMoves(Piece piece) {
        int x = piece.getX();
        int y = piece.getY();
        List<int[]> moves = piece.getPossibleMoves(board);
        ArrayList<int[]> checks = new ArrayList<>();
        for (int[] move : moves) {
            Move testMove = new Move(x, y, move[0], move[1]);
            if (board.willBeChecked(testMove, currentPlayer)) {
                checks.add(move);
            }
        }
        //removing these that result in check
        if (!checks.isEmpty()) {
            moves.removeAll(checks);
        }
        return moves;
    }

    //checks if promotion occured if so sets promoting piece and status
    private GameStatus checkPromoting() {
        Piece movingPiece = board.getBox(currentMove.getTo_x(), currentMove.getTo_y()).getCurrentPiece();

        try {
            if (movingPiece.getType() == PieceTypes.PAWN) {
                if (currentMove.getTo_y() == 7 || currentMove.getTo_y() == 0) {
                    this.promoting = movingPiece;
                    return GameStatus.PROMOTING;
                }
            }
        } catch (NullPointerException e) {
            return currentStatus;
        }
        return currentStatus;
    }

    //checks if stale or check mate occurs
    private GameStatus checkStatus() {

        //go through all my pieces and check all their moves ( if they can remove check) or at least move
        // if they cannot, iam checkmated or stalemated and game ends
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board.getBox(i, j).getCurrentPiece() != null) {
                    if (board.getBox(i, j).getCurrentPiece().IsWhite() == currentPlayer.isWhite()) {

                        /*get possible moves for the piece and exclude these that result in check, if 
                         all result in check, then moves is empty. if so for all -> *CHECKMATE* */
                        Piece tmp = board.getBox(i, j).getCurrentPiece();
                        List<int[]> moves = findPossibleMoves(tmp);
                        //this means there exists a move that removes the check!
                        if (!moves.isEmpty()) {
                            return currentStatus;
                        }
                    }
                }
            }
        }

        //no moves found, so its checkmate or stalemate
        if (((King) currentPlayer.getMyKing()).isChecked()) {
            return GameStatus.CHECKMATE;
        }

        return GameStatus.STALEMATE;
    }

    private Piece findKing(boolean isWhite) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board.getBox(i, j).getCurrentPiece() != null) {
                    if (board.getBox(i, j).getCurrentPiece().IsWhite() == isWhite) {
                        if (board.getBox(i, j).getCurrentPiece().getType() == PieceTypes.KING) {
                            return board.getBox(i, j).getCurrentPiece();
                        }
                    }
                }
            }
        }
        return null;
    }

    private boolean isInList(List<int[]> moves, int x, int y) {
        for (int[] m : moves) {
            if ((x == m[0]) && (y == m[1])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates GameData instance encapsulating necessary data and writes them
     * into file via json.
     */
    public void saveGame() {
        Gson gson = new Gson();
        GameData gd = new GameData();
        DataParser dp = new DataParser(gd);

        gd.setCurrentPLayerIsWhite(currentPlayer.isWhite());
        gd.setStatus(currentStatus);
        gd.setIsAIGame(player2.isAI());
        gd.setHistoryOfMoves(board.getHistoryOfMoves());
        dp.setData(player1.getMyPieces(), player2.getMyPieces());

        try {
            Writer fw = new FileWriter("src/main/resources/data/saved_game.json");
            gson.toJson(gd, fw);
            fw.flush();
            fw.close();
        } catch (IOException ex) {
            lgr.log(Level.SEVERE, "Cannot save the game", ex.getMessage());
        }
    }

}
