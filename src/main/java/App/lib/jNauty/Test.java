package App.lib.jNauty;

import App.Common.Utils.DemoDataCreator;

import java.util.List;

/**
 * Created by Keinan.Gilad on 10/20/2016.
 */
public class Test {

    public static void main(String[] args) {
        McKayGraphLabelingAlgorithm algo = new McKayGraphLabelingAlgorithm();
        List<Permutation> automorphisms = algo.findAutomorphisms(DemoDataCreator.generateGraphSymmetry());

        // take the last permutation
        Permutation p = automorphisms.get(automorphisms.size() - 1);

        // print orbits
        System.out.println("orbits 0-7:");
        System.out.println(p.orbit(0));
        System.out.println(p.orbit(1));
        System.out.println(p.orbit(2));
        System.out.println(p.orbit(3));
        System.out.println(p.orbit(4));
        System.out.println(p.orbit(5));
        System.out.println(p.orbit(6));
        System.out.println(p.orbit(7));

        System.out.println("cyclicRepresenatation:");
        System.out.println(p.cyclicRepresenatation());
    }
}
