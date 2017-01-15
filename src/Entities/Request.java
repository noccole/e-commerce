package Entities;

import java.util.Random;
import java.util.logging.Logger;

/**
 * Created by Nicole on 8/12/16.
 */
public class Request {

    private Location location;
    private State state;
    private int startTime;
    private int duration; //milliseconds, max 5 ms
    private int ressources; //anzahl ressources die der request braucht (CPUs)
    private BooleanGenerator generator;
    private static final Logger logger = Logger.getLogger( Request.class.getName() );

    public Request(int startTime, int duration, Location location, int ressources){
        this.state = State.IDLE;
        this.startTime = startTime;
        this.duration = duration;
        this.location = location;
        this.ressources = ressources;       //energy per request
    }
    public Request execute(){
        if(generator.generateBoolean(0.85))
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Request request = (Request) o;

        if (startTime != request.startTime) return false;
        if (duration != request.duration) return false;
        if (ressources != request.ressources) return false;
        if (location != request.location) return false;
        return state == request.state;
    }

    @Override
    public int hashCode() {
        int result = location != null ? location.hashCode() : 0;
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + startTime;
        result = 31 * result + duration;
        result = 31 * result + ressources;
        return result;
    }
}
