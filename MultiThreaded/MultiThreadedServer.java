import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MultiThreadedServer {
    private static final int PORT = 8020;
    private static final int THREAD_POOL_SIZE = 10;
    private static final Logger logger = Logger.getLogger(MultiThreadedServer.class.getName());
    private ServerSocket serverSocket;
    private final ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    private final ConcurrentHashMap<String, String> cache = new ConcurrentHashMap<>();

    public void start() {
        try {
            setupLogger();
            serverSocket = new ServerSocket(PORT);
            logger.info("Server started on port: " + PORT);

            while (!serverSocket.isClosed()) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    logger.info("Connection accepted from: " + clientSocket.getRemoteSocketAddress());
                    threadPool.execute(() -> handleClient(clientSocket));
                } catch (IOException ex) {
                    logger.log(Level.SEVERE, "Error accepting client connection", ex);
                }
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Server error", ex);
        } finally {
            stop();
        }
    }

    private void handleClient(Socket clientSocket) {
        try (PrintWriter outToClient = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            String message = inFromClient.readLine();
            logger.info("Received message from client: " + message);
            
            // Check cache for response
            String response = cache.computeIfAbsent(message, k -> {
                logger.info("Processing new request: " + k);
                return "Hello from the Server - Cached Response for: " + k;
            });

            outToClient.println(response);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Error handling client", ex);
        } finally {
            try {
                clientSocket.close();
                logger.info("Closed connection with client");
            } catch (IOException ex) {
                logger.log(Level.SEVERE, "Error closing client socket", ex);
            }
        }
    }

    private void setupLogger() {
        try {
            FileHandler fileHandler = new FileHandler("multithreaded_server.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Error setting up logger", ex);
        }
    }

    public void stop() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                logger.info("Server shutdown gracefully.");
            }
            threadPool.shutdown();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Error during server shutdown", ex);
        }
    }

    public static void main(String[] args) {
        MultiThreadedServer server = new MultiThreadedServer();
        Runtime.getRuntime().addShutdownHook(new Thread(server::stop));
        server.start();
    }
}
