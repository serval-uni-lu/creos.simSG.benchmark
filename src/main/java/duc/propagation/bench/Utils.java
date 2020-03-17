package duc.propagation.bench;

import duc.aintea.sg.Fuse;

import java.util.Random;

public class Utils {

    public static void makeRandFuseUc(Fuse[] init, int nb, Random random) {
        var chosen = new boolean[init.length];

        for (int i = 0; i < nb; i++) {
            int randIdx;
            while (true) {
                randIdx = random.nextInt(init.length);
                if (!chosen[randIdx]) {
                    chosen[randIdx] = true;
                    break;
                }
            }
            init[randIdx].getStatus().setConfAsProb(random.nextDouble() * 0.9);
        }

    }


}
