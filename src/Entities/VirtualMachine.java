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

    private double consumedMemory;
    private double consumedCPU;
    private double consumedNetworkBandwidth;       //depends on the consumed memory
    private double pageDirtyingRate;            //depends linearly on the combination of the utilized memory, CPU and network bandwidth)
    private double startTime;
    private double duration;

    //TODO: origin of the request

    public Result execute(){        //add param request
        distributeVariables();
        // if request failed
        Result result = new Result(false);  //add param request

        return result;
    }

    //method for uniformly distributing the variables: memory, cpu, startTime and duration
    public void distributeVariables(){
        Random r = new Random();
        startTime = r.nextGaussian()* 86400000 ;                            //24 h in milliseconds
        duration = r.nextGaussian()* 3600000 + 18000000;                    //5 h duration with 1 hour variance
        memory = (int) Math.floor(r.nextGaussian()*400+1000);               //1 GByte mean
        cpu = (int) Math.floor(r.nextGaussian()*400+500);                   // 500 MByte mean
        networkBandwidth = (int) Math.floor(r.nextGaussian()*20+100);       // 100 MByte mean
    }

}