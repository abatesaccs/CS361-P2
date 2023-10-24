package fa.nfa;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Stack;

import fa.State;

/**
 * Model of a Non-Deterministic Finite Automata
 * Date: Sun 22 Oct 2023
 * @author Christian Galvan, Andrew Bates
 */
public class NFA implements NFAInterface{
    private Set<NFAState> Q; // set of states
    private Set<Character> Sigma; // alphabet
    private NFAState q0; // start state
    private Set<NFAState> F; // set of final states

    /**
     * Default constructor for the non-deterministic finite automata
     * initializes the set of states (Q), the alphabet (Sigma), 
     * and the set of final states (F) as empty sets.
     */
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
        Set<NFAState> current = eClosure(q0);

        // iterate each character in string
        for (char c : s.toCharArray()) {
            Set<NFAState> next = new LinkedHashSet<>();
            
            // iterate current states and get next states from transitions
            for (NFAState state : current) {
                Set<NFAState> transitionalStates = state.toStates(c);
                
                if (transitionalStates != null) {
                    next.addAll(transitionalStates);
                }
            }

            current.clear();
            
            // get eclosure for each next state
            for (NFAState nextState : next) {
                current.addAll(eClosure(nextState));
            }
        }

        // check if any are final states
        for (NFAState state : current) {
            if (F.contains(state)) {
                return true;  // string is accepted
            }
        }

        return false;  // string is not accepted
    }


    @Override
    public Set<Character> getSigma() {
        return Sigma;
    }

    @Override
    public NFAState getState(String name) {
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
        return from.toStates(onSymb);
    }

    @Override
    public Set<NFAState> eClosure(NFAState s) {
        Stack<NFAState> stack = new Stack<>();
        Set<NFAState> eClosure = new LinkedHashSet<>();
        stack.push(s);
        
        // dfs search for eclosure of a state
        while(!stack.isEmpty()) {
            NFAState current = stack.pop();
            eClosure.add(current);
            Set<NFAState> states = current.toStates('e');
            
            if(states != null){
            	// push states that are connected onto stack
                for (NFAState nfaState : states) {
                	if(!eClosure.contains(nfaState)) {
                		stack.push(nfaState);
                	} 
                }
            }
        }
        return eClosure;
    }

    @Override
    public int maxCopies(String s) {
        int maxCopies = 0;

        Set<NFAState> current = eClosure(q0);
        maxCopies = Math.max(maxCopies, current.size());

        // traverse string and get number of copies
        for (char c : s.toCharArray()) {
            Set<NFAState> next = new LinkedHashSet<>();

            // iterate current states and get next states
            for (NFAState state : current) {
                Set<NFAState> transitionalStates = state.toStates(c);
                if (transitionalStates != null) {
                    next.addAll(transitionalStates);
                }
            }

            current.clear();
            
            // get eclosure for next states
            for (NFAState nextState : next) {
                current.addAll(eClosure(nextState));
            }

            // update our maxCopies
            maxCopies = Math.max(maxCopies, current.size());
        }

        return maxCopies;
    }

    @Override
    public boolean addTransition(String fromState, Set<String> toStates, char onSymb) {
        NFAState from = (NFAState)getState(fromState);
        boolean added = false;
        for (String state : toStates) {
            NFAState temp = (NFAState)getState(state);
            
            if(from != null && temp != null && (Sigma.contains(onSymb) || onSymb == 'e')) {
                from.addTransition(onSymb, temp);
                added = true;
            }
        }
        return added;
    }

    @Override
    public boolean isDFA() {
        for (NFAState state : Q) {  // for every state in the NFA
            for (char symbol : Sigma) {  // for every symbol in the alphabet
                Set<NFAState> transitionalStates = state.toStates(symbol);
                if (transitionalStates == null || transitionalStates.size() != 1) {  // if there is no transition or more than one transition for a given symbol
                    return false;
                }
            }
            if (state.toStates('e') != null) {  // if there is an epsilon transition
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Q = { ");
        for (NFAState state : Q) {
            sb.append(state.getName()).append(" ");
        }
        sb.append("}\nSigma = { ");
        for (char c : Sigma) {
            sb.append(c).append(" ");
        }
        sb.append("}\ndelta =\n");
        for (char c : Sigma) {
            sb.append("\t").append(c);
        }
        sb.append("\n");
        for (NFAState state : Q) {
            sb.append(state.getName());
            for (char c : Sigma) {
                Set<NFAState> transitionalStates = state.toStates(c);
                sb.append("\t");
                if (transitionalStates != null) {
                    sb.append("{ ");
                    for (NFAState toState : transitionalStates) {
                        sb.append(toState.getName()).append(" ");
                    }
                    sb.append("}");
                } else {
                    sb.append("{}");
                }
            }
            sb.append("\n");
        }
        sb.append("q0 = ");
        if (q0 != null) {
            sb.append(q0.getName());
        } else {
            sb.append("undefined");
        }
        sb.append("\n");
        for (NFAState state : F) {
            sb.append(state.getName()).append(" ");
        }
        sb.append("}");
        return sb.toString();
    }

}
