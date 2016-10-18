package App.Utils;

import App.Model.ComponentCode;
import App.Model.Edge;

import java.util.List;

/**
 * Created by Keinan.Gilad on 10/16/2016.
 */
public class DFSCodeUtil {
    /**
     * @param ci
     * @param cj
     * @return 0 - equal, -1 if ci < cj, 1 if cj< ci
     */

    public static int compare(ComponentCode ci, ComponentCode cj) {
        List<Edge> ciEdges = ci.getCode();
        List<Edge> cjEdges = cj.getCode();
        int edgesSize = ciEdges.size();
        for (int i = 0; i < edgesSize; i++) {
            Edge ei = ciEdges.get(i);
            Edge ej = cjEdges.get(i);

            int compareEdges = compare(ei, ej);
            if (compareEdges == 0) {
                continue;
            } else {
                return compareEdges;
            }
        }
        return 0; // equal
    }

    /*
    e < e':
    (1) when both e and e' are forward edges, j < j' or (i > i' ^ j = j');
    (2) when both e and e' are backward edges (i.e., edges not in DFS-tree T ), i < i' or (i = i' ^ j < j');
    (3) when e is a forward edge and e' is a backward edge, j <= i';
    (4) when e is a backward edge and e' is a forward edge, i < j'.
     */
    private static int compare(Edge ei, Edge ej) {
        boolean isEiForward = ei.isForward();
        boolean isEjForward = ej.isForward();
        int j = Integer.valueOf(ei.getV1().getName());
        int jTag = Integer.valueOf(ej.getV1().getName());

        int i = Integer.valueOf(ei.getV0().getName());
        int iTag = Integer.valueOf(ej.getV0().getName());

        if (isEiForward && isEjForward) {
            if (j < jTag || (j == jTag && i > iTag)) {
                return -1;
            } else if (jTag < j || (j == jTag && iTag > i)) {
                return 1;
            }
            return 0;

        } else if (!isEiForward && !isEjForward) {
            if (i < iTag || (i == iTag && j < jTag)) {
                return -1;
            } else if (iTag < i || (i == iTag && jTag < j)) {
                return 1;
            }
            return 0;
        } else if (isEiForward && !isEjForward) {
            if (j <= iTag) {
                return -1;
            }

        } else if (!isEiForward && isEjForward) {
            if (i <= jTag) {
                return -1;
            }
        }
        return 0;
    }

    /**
     * calculating minimum DFS code and set the code inside.
     * @param componentCode - a component graph
     */
    public static void calculateMinimumCode(ComponentCode componentCode) {
        // TODO - calculating minimum DFS code
    }
}
