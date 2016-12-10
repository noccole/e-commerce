package Entities;

import java.util.Random;

/**
 * Created by Nicole on 8/12/16.
 */
public class Request {

    Location location;
    private int startTime;
    private int duration;           //milliseconds, max 5 ms


    public Request(int startTime, int duration){
        this.startTime = startTime;
        this.duration = duration;
    }
    public Result execute(){
        return new Result(this, false);
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Location getLocation() {
        return location;
    }

}
