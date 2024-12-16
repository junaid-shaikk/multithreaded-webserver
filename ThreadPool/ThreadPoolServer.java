import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ThreadPoolServer {
    private final ExecutorService threadPool;
    private static final Logger logger = Logger.getLogger(ThreadPoolServer.class.getName());

    public ThreadPoolServer(int poolSize) {
        this.threadPool = Executors.newFixedThreadPool(poolSize);
        setupLogger();
    }

    private void setupLogger() {
        try {
            FileHandler fileHandler = new FileHandler("threadpool_server.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            logger.setLevel(Level.INFO);
        } catch (IOException ex) {
            System.err.println("Failed to initialize logger: " + ex.getMessage());
        }
    }

    public void handleClient(Socket clientSocket) {
        try (PrintWriter toSocket = new PrintWriter(clientSocket.getOutputStream(), true)) {
            String message = "Hello from server " + clientSocket.getInetAddress();
            toSocket.println(message);
            logger.info("Handled client: " + clientSocket.getInetAddress());
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Error handling client", ex);
        }
    }

    public static void main(String[] args) {
        int port = 8010;
        int poolSize = 10; // Adjust the pool size as needed
        ThreadPoolServer server = new ThreadPoolServer(poolSize);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            serverSocket.setSoTimeout(70000);
            System.out.println("Server is listening on port " + port);
            logger.info("Server started on port " + port);

            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    logger.info("Accepted connection from: " + clientSocket.getInetAddress());

                    // Use the thread pool to handle the client
                    server.threadPool.execute(() -> server.handleClient(clientSocket));
                } catch (IOException ex) {
                    logger.log(Level.SEVERE, "Error accepting client connection", ex);
                }
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Server error", ex);
        } finally {
            // Shutdown the thread pool when the server exits
            server.threadPool.shutdown();
            logger.info("Server shutdown gracefully.");
        }
    }
}
