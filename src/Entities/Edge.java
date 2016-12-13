package Entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

/**
 * Created by Nicole on 9/11/16.
 */
public class Edge {
    private List<PhysicalMachine> pms;
    private Location location;
    private static double SLA_Performance = 0.5;
    private static double SLA_Latency = 0.8;
    private static double SLA_Recovery = 0.9;
    private int numRequests=0;               //fill with num requests
    private int numFailedRequests=0;		//fill with num failed requests
    List<ResultList> results;
    private long durationRequest=0;			//Durationtime of a request
    private long durationRequestTotal=0;
    private long durationRecovery=0;		//Durationtime of recovery
    private State state;
    private Random r = new Random();
    private int idleStateEnergyConsumption=0;

    public Edge(int numPms, int numVms, Location location) {
        state = State.NEW;
        pms =  new ArrayList<PhysicalMachine>();
        this.location = location;
        for (int i =0; i<numPms; i++){
            PhysicalMachine pm = new PhysicalMachine(numVms);
            idleStateEnergyConsumption+= pm.getIdleStateEnergyConsumption();
            pms.add(pm);
        }
    }

    public boolean distributeWorkload(Stack<Request> requests) {

        //init for checking slas

            numRequests = requests.size();

            results = new ArrayList<ResultList>();
            for (PhysicalMachine pm : pms) {
                int numVms = pm.getPmSize();
                results.add(pm.execute(requests));
            }

            return checkSlas();

    }
    public double getTotalEnergyUtilization(){
        int totalEnergyUtilization = 0;
        for(PhysicalMachine pm : pms){
            totalEnergyUtilization += pm.getTotalEnergyUtilization();
        }
        return idleStateEnergyConsumption + totalEnergyUtilization;
    }
    private boolean checkSlas(){
        if(checkPerformance() && checkLatency() & checkRecovery() & !checkAvailabilty()){  //TODO: Availability
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
        //availability = uptime/(uptime+downtime);          TODO: woher bekommen wir die uptime und downtime?

        if(availability >= 0.98)
            return true;

        return false;
    }

    public int getEdgeSize() {
        return pms.size();
    }
    public List<ResultList> getResults(){
        return results;
    }
    public Location getLocation() {
        return location;
    }
}

