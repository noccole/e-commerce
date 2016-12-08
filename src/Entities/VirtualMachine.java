package Entities;

/**
 * Created by Nicole on 9/11/16.
 */
public class VirtualMachine {

    private boolean hasFailed;
    private int size;               //what is size
    private double consumedMemory;
    private double consumedCPU;
    private double consumedNetworkBandwidth;       //depends on the consumed memory
    private double pageDirtyingRate; //depends linearly on the combination of the utilized memory, CPU and network bandwidth)

    public Result execute(){        //add param request


        // if request failed
        Result result = new Result(false);  //add param request

        return result;
    }
}