package Entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nicole on 9/11/16.
 */
public class PhysicalMachine {
    private List<VirtualMachine> vms;
    private int memory;
    private int cpu;
    private int bandwidth;

    private double consumedMemory;
    private double consumedCPU;
    private double consumedNetworkBandwidth;       //depends on the consumed memory

    private int idleStateEnergyConsumption;

    public PhysicalMachine(int numVms) {
        vms = new ArrayList<VirtualMachine>(numVms);
        memory = 4000;
        cpu = 2000;
        idleStateEnergyConsumption = 20;
    }

    public int getPmSize() {
        return vms.size();
    }

    public void execute(){
        // set consumedMemory;
        //set consumedCPU;
        // set consumedNetworkBandwidth;
    }
    public ResultList distributeWorkload() {
        ResultList results = new ResultList();
        for(VirtualMachine vm: vms){
            results.addResult(vm.execute());
        }
        results.calculateStartingPoint();
        results.calculateFailedRequests();
        return results;
    }

    public double getTotalEnergyUtilization(){
        return idleStateEnergyConsumption + consumedCPU *cpu + consumedMemory* memory + consumedNetworkBandwidth * bandwidth;
    }

}
