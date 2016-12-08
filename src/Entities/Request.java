package Entities;

import java.util.Random;

/**
 * Created by Nicole on 8/12/16.
 */
public class Request {
    private int startTime;
    private int duration;           //milliseconds, max 5 ms


    public Request(){
        distributeVariablesUniformly();
    }
    public Result execute(){
        return new Result(this, false);
    }

    //method for uniformly distributing the variables: memory, cpu, startTime and duration
    public void distributeVariablesUniformly(){
        startTime = (int)(Math.random()*1481213984);                 //date of 8.12.2016 as mean value
        duration = (int)(Math.random()*7);                              // 5 is max for sla

    }
}
