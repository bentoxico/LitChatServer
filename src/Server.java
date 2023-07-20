import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

public class Server {

    private int portNumber = 6001;
    private ArrayList<ServerHelper> clientsSocketList = new ArrayList<>();

    public void acceptSocket() {

        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);

            while (true) {

                Socket clientSocket = serverSocket.accept(); // Blocking ----
                System.out.println("Connected");

                ServerHelper person = new ServerHelper(clientSocket);
                Thread thread = new Thread(person);

                clientsSocketList.add(person);

                System.out.println("ServerHelper created");
                thread.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void sendAll(String message, String name) {

        for (ServerHelper helper : clientsSocketList) {

            try {
                if (helper.name == name) {
                    continue;
                }
                PrintWriter out = new PrintWriter(helper.clientSocket.getOutputStream(), true);
                out.println(TerminalColors.CYAN.getText() + name + ": " + TerminalColors.RESET.getText() + message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public class ServerHelper implements Runnable {

        private Socket clientSocket;
        private String name;

        public ServerHelper(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            receive();
        }

        public void receive() {

            try {
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                out.println("Write your name -----");
                name = in.readLine();
                out.println("Nice! Ready to send message -----");

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            while (true) {

                try {

                    //PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                    String receivedMessage = in.readLine();
                    System.out.println(name + " typed: " + receivedMessage);
                    //System.out.println(Thread.currentThread().getName());

                    if (receivedMessage != null) {
                        send(receivedMessage);
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        public void send(String message) {
            sendAll(message, name);
        }
    }
}

