package Entities;

import java.util.Random;
import java.util.logging.Logger;

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
    private static final Logger logger = Logger.getLogger( VirtualMachine.class.getName() );

    private BooleanGenerator generator;
    public VirtualMachine(int memory, int cpu, int network, double pageDirtyingRate){
            state = State.IDLE;
            this.memory = memory;
            this.cpu = cpu;
            this.network = network;
            this.pageDirtyingRate = pageDirtyingRate;
    }

    public Request distributeWorkload(Request request){
        if(generator.generateBoolean(0.85)) {                   //check if vm fails
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

        logger.info("Execute request on VM");
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