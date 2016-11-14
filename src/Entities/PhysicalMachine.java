package Entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nicole on 9/11/16.
 */
public class PhysicalMachine {
    private List<VirtualMachine> vms;

    public PhysicalMachine(int numVms) {
        vms = new ArrayList<VirtualMachine>(numVms);
    }

    public int getPmSize() {
        return vms.size();
    }

    public ResultList distributeWorkload() {
        ResultList results = new ResultList();
        for(VirtualMachine vm: vms){
            results.addResult(vm.execute());
        }
        results.calculateStartingPoint();
        results.calculateFailedRequests();
        return results;
    }

}
