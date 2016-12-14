package Entities;

import java.util.Random;

/**
 * Created by Nicole on 9/11/16.
 */
public class VirtualMachine {

    private State state;
    //all sizes in MB bzw. MB/s
    private int memory;
    private int cpu;
    private int network;             //TODO: depends on the consumed memory! (does not depend on memory yet)
    private double pageDirtyingRate;

    private Random r= new Random();
    public VirtualMachine(int memory, int cpu, int network, double pageDirtyingRate){
            state = State.IDLE;
            this.memory = memory;
            this.cpu = cpu;
            this.network = network;
            this.pageDirtyingRate = pageDirtyingRate;
    }

    public Request distributeWorkload(Request request){
        if(r.nextBoolean()) {                   //check if vm fails
            return this.execute(request);
        }else{
            this.failVm();
            return null;
        }
    }
    private Request execute (Request request) {
        Request result = request.execute();
        if (!request.getSuccess())                      //check if request failed
            return this.execute(request);               //if request has failed, try again!

        return result;
    }

    public State getState() {
        return state;
    }
    public void restartVm(){
        this.state = State.IDLE;
    }
    public void failVm(){
        this.state = State.FAILED;
    }



}