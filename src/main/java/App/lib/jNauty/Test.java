package App.lib.jNauty;

import App.Utils.DemoDataCreator;

import java.util.List;

/**
 * Created by Keinan.Gilad on 10/20/2016.
 */
public class Test {

    public static void main(String[] args) {
        McKayGraphLabelingAlgorithm algo = new McKayGraphLabelingAlgorithm(DemoDataCreator.generateGraphSymmetry());
        List<Permutation> automorphisms = algo.findAutomorphisms();

        // take the last permutation
        Permutation p = automorphisms.get(automorphisms.size() - 1);

        // print orbits
        System.out.println(p.orbit(0));
        System.out.println(p.orbit(1));
        System.out.println(p.orbit(2));
        System.out.println(p.orbit(3));
        System.out.println(p.orbit(4));
        System.out.println(p.orbit(5));
        System.out.println(p.orbit(6));
        System.out.println(p.orbit(7));
    }
}
