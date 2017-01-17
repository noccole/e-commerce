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
    private List<ResultList> results;
    private Stack<Request> allRequests;
    private State state;
    private int idleStateEnergyConsumption=0;
    private BooleanGenerator generator;
    private int numPMs;
    private int numVms;
    private int edgefails;


    public Edge(int numPms, int numVms, Location location) {
        this.state = State.IDLE;
        this.numPMs = numPms;
        this.numVms = numVms;
        this.pms =  new ArrayList<PhysicalMachine>();
        this.location = location;
        this.results = new ArrayList<ResultList>();
        this.allRequests = new Stack<Request>();
        this.edgefails = 0;
        for (int i =0; i<numPms; i++){
            PhysicalMachine pm = new PhysicalMachine(numVms);
            idleStateEnergyConsumption+= pm.getIdleStateEnergyConsumption();
            pms.add(pm);
        }
    }

    public List<ResultList>  distributeWorkload( Request request) {
        allRequests.add(request);

        if(generator.generateBoolean(0.80)) {           //let fail edge randomly
            this.execute(request);
            return results;
        }else{
            this.edgefails ++;
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


    public int getEdgefails(){
        return this.edgefails;
    }
    public void restartEdge(){
        this.state = State.IDLE;
    }
    public void failEdge(){
        this.state = State.FAILED;
    }

    public int getPMs(){
        return this.numPMs;
    }
    public List<PhysicalMachine> getPMs2(){
        return this.pms;
    }
    public int getVms(){
        return this.numVms;
    }
    public Stack<Request> getAllRequests() {
        return allRequests;
    }
}

