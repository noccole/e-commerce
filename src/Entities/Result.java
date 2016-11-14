package Entities;

/**
 * Created by Nicole on 14/11/16.
 */
public class Result {
    private boolean success;
    //private HttpRequest request;

    public Result(boolean success){
        this.success = success;
        //this. request = request;
    }
    public boolean getSuccess(){
        return success;
    }
}
