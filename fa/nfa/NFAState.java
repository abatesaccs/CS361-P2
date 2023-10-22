package fa.nfa;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import fa.State;

public class NFAState extends State{
    private Map<Character, Set<NFAState>> transitions;

    public NFAState(String name) {
        super(name);
        transitions = new HashMap<>();
    }

    public void addTransition(char onSymb, NFAState toState) {
        Set<NFAState> temp = transitions.get(onSymb);
        if(temp != null && !temp.contains(toState)) {
            temp.add(toState);
        }
    }

    public Set<NFAState> getTransition(char onSymb) {
        return transitions.get(onSymb);
    }
}
