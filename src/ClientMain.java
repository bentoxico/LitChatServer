import java.io.IOException;

public class ClientMain {
    public static void main(String[] args) {
        try {
            Client client = new Client();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
