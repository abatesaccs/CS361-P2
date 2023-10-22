package fa.nfa;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Stack;

import fa.State;

public class NFA implements NFAInterface{
    private Set<NFAState> Q; // set of states
    private Set<Character> Sigma; // alphabet
    private NFAState q0; // start state
    private Set<NFAState> F; // set of final states

    public NFA() {
        this.Q = new LinkedHashSet<>();
        this.Sigma = new LinkedHashSet<>();
        this.F = new LinkedHashSet<>();
    }

    @Override
    public boolean addState(String name) {
        // Check if a state with the given name already exista
        for(NFAState state : Q) {
            if(state.getName().equals(name)) {
                return false; // Name already exists
            }
        }
        // If passing, add the new state
        Q.add(new NFAState(name));
        return true;
    }

    @Override
    public boolean setFinal(String name) {
        NFAState state = (NFAState)getState(name); // Check for state
        if(state != null){
            F.add(state); // Add state
            return true;
        }
        return false;
    }

    @Override
    public boolean setStart(String name) {
        NFAState state = (NFAState)getState(name); // Check for state
        if(state != null) {
            q0 = state; // Set state
            return true;
        }
        return false;
    }

    @Override
    public void addSigma(char symbol) {
        Sigma.add(symbol);
    }

    @Override
    public boolean accepts(String s) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'accepts'");
    }

    @Override
    public Set<Character> getSigma() {
        return Sigma;
    }

    @Override
    public State getState(String name) {
        for (NFAState state : Q) {
            if (state.getName().equals(name)) return state;
        }
        return null;
    }

    @Override
    public boolean isFinal(String name) {
        NFAState state = (NFAState)getState(name);
        return F.contains(state);
    }

    @Override
    public boolean isStart(String name) {
        NFAState state = (NFAState)getState(name);
        return q0.equals(state);
    }

    @Override
    public Set<NFAState> getToState(NFAState from, char onSymb) {
        return from.getTransition(onSymb);
    }

    @Override
    // TODO: Untested
    public Set<NFAState> eClosure(NFAState s) {
        Stack<NFAState> stack = new Stack<>();
        Set<NFAState> closureStates = new LinkedHashSet<>();
        stack.push(s);
        while(!stack.isEmpty()) {
            NFAState current = stack.pop();
            closureStates.add(current);
            Set<NFAState> states = current.getTransition('e');
            if(states != null){
                for (NFAState nfaState : states) {
                    stack.push(nfaState);
                }
            }
        }
        return closureStates;
    }

    @Override
    public int maxCopies(String s) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'maxCopies'");
    }

    @Override
    public boolean addTransition(String fromState, Set<String> toStates, char onSymb) {
        NFAState from = (NFAState)getState(fromState); // get state from name
        for (String state : toStates) { // for each state in the set of toStates
            NFAState temp = (NFAState)getState(state); // get state from name
            // Make needed checks, allow epsilon transitions to be added w/o being in aplhabet
            if(from != null && temp != null && (Sigma.contains(onSymb) || onSymb == 'e')) {
                from.addTransition(onSymb, temp);
                return true; // added one or more states
            }
        }
        return false; // added no states
    }

    @Override
    public boolean isDFA() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isDFA'");
    }
    
    // toString method will need to be modified for NFA

    // public String toString() {
	    // StringBuilder sb = new StringBuilder();
	    // sb.append("Q = { ");
	    // for (NFAState state : Q) {
	    //     sb.append(state.getName()).append(" ");
	    // }
	    // sb.append("}\nSigma = { ");
	    // for (char c : Sigma) {
	    //     sb.append(c).append(" ");
	    // }
	    // sb.append("}\ndelta =\n");
	    // for (char c : Sigma) {
	    //     sb.append("\t").append(c);
	    // }
	    // sb.append("\n");
	    // for (NFAState state : Q) {
	    //     sb.append(state.getName());
	    //     for (char c : Sigma) {
	    //         sb.append("\t").append(state.getTransition(c).getName());
	    //     }
	    //     sb.append("\n");
	    // }
	    // sb.append("q0 = ").append(q0.getName()).append("\nF = { ");
	    // for (NFAState state : F) {
	    //     sb.append(state.getName()).append(" ");
	    // }
	    // sb.append("}");
	    // return sb.toString();
	// }
}
