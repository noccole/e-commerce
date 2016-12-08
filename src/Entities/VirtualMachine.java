package Entities;

import java.util.Random;

/**
 * Created by Nicole on 9/11/16.
 */
public class VirtualMachine {

    private boolean hasFailed;
    //all sizes in MB bzw. MB/s
    private int memory;
    private int cpu;
    private int networkBandwidth;
    boolean failure;

    private double consumedMemory;
    private double consumedCPU;
    private double consumedNetworkBandwidth;       //depends on the consumed memory
    private double pageDirtyingRate;            //depends linearly on the combination of the utilized memory, CPU and network bandwidth)

    //TODO: origin of the request

    public Result execute(Request request){        //add param request
        distributeVariablesNormally();

        return request.execute();
    }

    //method for normal distributing the variables: memory, cpu, startTime and duration
    public void distributeVariablesNormally(){
        Random r = new Random();
        memory = (int) Math.floor(r.nextGaussian()*400+1000);               //1 GByte mean
        cpu = (int) Math.floor(r.nextGaussian()*400+500);                   // 500 MByte mean
        networkBandwidth = (int) Math.floor(r.nextGaussian()*20+100);       // 100 MByte mean
    }

}