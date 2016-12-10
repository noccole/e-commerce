package Entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nicole on 14/11/16.
 */
public class ResultList {
    private List<Request> results;
    private int startingPoint;
    private int numFailedRequests;

    public ResultList(){
        this.results = new ArrayList<Request>();
    }
    public void addRequest(Request request){
        results.add(request);
    }

    public void calculateStartingPoint(){
        int i=1;
        for (Request request : results){
            if(!request.getSuccess()) {       //first Failure
                startingPoint = i;
                return;
            }
            i++;
        }
    }
    public void calculateFailedRequests(){
        for (Request request : results){
            if(!request.getSuccess())
                numFailedRequests++;
        }
    }
    public int getFailedRequests(){
        return numFailedRequests;
    }
    public int getStartingPoint(){
        return startingPoint;
    }
}
