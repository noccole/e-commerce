package Entities;

import java.util.Random;

/**
 * Created by Nicole on 9/11/16.
 */
public class VirtualMachine {

    State state;
    private boolean hasFailed;
    //all sizes in MB bzw. MB/s
    private int memory;
    private int cpu;
    private int network;
    boolean failure;

    private double consumedMemory;
    private double consumedCPU;
    private double consumedNetworkBandwidth;       //depends on the consumed memory
    private double pageDirtyingRate;            //depends linearly on the combination of the utilized memory, CPU and network bandwidth)

    public VirtualMachine(int memory, int cpu, int network){
            state = State.IDLE;
            this.memory = memory;
            this.cpu = cpu;
            this.network = network;
    }

    public Result execute(Request request){        //add param request
        state = state.PROCESSING;
        return request.execute();
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}