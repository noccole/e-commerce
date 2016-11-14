import Entities.Edge;
import Entities.PhysicalMachine;

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
}
