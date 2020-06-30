package cz.cvut.fel.pjv.chessproject.Networking;

import com.google.gson.Gson;
import cz.cvut.fel.pjv.chessproject.Model.Game;
import cz.cvut.fel.pjv.chessproject.Model.Move;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Thread for sending action in multiplayer mode. Saves the move into json file
 * and then sends the file. All possible problems are logged.
 */
public class Sender implements Runnable {

    private final Game g;
    private int PORT = 5555;

    private static final Logger lgr = Logger.getLogger(Game.class.getName());

    public Sender(Game g, boolean player) {
        this.g = g;
        this.PORT = player ? 8888 : 5555;
    }

    /**
     * Saves the move into json file and then sends the file. Working over
     * localhost.
     */
    @Override
    public void run() {
        try {
            Gson gson = new Gson();
            Move m = g.getBoard().getLastMove();

            try {
                Writer fw = new FileWriter("src/main/resources/data/sentMove.json");
                gson.toJson(m, fw);
                fw.flush();
                fw.close();
            } catch (IOException ex) {
                lgr.log(Level.SEVERE, "Couldn't write your action into file.", ex.getMessage());
            }

            Socket socket = new Socket("127.0.0.1", PORT);

            File file = new File("src/main/resources/data/sentMove.json");

            // Get the size of the file
            byte[] bytes = new byte[16 * 1024];
            InputStream in = new FileInputStream(file);
            OutputStream out = socket.getOutputStream();

            int count;
            while ((count = in.read(bytes)) > 0) {
                out.write(bytes, 0, count);
            }

            lgr.log(Level.INFO, "MOVE SENT CORRECTLY.");
            out.close();
            in.close();
            socket.close();
        } catch (IOException e) {
            lgr.log(Level.SEVERE, "Couldn't send the file.", e.getMessage());
        }
    }

}
