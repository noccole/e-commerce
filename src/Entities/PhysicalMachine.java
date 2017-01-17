package Entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Created by Nicole on 9/11/16.
 */
public class PhysicalMachine {
    private List<VirtualMachine> vms;
    private State state;
    private int memory;
    private int cpu;
    private int network;

    private int energyCpu;
    private int energyMemory;
    private int energyNetwork;

    private int phsyicalmaschinefails;
    private double workloadrateCpu;            // in percent
    private double workloadrateMemory;
    private double workloadrateNetwork;
    private BooleanGenerator generator;
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
        workloadrateCpu = Math.random();//0.5;
        workloadrateMemory = Math.random();//0.4;                   //Math.random() generates double value between 0.0 and 1.0
        workloadrateNetwork = Math.random();//0.1;
        state = State.NEW;
        this.phsyicalmaschinefails =0;
        results = new ResultList();
        createVms(numVms);
    }
    private void createVms(int numVms){
        for(int i =0; i<numVms; i++) {
            vms.add(createVmWithNormallyDistributedVariables(numVms));
        }
    }
    //method for normal distributing the variables: memory, cpu, startTime and duration
    public VirtualMachine createVmWithNormallyDistributedVariables(int numVms){
        Random r = new Random();
        int memoryVm = (int) Math.floor(r.nextGaussian()*100+(memory/numVms));               //1 GByte mean
        int cpuVm = (int) Math.floor(r.nextGaussian()*400+(cpu/numVms));                   // 500 MByte mean
        int networkVm = (int) Math.floor(r.nextGaussian()*20+(network/numVms));       // 100 MByte mean

        //depends linearly on the combination of the utilized memory, CPU and network bandwidth)
        double pageDirtyingRate = (memoryVm/memory)+(cpuVm/cpu)+(networkVm/network);
        return new VirtualMachine(memoryVm, cpuVm, networkVm, pageDirtyingRate);
    }
    public ResultList distributeWorkload(Request request){
        if(generator.generateBoolean(0.80)) {           //let fail pm randomly
            this.execute(request);
            return results;
        }else{
            this.phsyicalmaschinefails ++;
            this.failPm();
            return null;
        }

    }
    private void execute(Request request){
        this.state = State.IDLE;
        for (VirtualMachine vm : vms) {
            if (vm.getState() != State.FAILED) {
                Request resultRequest = vm.distributeWorkload(request);
                if(resultRequest != null) {
                    results.addRequest(resultRequest);   //write all Pm Results to results;
                    return;
                }else {                                         //vm fails
                    this.execute(request);         //distribute to other vm
                    return;
                }
            }else{
                vm.restartVm();
            }
        }
    }

    public int getPhsyicalmaschinefails(){
        return this.phsyicalmaschinefails;
    }
    public double getTotalEnergyUtilization(){
        // consumed Cpu = workloadRateCpu * MaxCpu
        return idleStateEnergyConsumption + workloadrateCpu *energyCpu + workloadrateMemory* energyMemory + workloadrateNetwork * energyNetwork;
    }
    public List<VirtualMachine> getVms2(){
        return this.vms;
    }

    public int getPmSize() {
        return vms.size();
    }

    public int getIdleStateEnergyConsumption() {
        return idleStateEnergyConsumption;
    }
    public State getState() {
        return state;
    }

    public void restartPm(){
        this.state = State.IDLE;
    }
    public void failPm(){
        this.state = State.FAILED;
    }
}
