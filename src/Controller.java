import Entities.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by Nicole on 9/11/16.
 */

//http://jmeter.apache.org/usermanual/index.html

public class Controller {

    private List<Edge> edges;
    private Stack<Request> initialRequests;
    private Stack<Request> requestsNorth;
    private Stack<Request> requestsEast;
    private Stack<Request> requestsSouth;
    private Stack<Request> requestsWest;
    private List<ResultList> results;
    private Random r = new Random();
    private static double SLA_Performance = 0.5;
    private static double SLA_Latency = 0.8;
    private static double SLA_Recovery = 0.9;
    private float uptime = System.currentTimeMillis()/1000F;
    private long downtime = 0;
    private long durationRequest=0;			//Durationtime of a request
    private long durationRequestTotal=0;
    private long durationRecovery=0;		//Durationtime of recovery
    private static final Logger logger = Logger.getLogger( Controller.class.getName() );
    private int count = 0;

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
        results = new ArrayList<ResultList>();
        requestsNorth= new Stack<Request>();
        requestsEast = new Stack<Request>();
        requestsSouth = new Stack<Request>();
        requestsWest = new Stack<Request>();
        createWorkload(numRequests);
        logger.info("Create Edges!");
    }

    public void createWorkload(int numRequests){
        initialRequests = new Stack<Request>();

        for(int i=0; i<numRequests; i++){
            initialRequests.add(createRequestWithUniformVariables());
        }
        logger.info("Create Workload!");
    }
    public void initialWorkloadDistribution(){
        for (Request request : initialRequests) {
            Location location = request.getLocation();
            if (location == Location.NORTH) {
                requestsNorth.push(request);
            } else if (location == Location.EAST) {
                requestsEast.push(request);
            } else if (location == Location.SOUTH) {
                requestsSouth.push(request);
            } else if (location == Location.WEST) {
                requestsWest.push(request);
            }
        }
        PrintOutputBefore();
        findBestEdgeAndDistributeWorkload(requestsNorth, Location.NORTH);
        findBestEdgeAndDistributeWorkload(requestsEast, Location.EAST);
        findBestEdgeAndDistributeWorkload(requestsSouth, Location.SOUTH);
        findBestEdgeAndDistributeWorkload(requestsWest, Location.WEST);
        PrintOutputAfter();
    }

    public void findBestEdgeAndDistributeWorkload(Stack<Request> requestsLocation, Location location){
        while(!requestsLocation.empty()) {
            Request req = requestsLocation.pop();
            Edge selectedEdge = selectPerfectEdge(req, location);
            this.execute(selectedEdge, req);
        }
        logger.info("Find the edge, which fits best to distribute the workload!");
    }
    private Edge selectPerfectEdge(Request request, Location location){
        double lowestEnergy = Integer.MAX_VALUE;
        Edge selectedEdge = null;
        for (Edge edge : edges) {
            Location edgeLocation = edge.getLocation();
            if (edgeLocation == location) {
                if(edge.getTotalEnergyUtilization() < lowestEnergy && edge.getState() != State.FAILED) {
                    lowestEnergy = edge.getTotalEnergyUtilization();
                    selectedEdge = edge;
                }else{
                    if(selectedEdge != null)
                        selectedEdge.restartEdge();              //set edge after failure to idle for next run
                }
            }
        }
        return selectedEdge;
    }
    private void execute(Edge selectedEdge, Request request) {
        try {
            List<ResultList> edgeResult = new ArrayList<ResultList>();
            edgeResult = selectedEdge.distributeWorkload(request);
            Location location = selectedEdge.getLocation();

            if (edgeResult == null) {            //Edge fails, retry the request on other edge
                List<Request> retryRequestsOnOtherEdge = selectedEdge.getAllRequests();     //Here ALL requests of edge are retried, not only for starting point

                Stack<Request> requestsLocation = new Stack<Request>();
                requestsLocation.addAll(retryRequestsOnOtherEdge);
                if (location == Location.NORTH) {
                    requestsLocation.addAll(requestsNorth);
                } else if (location == Location.EAST) {
                    requestsLocation.addAll(requestsEast);
                } else if (location == Location.SOUTH) {
                    requestsLocation.addAll(requestsSouth);
                } else if (location == Location.WEST) {
                    requestsLocation.addAll(requestsWest);
                }
                findBestEdgeAndDistributeWorkload(requestsLocation, location);
                return;

            } else {
                //Edge does not fail
                if (location == Location.NORTH) {
                    requestsNorth.remove(request);
                } else if (location == Location.EAST) {
                    requestsEast.remove(request);
                } else if (location == Location.SOUTH) {
                    requestsSouth.remove(request);
                } else if (location == Location.WEST) {
                    requestsWest.remove(request);
                }
                results.addAll(edgeResult);
                return;
            }

        } catch (Exception e) {
            if (count != 100) {
                count++;
                //System.out.println("Request Failed " + count);
                execute(selectedEdge, request);
            }
            //System.out.println("Request Failed over Limit and is terminated");
        }
        logger.info("Distribute Workload");
    }
    //method for uniformly distributing the request variables: memory, cpu, startTime and duration
    public Request createRequestWithUniformVariables(){
        int startTime = (int)(Math.random()*1481213984);                 //date of 8.12.2016 as mean value
        int duration = (int)(Math.random()*7);                              // 5 is max for sla
        int ressources = (int)(Math.random()*2000+1);                   // random cpu size for workload per request
        List<Location> VALUES = Collections.unmodifiableList(Arrays.asList(Location.values()));

        Location randomLocation = VALUES.get(r.nextInt(VALUES.size()));

        Request request = new Request(startTime, duration, randomLocation,ressources);
        logger.info("Distribute the request variables uniformly: memory, cpu, start time and duration");
        return request;
    }
    private boolean checkSlas(){
        if(checkPerformance() && checkLatency() & checkRecovery() & checkAvailabilty()){
            return true;
        }
        return false;
    }

    private boolean checkPerformance(){
        int totalFailed=0;
        int numRequests =0;
        for(ResultList result : results){
            totalFailed += result.getFailedRequests();
            result.getNumResults();
            this.downtime++;
        }
        logger.info("Check Performance! (max. 2 % failed tasks per fullfilled request)");
        //Performance: Maximum of 2 % failed tasks per fullfilled request
        if(totalFailed/numRequests < SLA_Performance){
            return true;
        }else {
            return false;
        }
    }

    private boolean checkLatency(){
        for(int x=0; x<=99; x++){
            durationRequestTotal+=durationRequestTotal+durationRequest;
        }
        logger.info("Check Latency! (Per 100 tasks, max. 0.5 sec. processing time)");
        //Latency: Per 100 tasks maximum 0,5 seconds of processing time
        if(durationRequestTotal < SLA_Latency)
            return true;
        return false;
    }

    private boolean checkRecovery(){
        double mttr = 0;
        logger.info("Check Recovery! (Mean time to recover is 0.2 sec.)");
        if(durationRecovery < SLA_Recovery)
            return true;


        return false;

        /*
        Mean time to recover from failure:
        MTTR = totalDownTimeCausedByFailure/numberOfBreakdowns
         */
    }

    private boolean checkAvailabilty(){
        float availability = 0;
        logger.info("Check availability! (must be at least 98 % of the uptime");
        availability = this.uptime/(this.uptime+this.downtime);

        if(availability >= 0.98)
            return true;


        return false;
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

    public void PrintOutputBefore(){

        System.out.println("VALUES BEFORE EXECUTION:");

        System.out.println("Amount of Edges: " +edges.size() );
        int countPms = 0;
        int countVms = 0;
        for(Edge e : edges){
            countPms += e.getPMs();
            countVms += e.getVms();
        }
        System.out.println("Amount of PMs: "+ countPms);
        System.out.println("Amount of VMs: "+ countVms);
        System.out.println("Initial Requests size " +initialRequests.size());
        System.out.println("Requests North " +requestsNorth.size());
        System.out.println("Requests East " +requestsEast.size());
        System.out.println("Requests South " +requestsSouth.size());
        System.out.println("Requests West " +requestsWest.size());

    }
    public void PrintOutputAfter(){

        System.out.println("VALUES AFTER EXECUTION:") ;
        int sucessful = 0;

        for(ResultList resultlist : results){
            sucessful++;
        }

        System.out.println("Task Executions "+ sucessful);
        System.out.println("Final Retries "+ (sucessful - initialRequests.size()));
    }
}
