package Entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nicole on 9/11/16.
 */
public class Edge {
    private List<PhysicalMachine> pms;
    private Location location;
    private static double SLA = 0.05;
    private int numRequests=0;               //fill with num requests

    public Edge(int numPms) {
        pms = new ArrayList<PhysicalMachine>(numPms);
    }

    public int getEdgeSize() {
        return pms.size();
    }

    public List<ResultList> distributeWorkload() {
        List<ResultList> results = new ArrayList<ResultList>();
        for(PhysicalMachine pm: pms){
            results.add(pm.distributeWorkload());
        }
        checkSla(results);
        return results;
    }
    private boolean checkSla(List<ResultList> results){
        int totalFailed=0;
        for(ResultList result : results){
            totalFailed += result.getFailedRequests();
        }
        if(totalFailed/numRequests < SLA)
            return true;
        else
            return false;
    }
}

