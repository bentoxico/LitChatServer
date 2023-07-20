import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {

        private ExecutorService cachedPool = Executors.newCachedThreadPool();
        private Socket socket;

    public Client() throws IOException {
            socket = new Socket("localhost",6001);
            cachedPool.submit(this::sendMessage);
            cachedPool.submit(this::receiveMessage);
        }
        public void sendMessage () {

            while (true) {

                try {
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

                    String message = in.readLine();
                    out.println(message);
                    //System.out.println(Thread.currentThread().getName());

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        public void receiveMessage () {

            while (true) {

                try {
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    String receivedMessage = in.readLine();
                    out.write(receivedMessage);
                    System.out.println(receivedMessage);
                    //System.out.println(Thread.currentThread().getName());

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
