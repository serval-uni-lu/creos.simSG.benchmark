package duc.propagation.external;

import duc.propagation.bench.RealCase3SubsHelper;
import duc.propagation.bench.Utils;
import duc.sg.java.loadapproximator.uncertain.naive.UncertainLoadApproximator;
import duc.sg.java.model.Fuse;
import duc.sg.java.model.Substation;

import java.util.Random;

public class RC3Sub2Uncertain {
    public static void main(String[] args) {
        Random random = new Random(12345);

        var consumptions = new double[8];
        for (int i = 0; i < consumptions.length; i++) {
            consumptions[i] = random.nextDouble() * 100;
        }

        Substation subs = RealCase3SubsHelper.getSubs("Substation 2", consumptions);
        Fuse[] fuses = subs.extractFuses().toArray(new Fuse[0]);
        Utils.makeRandFuseUc(fuses, fuses.length, random);


        for (int i = 0; i < 100; i++) {
            UncertainLoadApproximator.approximate(subs);
            System.out.println(i);
        }

    }

}
