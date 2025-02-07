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

public class ThreadPoolServer {
    private final ExecutorService threadPool;
    private static final Logger logger = Logger.getLogger(ThreadPoolServer.class.getName());
    private static final ConcurrentHashMap<String, String> cache = new ConcurrentHashMap<>();

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
        try (BufferedReader fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter toClient = new PrintWriter(clientSocket.getOutputStream(), true)) {
    
            String request = fromClient.readLine();
            if (request == null || request.trim().isEmpty()) {
                logger.warning("Received empty or null request. Ignoring.");
                return;
            }
    
            logger.info("Received request: " + request);
    
            String response;
            synchronized (cache) {
                if (cache.containsKey(request)) {
                    logger.info("Cache hit! Returning cached response for: " + request);
                    response = cache.get(request);
                } else {
                    response = "Hello from server " + clientSocket.getInetAddress() + "\n";
                    cache.put(request, response);
                    logger.info("Processed new request and cached response for: " + request);
                }
            }
    
            toClient.println(response);
            toClient.flush();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Error handling client", ex);
        } finally {
            try {
                clientSocket.close();
            } catch (IOException ex) {
                logger.log(Level.SEVERE, "Error closing client socket", ex);
            }
        }
    }           

    public static void main(String[] args) {
        int port = 8010;
        int poolSize = 1000;
        ThreadPoolServer server = new ThreadPoolServer(poolSize);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
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
            server.threadPool.shutdown();
            logger.info("Server shutdown gracefully.");
        }
    }
}
