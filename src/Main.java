import java.io.IOException;

/**
 * Created by Nicole on 9/11/16.
 */

public class Main{
    public static void main(String[] args) throws IOException {
        //Controller cont = new Controller(14271);
        Controller cont = new Controller(1000);
        cont.distributeWorkloadOnAllNodes();
        /*while(true) {
            cont.socketAccept();
        }*/
        return;
    }
}