package Entities;

import java.util.List;

/**
 * Created by Nicole on 9/11/16.
 */
public class Edge {
    private List<PhysicalMachine> pms;
    private boolean isFailed;

    public void edgeFails(){
        isFailed = true;
    }
    public boolean getIsFailed(){
        return isFailed;
    }
}
