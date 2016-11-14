import Entities.Edge;
import Entities.PhysicalMachine;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nicole on 9/11/16.
 */

//http://jmeter.apache.org/usermanual/index.html

public class Controller {
    private List<Edge> edges;

    public Controller(){
        this.edges = new ArrayList<Edge>();
    }
    public void distributeWorkloadOnAllNodes(){
        for(Edge edge : edges) {
            if(!edge.distributeWorkload()){
                //distributeAgain
            }else{
                //sla is fulfilled, NOTHING TO DO
            }
        }
    }

}
