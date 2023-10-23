package fa.nfa;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import fa.State;

/**
 * NFAState object that can specify a set of transitions that a state can take
 * Date: Sun 22 Oct 2023
 * @author Christian Galvan, Andrew Bates
 */
public class NFAState extends State{
    private Map<Character, Set<NFAState>> transitions;

    public NFAState(String name) {
        super(name);
        transitions = new HashMap<>();
    }

    public void addTransition(char onSymb, NFAState toState) {
        Set<NFAState> temp = transitions.get(onSymb);
        if (temp == null) {
            temp = new LinkedHashSet<>();
            transitions.put(onSymb, temp);
        }
        if (!temp.contains(toState)) {
            temp.add(toState);
        }
    }

    public Set<NFAState> getTransition(char onSymb) {
        return transitions.get(onSymb);
    }

    public Set<NFAState> toStates(char onSymb) {
        Set<NFAState> states = transitions.get(onSymb);
        if (states == null) {
            return new LinkedHashSet<>();
        }
        return states;
    }

}
