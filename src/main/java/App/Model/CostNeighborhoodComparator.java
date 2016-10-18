package App.Model;

import java.util.Comparator;

/**
 * Created by Keinan.Gilad on 10/4/2016.
 */
public class CostNeighborhoodComparator implements Comparator<NeighborhoodContext> {
    @Override
    public int compare(NeighborhoodContext o1, NeighborhoodContext o2) {
        return o1.getCost().compareTo(o2.getCost()) * -1;
    }
}
