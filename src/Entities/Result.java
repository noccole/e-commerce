package Entities;

/**
 * Created by Nicole on 14/11/16.
 */
public class Result {
    private boolean failure;
    private Request request;

    public Result(Request request, boolean failure){
        this.failure = failure;
        this.request = request;
    }
    public boolean getSuccess(){
        return failure;
    }
}
