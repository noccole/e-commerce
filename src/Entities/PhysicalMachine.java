package Entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nicole on 9/11/16.
 */
public class PhysicalMachine {
    private List<VirtualMachine> vms;
    private boolean success;
    private int memory;
    private int cpu;
    private int network;
    private int energyCpu;
    private int energyMemory;
    private int energyNetwork;

    private double workloadrateCpu;            // in percent
    private double workloadrateMemory;
    private double workloadrateNetwork;

    private int idleStateEnergyConsumption;

    public PhysicalMachine(int numVms) {
        vms = new ArrayList<VirtualMachine>(numVms);
        memory = 4000;
        cpu = 2000;
        network= 100;
        energyCpu = 95;             // energy in watt
        energyMemory = 5;
        energyNetwork = 3;
        idleStateEnergyConsumption = 20;
    }

    public int getPmSize() {
        return vms.size();
    }

    public ResultList execute(List<Request> requests){
        if(requests.size() != this.getPmSize()) {
            success = false;
            return null;
        }
        // TODO: calculate workloads
        ResultList results = new ResultList();
        int index=0;
        for(VirtualMachine vm: vms){
            results.addResult(vm.execute(requests.get(index++)));
        }
        results.calculateStartingPoint();
        results.calculateFailedRequests();
        return results;
    }



    public double getTotalEnergyUtilization(){
        // consumed Cpu = workloadRateCpu * MaxCpu
        return idleStateEnergyConsumption + workloadrateCpu *energyCpu + workloadrateMemory* energyMemory + workloadrateNetwork * energyNetwork;
    }

}
