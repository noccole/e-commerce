package Entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nicole on 14/11/16.
 */
public class ResultList {
    private List<Result> results;
    private int startingPoint;
    private int numFailedRequests;

    public ResultList(){
        this.results = new ArrayList<Result>();
    }
    public void addResult(Result result){
        results.add(result);
    }
    public void calculateStartingPoint(){
        int i=1;
        for (Result result : results){
            if(!result.getSuccess()) {       //first Failure
                startingPoint = i;
                return;
            }
            i++;
        }
    }
    public void calculateFailedRequests(){
        for (Result result : results){
            if(!result.getSuccess())
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
