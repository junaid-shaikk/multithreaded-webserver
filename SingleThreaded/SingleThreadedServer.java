import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class SingleThreadedServer {
    private static final int PORT = 8010;
    private static final Logger logger = Logger.getLogger(SingleThreadedServer.class.getName());
    private ServerSocket serverSocket;

    public void start() {
        try {
            // Setup logger
            FileHandler fileHandler = new FileHandler("server.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);

            // Starting the server
            serverSocket = new ServerSocket(PORT);
            logger.info("Server started on port: " + PORT);

            while (!serverSocket.isClosed()) {
                try (Socket clientSocket = serverSocket.accept();
                     PrintWriter outToClient = new PrintWriter(clientSocket.getOutputStream(), true);
                     BufferedReader inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                    logger.info("Connection accepted from: " + clientSocket.getRemoteSocketAddress());
                    String message = inFromClient.readLine();
                    logger.info("Received message: " + message);

                    // Responding to client
                    outToClient.println("Hello from the Server");
                } catch (IOException ex) {
                    logger.log(Level.SEVERE, "Error handling client connection", ex);
                }
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Server error", ex);
        } finally {
            stop();
        }
    }

    public void stop() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                logger.info("Server shutdown gracefully.");
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Error during server shutdown", ex);
        }
    }

    public static void main(String[] args) {
        SingleThreadedServer server = new SingleThreadedServer();
        Runtime.getRuntime().addShutdownHook(new Thread(server::stop));
        server.start();
    }
}
