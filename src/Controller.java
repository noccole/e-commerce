import Entities.*;

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
    private List<ResultList> results;
    private Random r = new Random();

    public Controller(int numRequests){
        edges = new ArrayList<Edge>();
        edges.add(new Edge(5, 8, Location.NORTH));
        edges.add(new Edge(10, 12, Location.EAST));
        edges.add(new Edge(2, 3, Location.SOUTH));
        edges.add(new Edge(6, 6, Location.WEST));
        edges.add(new Edge(2, 6, Location.NORTH));
        edges.add(new Edge(16, 18, Location.EAST));
        edges.add(new Edge(7, 7, Location.SOUTH));
        edges.add(new Edge(8, 8, Location.WEST));
        edges.add(new Edge(5, 6, Location.NORTH));
        edges.add(new Edge(8, 18, Location.EAST));
        edges.add(new Edge(3, 7, Location.SOUTH));
        edges.add(new Edge(9, 8, Location.WEST));

        createWorkload(numRequests);
    }

    public void createWorkload(int numRequests){
        requests = new Stack<Request>();

        for(int i=0; i<numRequests; i++){
            requests.push(createRequestWithUniformVariables());
        }
    }
    public void distributeWorkloadOnAllNodes(){
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
        requests = new Stack<Request>();
        requests.addAll(distributeWorkloadOnAllNodesForEachLocation(requestsNorth, Location.NORTH));
        requests.addAll(distributeWorkloadOnAllNodesForEachLocation(requestsEast, Location.EAST));
        requests.addAll(distributeWorkloadOnAllNodesForEachLocation(requestsSouth, Location.SOUTH));
        requests.addAll(distributeWorkloadOnAllNodesForEachLocation(requestsWest, Location.WEST));
    }

    public Stack<Request> distributeWorkloadOnAllNodesForEachLocation(Stack<Request> requestsLocation, Location location){
        Stack<Request> retryRequestsOnOtherEdge = new Stack<Request>();
        for(Request req : requestsLocation) {
            double lowestEnergy = Integer.MAX_VALUE;
            Edge selectededge = edges.get(0);
            for (Edge edge : edges) {
                Location edgeLocation = edge.getLocation();
                if (edgeLocation == location) {
                    if(edge.getTotalEnergyUtilization() < lowestEnergy && edge.getState() != State.FAILED) {
                        lowestEnergy = edge.getTotalEnergyUtilization();
                        selectededge = edge;
                    }else{
                        selectededge.setState(State.IDLE);
                    }
                }
            }
            Stack<Request> reqs = new Stack<Request>();
            reqs.push(req);
            if(reqs.size() > 0){
                List<ResultList> edgeResult = selectededge.distributeWorkload(reqs);
                if(selectededge.getState() == State.FAILED){
                    retryRequestsOnOtherEdge.addAll(selectededge.getAllRequests());
                    this.distributeWorkloadOnAllNodes();
                }
                //System.out.println(selectededge.distributeWorkload(reqs));
            }
        }
        return retryRequestsOnOtherEdge;
    }

    //method for uniformly distributing the request variables: memory, cpu, startTime and duration
    public Request createRequestWithUniformVariables(){
        int startTime = (int)(Math.random()*1481213984);                 //date of 8.12.2016 as mean value
        int duration = (int)(Math.random()*7);                              // 5 is max for sla
        int ressources = (int)(Math.random()*2000+1);                   // random cpu size for workload per request
        List<Location> VALUES = Collections.unmodifiableList(Arrays.asList(Location.values()));

        Location randomLocation = VALUES.get(r.nextInt(VALUES.size()));

        Request request = new Request(startTime, duration, randomLocation,ressources);
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
