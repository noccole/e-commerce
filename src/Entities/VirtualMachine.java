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

    private Random r;
    public VirtualMachine(int memory, int cpu, int network){
            state = State.IDLE;
            this.memory = memory;
            this.cpu = cpu;
            this.network = network;
    }

    public Request execute(Request request){        //add param request
        state = state.PROCESSING;
        Request result = request.execute();
        state = state.IDLE;
        return result;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}