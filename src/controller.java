import Entities.Edge;
import Entities.PhysicalMachine;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.List;

/**
 * Created by Nicole on 9/11/16.
 */

//http://jmeter.apache.org/usermanual/index.html

public class Controller {
    private static double SLA_PERCENT_NUM_FAILURES = 0.05; //maximize the value but be below the threshold
    private List<Edge> edges;

    public void distributeWorkloadOnAllNodes(){

        //Distribute workload with bfd heuristic
    }
   /* public void listenForFailedNodes(){
        //if a pm/edge fails -> retry with the same workload on the same node
    }
    public void distributeWorkload(Edge failedEdge, Edge newEdge){

    }
    public void distributeWorkloadPm(PhysicalMachine failedPm, PhysicalMachine newPm){

    }*/

    public static final int UDP_PACKET_BUFFER_SIZE = 1024;
    public static final String UDP_PACKET_CONTROL_SIZE_PREFIX = "!CONTROL.SIZE:";
    private DatagramSocket udpSocket;

    public Controller(int udpPort) throws IOException {
        socketInit(udpPort);
    }

    public void socketInit(int port) throws IOException {
        if(this.udpSocket == null || this.udpSocket.isClosed()){
            this.udpSocket = new DatagramSocket(port);
        }
    }

    public Runnable socketAccept() throws IOException {
        byte[] receiveData = new byte[UDP_PACKET_BUFFER_SIZE];
        final DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        this.udpSocket.receive(receivePacket);
        String msg = new String(receivePacket.getData(), 0, receivePacket.getLength());
        System.out.println(msg);
        return null;
    }
}
