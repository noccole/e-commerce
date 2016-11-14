import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.io.Closeable;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
/**
 * Created by Nicole on 9/11/16.
 */
public class Main {

    public static void main(String[] args) throws IOException {
        Controller cont = new Controller(14271);
        while(true) {
            cont.socketAccept();
        }
    }
}