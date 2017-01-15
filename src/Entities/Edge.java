package Entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import java.util.logging.Logger;

/**
 * Created by Nicole on 9/11/16.
 */
public class Edge {
    private List<PhysicalMachine> pms;
    private Location location;
    private List<ResultList> results;
    private Stack<Request> allRequests;
    private State state;
    private int idleStateEnergyConsumption=0;
    private BooleanGenerator generator;
    private static final Logger logger = Logger.getLogger( Edge.class.getName() );
    private int numPMs;
    private int numVms;

    public Edge(int numPms, int numVms, Location location) {
        this.state = State.IDLE;
        this.numPMs = numPms;
        this.numVms = numVms;
        this.pms =  new ArrayList<PhysicalMachine>();
        this.location = location;
        this.results = new ArrayList<ResultList>();
        this.allRequests = new Stack<Request>();
        for (int i =0; i<numPms; i++){
            PhysicalMachine pm = new PhysicalMachine(numVms);
            idleStateEnergyConsumption+= pm.getIdleStateEnergyConsumption();
            pms.add(pm);
        }
    }
    public List<ResultList>  distributeWorkload( Request request) {
        allRequests.add(request);

        if(generator.generateBoolean(0.85)) {           //let fail edge randomly
            this.execute(request);
            return results;
        }else{
            this.failEdge();
            return null;
        }
    }
    private void execute(Request request){
        for (PhysicalMachine pm : pms) {
            if(pm.getState() != State.FAILED) {
                ResultList resList = pm.distributeWorkload(request);
                if(resList != null){                    // check if PM failed
                    resList.calculateStartingPoint();
                    resList.calculateFailedRequests();
                    results.add(resList);
                    return;
                }else{
                    this.execute(request);
                    return;
                }
            }else {
                pm.restartPm();                               //set to idle after restarting pm
            }
        }
    }
    public double getTotalEnergyUtilization(){
        int totalEnergyUtilization = 0;
        for(PhysicalMachine pm : pms){
            totalEnergyUtilization += pm.getTotalEnergyUtilization();
        }
        logger.info("Calculate the total energy utilization for the edge!");
        return idleStateEnergyConsumption + totalEnergyUtilization;
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
    public State getState() {
        return state;
    }

    public void restartEdge(){
        this.state = State.IDLE;
    }
    public void failEdge(){
        this.state = State.FAILED;
    }

    public Stack<Request> getAllRequests() {
        return allRequests;
    }

    public int getPMs(){
        return this.numPMs;
    }
    public int getVms(){
        return this.numVms;
    }
}

