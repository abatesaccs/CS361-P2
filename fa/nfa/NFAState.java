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

    /**
     * constructs a NFAState object with a name
     * @param name, the name of the state
     */
    public NFAState(String name) {
        super(name);
        transitions = new HashMap<>();
    }

    /**
     * Adds a transition from this state to another state
     * @param onSymb, the symbol on which the transition occurs
     * @param toState, the state where transition leads
     */
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

    /**
     * get the set of states where this state goes to
     * if there is no transition on the symbol, an empty set is returned
     * @param onSymb, The symbol on which the transition occurs.
     * @return The set of states reached by the transition.
     */
    public Set<NFAState> toStates(char onSymb) {
        Set<NFAState> states = transitions.get(onSymb);
        if (states == null) {
            return new LinkedHashSet<>();
        }
        return states;
    }

}
