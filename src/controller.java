import Entities.Edge;
import Entities.PhysicalMachine;

/**
 * Created by Nicole on 9/11/16.
 */

//http://jmeter.apache.org/usermanual/index.html

public class Controller {
    int numberOfFailuresSLA; //maximize the value but be below the threshold

    public void distributeWorkloadOnAllNodes(){

        //Distribute workload with bfd heuristic
    }
    public void listenForFailedNodes(){
        //if a pm/edge fails -> retry with the same workload on the same node
    }
    public void distributeWorkload(Edge failedEdge, Edge newEdge){

    }
    public void distributeWorkloadPm(PhysicalMachine failedPm, PhysicalMachine newPm){

    }
}
