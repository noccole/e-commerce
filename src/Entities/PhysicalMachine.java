package Entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import java.util.logging.Logger;

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
    private final Logger logger = Logger.getLogger("physicalMachine");

    ResultList results;

    public PhysicalMachine(int numVms) {
        vms = new ArrayList<VirtualMachine>();
        memory = 4000;
        cpu = 2000;
        network= 100;
        energyCpu = 95;             // energy in watt
        energyMemory = 5;
        energyNetwork = 3;
        idleStateEnergyConsumption = 20;
        results = new ResultList();
        createVms(numVms);
    }
    private void createVms(int numVms){
        for(int i =0; i<numVms; i++) {
            vms.add(createVmWithNormallyDistributedVariables());
        }
    }
    //method for normal distributing the variables: memory, cpu, startTime and duration
    public VirtualMachine createVmWithNormallyDistributedVariables(){
        Random r = new Random();
        int memoryVm = (int) Math.floor(r.nextGaussian()*400+1000);               //1 GByte mean
        int cpuVm = (int) Math.floor(r.nextGaussian()*400+500);                   // 500 MByte mean
        int networkVm = (int) Math.floor(r.nextGaussian()*20+100);       // 100 MByte mean

        return new VirtualMachine(memoryVm, cpuVm, networkVm);
    }

    public ResultList execute(Stack<Request> requests){
        if(requests.size() > this.getPmSize()) {
            logger.info("too many requests for this machine" + requests.size() + " vms: "+ this.getPmSize());
        }
        // TODO: calculate workloads


        for(VirtualMachine vm: vms){
            if(vm.getState() == State.IDLE)
                results.addResult(vm.execute(requests.pop()));
            vm.setState(State.IDLE);
        }
        if (!requests.empty())
            this.execute(requests);

        results.calculateStartingPoint();
        results.calculateFailedRequests();
        return results;
    }

    public double getTotalEnergyUtilization(){
        // consumed Cpu = workloadRateCpu * MaxCpu
        return idleStateEnergyConsumption + workloadrateCpu *energyCpu + workloadrateMemory* energyMemory + workloadrateNetwork * energyNetwork;
    }

    public int getPmSize() {
        return vms.size();
    }

}
