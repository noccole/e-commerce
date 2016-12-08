package Entities;

import java.util.ArrayList;
import java.util.List;

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

    public Edge(int numPms) {
        pms = new ArrayList<PhysicalMachine>(numPms);
    }

    public int getEdgeSize() {
        return pms.size();
    }

    public boolean distributeWorkload(List<Request> requests) {
        boolean success = false;
        results = new ArrayList<ResultList>();
        for(PhysicalMachine pm: pms){
            results.add(pm.execute(requests));
        }
        if(checkPerformance()==true && checkLatency()==true && checkRecovery()==true){
            success=true;
        }
        else {
            success=false;
        }
        return success;
    }

    private boolean checkPerformance(){
        int totalFailed=0;
        for(ResultList result : results){
            totalFailed += result.getFailedRequests();
        }
        //Performance: Maximum of 2 % failed tasks per fullfilled request
        if(totalFailed/numRequests < SLA_Performance){
            return true;
        }
        else {
            return false;
        }
    }

    private boolean checkLatency(){
        for(int x=0; x<=99; x++){
            durationRequestTotal+=durationRequestTotal+durationRequest;
        }
        //Latency: Per 100 tasks maximum 0,5 seconds of processing time
        if(durationRequestTotal < SLA_Latency){
            return true;
        }
        else {
            return false;
        }
    }

    private boolean checkRecovery(){
        if(durationRecovery < 0.2){
            return true;
        }
        else {
            return false;
        }
    }

    public List<ResultList> getResults(){
        return results;
    }
}

