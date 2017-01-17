import java.io.IOException;

/**
 * Created by Nicole on 9/11/16.
 */

public class Main{
    public static void main(String[] args) throws IOException {
        //Controller cont = new Controller(14271);
        Controller cont = new Controller(10000);
        cont.initialWorkloadDistribution();
        System.out.println("Summery: EXECUTION FINISHED SUCCESSFUL");
        /*while(true) {
            cont.socketAccept();
        }*/
        return;
    }
}