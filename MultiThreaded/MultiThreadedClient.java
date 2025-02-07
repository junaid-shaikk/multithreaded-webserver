import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MultiThreadedClient {
    private static final int PORT = 8020;
    private static final String SERVER_ADDRESS = "localhost";
    private static final int THREAD_COUNT = 100;
    private static final Logger logger = Logger.getLogger(MultiThreadedClient.class.getName());
    private static final ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_COUNT);
    private static final ConcurrentHashMap<String, String> cache = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        setupLogger();
    
        for (int i = 0; i < THREAD_COUNT; i++) {
            threadPool.execute(() -> {
                try {
                    String threadId = Thread.currentThread().getName();
                    sendRequest("Hello from Client " + threadId);
                } catch (IOException ex) {
                    logger.log(Level.SEVERE, "Error in client thread", ex);
                }
            });
        }
    
        threadPool.shutdown();
    }
    
    private static void sendRequest(String message) throws IOException {
        if (cache.containsKey(message)) {
            logger.info("Cache hit! Retrieved from client cache: " + cache.get(message));
            return;
        }

        InetAddress address = InetAddress.getByName(SERVER_ADDRESS);
        try (Socket socket = new Socket(address, PORT);
             PrintWriter toServer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            toServer.println(message);
            logger.info("Sent: " + message);

            String response = fromServer.readLine();
            logger.info("Received: " + response);
            cache.put(message, response);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Error communicating with the server", ex);
        }
    }

    private static void setupLogger() {
        try {
            FileHandler fileHandler = new FileHandler("multithreaded_client.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            logger.setLevel(Level.INFO);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Error setting up logger", ex);
        }
    }
}