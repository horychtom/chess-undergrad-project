package cz.cvut.fel.pjv.chessproject.Networking;

import com.google.gson.Gson;
import cz.cvut.fel.pjv.chessproject.Clock;
import cz.cvut.fel.pjv.chessproject.Enums.GameStatus;
import cz.cvut.fel.pjv.chessproject.StatusWrapper;
import cz.cvut.fel.pjv.chessproject.Menu;
import cz.cvut.fel.pjv.chessproject.Model.Game;
import cz.cvut.fel.pjv.chessproject.Model.Move;
import cz.cvut.fel.pjv.chessproject.Pieces.Queen;
import cz.cvut.fel.pjv.chessproject.Time;
import cz.cvut.fel.pjv.chessproject.View.BoardView;
import java.io.FileNotFoundException;
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

/**
 * Thread for receiving action in multiplayer mode. Reads the received json file
 * and let game do the move. All possible problems are logged.
 * ALERT: this whole class should be reworked, had no time for that
 */
public class Reciever implements Runnable {

    private final Game g;
    private volatile StatusWrapper s;
    private int PORT;
    private final BoardView bw;
    private Time time;

    private static final Logger lgr = Logger.getLogger(Reciever.class.getName());

    public Reciever(Game g, StatusWrapper s, boolean player1, BoardView bw, Time time) {
        this.g = g;
        this.s = s;
        this.PORT = player1 ? 5555 : 8888;
        this.bw = bw;
        this.time = time;
    }

    /**
     * Reads the received json file and let game do the move.
     */
    @Override
    public void run() {
        try {
            ServerSocket serverSocket = null;

            try {
                serverSocket = new ServerSocket(PORT);
            } catch (IOException ex) {
                lgr.log(Level.SEVERE, "Can't setup server on this port number.", ex.getMessage());
            }

            Socket socket = null;
            InputStream in = null;
            OutputStream out = null;

            try {
                socket = serverSocket.accept();
            } catch (IOException ex) {
                lgr.log(Level.SEVERE, "Can't accept client connection.", ex.getMessage());
            }

            try {
                in = socket.getInputStream();
            } catch (IOException ex) {
                lgr.log(Level.SEVERE, "Can't get socket input stream.", ex.getMessage());
            }

            try {
                out = new FileOutputStream("src/main/resources/data/recievedMove.json");
            } catch (FileNotFoundException ex) {
                lgr.log(Level.SEVERE, "File not found.", ex.getMessage());
            }

            byte[] bytes = new byte[16 * 1024];

            int count;
            while ((count = in.read(bytes)) > 0) {
                out.write(bytes, 0, count);
            }

            Gson gson = new Gson();
            Move recieved = new Move();
            try {
                recieved = gson.fromJson(new FileReader("src/main/resources/data/recievedMove.json"), Move.class);
            } catch (FileNotFoundException ex) {
                Menu.terminal.setText(">_no such file");
                lgr.log(Level.SEVERE, "File not found.", ex.getMessage());
                return;
            }
            lgr.log(Level.INFO, "MOVE RECIEVED CORRECTLY");
            setUp(recieved);

            out.close();
            in.close();
            socket.close();
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("fail");
        }
    }

    //performs opponents move, refreshes the board view and starts the clock,
    //everything is thread safe in runLater block.s
    private void setUp(Move recieved) {
        g.createMoveFrom(recieved.getFrom_x(), recieved.getFrom_y());
        g.createMoveTo(recieved.getTo_x(), recieved.getTo_y());
        g.performGameCycle();

        s.setGs(recieved.getResultIn());

        if (s.getGs() == GameStatus.PROMOTING) {
            g.getBoard().promote(g.getPromotingPiece(), new Queen(!g.getCurrentPlayer().isWhite(), 0, 0), g.getCurrentPlayer());
        }

        Platform.runLater(new Runnable() {
            public void run() {
                bw.reDraw();
                time.setIsOn(true);
                Thread clock = new Clock(time);
                clock.start();
                Menu.setTime.setDisable(true);
                Menu.seconds.setDisable(true);
            }
        });;
        g.setIsRunning(true);
    }

}
