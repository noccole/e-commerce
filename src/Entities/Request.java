package Entities;

import java.util.Random;

/**
 * Created by Nicole on 8/12/16.
 */
public class Request {

    private Location location;
    private State state;
    private int startTime;
    private int duration; //milliseconds, max 5 ms
    private int ressources; //anzahl ressources die der request braucht (CPUs)
    private Random r = new Random();

    public Request(int startTime, int duration, Location location, int ressources){
        state = State.NEW;
        this.startTime = startTime;
        this.duration = duration;
        this.location = location;
        this.ressources = ressources;
    }
    public Request execute(){

        if(r.nextBoolean())
            state= State.SUCCESS;
        else
            state = State.FAILED;

        return this;
    }

    public int energyConsumption(){
        return ressources * duration;
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

    public int getRessources() {
        return ressources;
    }

    public void setRessources(int ressources) {
        this.ressources = ressources;
    }

    public Location getLocation() {
        return location;
    }
    public boolean getSuccess(){
        if(state == State.SUCCESS)
            return true;

       return false;
    }

}
