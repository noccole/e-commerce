import Entities.Edge;
import Entities.Location;
import Entities.PhysicalMachine;
import Entities.Request;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.*;

/**
 * Created by Nicole on 9/11/16.
 */

//http://jmeter.apache.org/usermanual/index.html

public class Controller {

    private List<Edge> edges;
    private Stack<Request> requests;
    private Random r = new Random();

    public Controller(int numRequests){
        edges = new ArrayList<Edge>();
        edges.add(new Edge(5, 8, Location.NORTH));
        edges.add(new Edge(10, 12, Location.EAST));
        edges.add(new Edge(2, 3, Location.SOUTH));
        edges.add(new Edge(6, 6, Location.WEST));

        createWorkload(numRequests);
    }

    public void createWorkload(int numRequests){
        requests = new Stack<Request>();

        for(int i=0; i<100; i++){
            requests.push(createRequestWithUniformVariables());
        }
    }
    public void distributeWorkloadOnAllNodes(){

        //TODO: Distribute workload with bfd heuristic

        Stack<Request> requestsNorth = new Stack<Request>();
        Stack<Request> requestsEast = new Stack<Request>();
        Stack<Request> requestsSouth = new Stack<Request>();
        Stack<Request> requestsWest = new Stack<Request>();
        for(Request request : requests){
                Location location = request.getLocation();
                if(location == Location.NORTH) {
                    requestsNorth.push(request);
                }else if(location == Location.EAST){
                    requestsEast.push(request);
                }else if(location == Location.SOUTH){
                    requestsSouth.push(request);
                }else if(location == Location.WEST){
                    requestsWest.push(request);
                }
        }
        for(Edge edge : edges){
            Location location = edge.getLocation();
            if(location == Location.NORTH) {
                edge.distributeWorkload(requestsNorth);
            }else if(location == Location.EAST){
                edge.distributeWorkload(requestsEast);
            }else if(location == Location.SOUTH){
                edge.distributeWorkload(requestsSouth);
            }else if(location == Location.WEST){
                edge.distributeWorkload(requestsWest);
            }

        }

    }

    //method for uniformly distributing the request variables: memory, cpu, startTime and duration
    public Request createRequestWithUniformVariables(){
        int startTime = (int)(Math.random()*1481213984);                 //date of 8.12.2016 as mean value
        int duration = (int)(Math.random()*7);                              // 5 is max for sla

        List<Location> VALUES = Collections.unmodifiableList(Arrays.asList(Location.values()));

        Location randomLocation = VALUES.get(r.nextInt(VALUES.size()));

        Request request = new Request(startTime, duration, randomLocation);
        return request;
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

    /*public Controller(int udpPort) throws IOException {
        socketInit(udpPort);
    }*/

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
