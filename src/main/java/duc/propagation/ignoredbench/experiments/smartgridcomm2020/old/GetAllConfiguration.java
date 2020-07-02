package duc.propagation.ignoredbench.experiments.smartgridcomm2020.old;

import duc.sg.java.loadapproximator.uncertain.bsrules.ConfigurationMatrix;
import duc.sg.java.loadapproximator.uncertain.bsrules.EmptyConfigurationMatrix;
import duc.sg.java.model.Cable;
import duc.sg.java.model.Fuse;
import duc.sg.java.model.State;
import duc.sg.java.model.Substation;
import duc.sg.java.utils.BaseTransform;

import java.util.*;

public class GetAllConfiguration {
    private GetAllConfiguration(){}

    private static List<Fuse> getUncertainFuses(Fuse[] fuses) {
        var uFuses = new ArrayList<Fuse>();
        for (Fuse f: fuses) {
            if(!f.getOwner().isDeadEnd() && f.getStatus().isUncertain()) {
                uFuses.add(f);
            }
        }

        return uFuses;
    }

    private static Map<Fuse, State> boolArr2MapFuseState(boolean[] fuseStates, List<Fuse> uFuses, Collection<Fuse> allFuses) {
        var res = new HashMap<Fuse, State>(allFuses.size());

        for(Fuse f: allFuses) {
            State state;
            if(uFuses.contains(f)) {
                int idx = uFuses.indexOf(f);
                state = fuseStates[idx]? State.CLOSED : State.OPEN;
            } else {
                state = f.getStatus().isClosed()? State.CLOSED : State.OPEN;
            }
            res.put(f, state);
        }

        return res;
    }

    public static ConfigurationMatrix getAllConfiguration(Substation substation) {
        substation.updateAllFuses();

        Collection<Fuse> allFuses = substation.getAllFuses();
        List<Fuse> uFuses = getUncertainFuses(allFuses.toArray(new Fuse[0]));

        int nbPossibilities = (int) Math.pow(2, uFuses.size());

        if(nbPossibilities == 1) {
            return new EmptyConfigurationMatrix();
        }

        var res = new ConfigurationMatrix(uFuses.toArray(new Fuse[0]));

        double confToAdd = 0;

        for (int idxConf = 0; idxConf < nbPossibilities; idxConf++) {
            boolean[] fuseStates = BaseTransform.toBinary(idxConf, uFuses.size());
            Map<Fuse, State> fuseStateMap = boolArr2MapFuseState(fuseStates, uFuses, allFuses);

            double confidence = 1;
            for(Map.Entry<Fuse, State> fuseState: fuseStateMap.entrySet()) {
                if(fuseState.getValue() == State.CLOSED) {
                    confidence *= fuseState.getKey().getStatus().confIsClosed();
                } else {
                    confidence *= fuseState.getKey().getStatus().confIsOpen();
                }
            }

            if(isValid(substation, fuseStateMap)) {
                res.add(fuseStateMap, 0);
            } else {
                confToAdd += confidence;
            }

        }

        if(confToAdd > 0) {
            res.addConfToMaxOpen(confToAdd); //this will result in wrong result but it's enough for the XP
        }


        return res;


    }

    private static boolean isValid(Substation substation, Map<Fuse, State> fuseStateMap) {
        var waiting = new Stack<Fuse>();
        var inWaitingList = new HashSet<Fuse>(); //real optimization ??
        var visited = new HashSet<Fuse>();

        var cableVisited = new HashSet<Cable>();

        for (Fuse f: substation.getFuses()) {
            State state = fuseStateMap.get(f);
            if(state == State.CLOSED) {
                waiting.push(f);
            }
        }

         while (!waiting.isEmpty()) {
            var current = waiting.pop();
            visited.add(current);
            cableVisited.add(current.getCable());

            var opp = current.getOpposite();
            if(fuseStateMap.get(opp).equals(State.CLOSED)) {
                var ownerOpp = opp.getOwner();
                for (var f : ownerOpp.getFuses()) {
                    if (!visited.contains(f) && !inWaitingList.contains(f) && fuseStateMap.get(f).equals(State.CLOSED)) {
                        waiting.add(f);
                        inWaitingList.add(f);
                    }
                }
            }
        }

        int totalCables = substation.getAllFuses().size() / 2;
        return cableVisited.size() == totalCables;
    }

}
