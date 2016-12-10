package Entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by Nicole on 9/11/16.
 */
public class Edge {
    private List<PhysicalMachine> pms;
    private Location location;
    private static double SLA_Performance = 0.02;
    private static double SLA_Latency = 0.5;
    private static double SLA_Recovery = 0.2;
    private int numRequests=0;               //fill with num requests
    private int numFailedRequests=0;		//fill with num failed requests
    List<ResultList> results;
    private long durationRequest=0;			//Durationtime of a request
    private long durationRequestTotal=0;
    private long durationRecovery=0;		//Durationtime of recovery
    private boolean failure;

    public Edge(int numPms, int numVms, Location location) {
        pms =  new ArrayList<PhysicalMachine>();
        this.location = location;
        for (int i =0; i<numPms; i++){
            pms.add(new PhysicalMachine(numVms));
        }


    }

    public int getEdgeSize() {
        return pms.size();
    }

    public boolean distributeWorkload(Stack<Request> requests) {

        //init for checking slas
        numRequests = requests.size();

        results = new ArrayList<ResultList>();
        for(PhysicalMachine pm: pms){
            int numVms = pm.getPmSize();
            results.add(pm.execute(requests));
        }

        return checkSlas();
    }
    private boolean checkSlas(){
        if(checkPerformance() && checkLatency() && checkRecovery()){
            return true;
        }
        return false;
    }

    private boolean checkPerformance(){
        int totalFailed=0;

        for(ResultList result : results){
            totalFailed += result.getFailedRequests();
        }

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

        //Latency: Per 100 tasks maximum 0,5 seconds of processing time
        if(durationRequestTotal < SLA_Latency)
            return true;

        return false;
    }

    private boolean checkRecovery(){
        double mttr = 0;
        if(durationRecovery < SLA_Recovery)
            return true;


        return false;

        /*
        Mean time to recover from failure:
        MTTR = totalDownTimeCausedByFailure/numberOfBreakdowns
         */
    }

    private boolean checkAvailabilty(){
        int availability = 0;
        //availability = uptime/(uptime+downtime);          TODO: woher bekommen wir dieuptime und downtime

        if(availability >= 0.98)
            return true;

        return false;
    }

    public List<ResultList> getResults(){
        return results;
    }
    public Location getLocation() {
        return location;
    }
}

