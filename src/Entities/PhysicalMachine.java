package Entities;

import java.util.List;

/**
 * Created by Nicole on 9/11/16.
 */
public class PhysicalMachine {
    private List<VirtualMachine> vms;
    private Process p;
    private boolean isFailed;

    public PhysicalMachine(){
        //p = new Process();
    }

    public void edgeFails(){
        isFailed = true;
    }
    public boolean getIsFailed(){
        return isFailed;
    }
}
