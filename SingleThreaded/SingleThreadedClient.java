import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class SingleThreadedClient {
    private static final int PORT = 8010;
    private static final String SERVER_ADDRESS = "localhost";
    private static final Logger logger = Logger.getLogger(SingleThreadedClient.class.getName());

    public void run() {
        try {
            // Setup logger
            FileHandler fileHandler = new FileHandler("client.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);

            InetAddress serverInetAddress = InetAddress.getByName(SERVER_ADDRESS);
            try (Socket clientSocket = new Socket(serverInetAddress, PORT);
                 PrintWriter outToServer = new PrintWriter(clientSocket.getOutputStream(), true);
                 BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                logger.info("Connected to server at: " + serverInetAddress);

                // Sending a message to the server
                outToServer.println("Hello from the Client: " + clientSocket.getLocalSocketAddress());

                // Reading the server's response
                String response = inFromServer.readLine();
                logger.info("Response from server: " + response);

                System.out.println("Response from server: " + response);
            }
        } catch (UnknownHostException ex) {
            logger.log(Level.SEVERE, "Unknown host", ex);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "I/O error", ex);
        }
    }

    public static void main(String[] args) {
        new SingleThreadedClient().run();
    }
}
