import java.util.*;
import java.util.stream.*;

public class DeterminizationTree {
/** 
 * This class reperesents the tree of the determinization procces,
 * converting from NBA to DNA
 * 
 */
	
	private Buchi buchi;
	private List<TreeNode> nodelist;
	private int number;
	private Map<String, Integer> lastUpdated;
	private int currentIteration;
	private int cutoffDepth;
	
	/**
	 * Constructor for Tree.
	 * 
	 * ***NOTE*** Is only mathematicaly correct for:
	 * 	cutoffDepth == buchi.states.length();
	 * 
	 * @param buchi - the NBA to be determinized
	 */
	public DeterminizationTree(Buchi buchi, int cutoffDepth){
        this.cutoffDepth = cutoffDepth;
		this.buchi = buchi;
		this.currentIteration = 0;
		this.nodelist = new LinkedList<TreeNode>();
		this.lastUpdated = new HashMap<>();
		Map<String, BuchiState> initialStates = new HashMap<>();
		for(Map.Entry<String, BuchiState> stateEntry : buchi.states.entrySet()) {
            BuchiState s = stateEntry.getValue();
			lastUpdated.put(s.id, 0);
			if(s.isInitial){
				initialStates.put(s.id, s);
			}
		}
		nodelist.add(new TreeNode(initialStates, null));
	}
	
	/**
	 * Does one step on the tree, with char s.
	 * @param s - the input char
	 * @return The new tree after the step
	 */
	public DeterminizationTree doStep(char s, boolean makeChildren){
		this.currentIteration++;
		DeterminizationTree toRet = this.deepCopy();
		if(toRet.nodelist.isEmpty()) {
            toRet.number = 0;
            return toRet;
        }
        toRet.number = nodelist.size()*2;
		toRet.doRecursiveStep(s, toRet.nodelist.iterator().next(), makeChildren);
		return toRet;
	}
	//implementation of doStep:
	private boolean doRecursiveStep(char s, TreeNode t, boolean makeChildren){
		//do recursivly on children:
		if(!t.children.isEmpty()){
			ListIterator<TreeNode> iter = t.children.listIterator();
			while(iter.hasNext()){
				TreeNode next = iter.next();
				if(doRecursiveStep(s, next, makeChildren) == false){
					int newNumber = 2*this.nodelist.indexOf(next); // return false
					this.number = (number < newNumber ? number : newNumber);
					this.nodelist.remove(next);
					iter.remove();
				}
			}
		}
		Map<String, BuchiState> accStates = new LinkedHashMap<>();
		Map<String, BuchiState> otherStates = new LinkedHashMap<>();
        for(BuchiState currState : t.states.values()) {
            Set<String> nextStatesID = currState.transitions.get(s);
            if (nextStatesID == null) {
                // There are no transitions with the character s
                continue;
            }
            for(String id : nextStatesID){
                BuchiState state = buchi.states.get(id);
                if(lastUpdated.get(state.id) < this.currentIteration){
                    lastUpdated.put(state.id, this.currentIteration);
                    if(state.isFinal){
                        accStates.put(state.id, state);
                    } else {
                        otherStates.put(state.id, state);
                    }
                }
            }
        }

        t.states = otherStates;
        t.acceptedstates.putAll(accStates);
		if(t.acceptedstates.isEmpty()){
			if(t.states.isEmpty() && t.children.isEmpty()){
				return false; //kill this node
			} else {
				// reached accepting state:
				t.states = getSubtreeStatesAndDelete(t);
				t.states.putAll(t.acceptedstates);
				int newNumber = 2*this.nodelist.indexOf(t)+1; // return true
				this.number = (number < newNumber ? number : newNumber);
				t.acceptedstates = new HashMap<>();
			}
		} else {
			if(!t.acceptedstates.isEmpty() && this.nodelist.size() < this.cutoffDepth
					&& makeChildren){
				// Add new child with all states that accepted:
				TreeNode newChild = new TreeNode(t.acceptedstates, t);
				t.children.add(newChild);
				this.nodelist.add(newChild);
				t.acceptedstates = new HashMap<>();
			} else {
				t.acceptedstates.putAll(accStates);
			}
		}
        return true;
	}
	// returns all states in this subtree
	private Map<String, BuchiState> getSubtreeStatesAndDelete(TreeNode node){
		Map<String, BuchiState> toRet = node.states;
		toRet.putAll(node.acceptedstates);
		TreeNode child;
		ListIterator<TreeNode> iter = node.children.listIterator();
		while(iter.hasNext()){
			// remove all children:
			TreeNode next = iter.next();
			toRet.putAll(getSubtreeStatesAndDelete(next));
			this.nodelist.remove(next);
			iter.remove();
		}
        return toRet;
	}
	
	/**
	 * Returns the number from the last step
	 */
	public int getNumber(){
		return this.number;
	}

	/**
	 * Returns the array representing the current state
	 * @return Array representing tree shape
	 */
	public int[] getTreeArray(){
		int[] toRet = new int[this.buchi.states.size()-1];
		for(int i = 0; i < this.nodelist.size()-1; i++){
            int index = this.nodelist.indexOf(this.nodelist.get(i).parent); 
            if (index < 0) {
                index = 0;
            }
			toRet[i] = index;
		}
        return toRet;
	}
	
	public String getStateMappingString(){
        String toRet = "[";
        for (String id : buchi.states.keySet()) {
            int index = 0;
            boolean wasFound = false;
            for (TreeNode node : nodelist) {
                if (node.states.containsKey(id) || node.acceptedstates.containsKey(id)) {
                    toRet += index + " ";
                    wasFound = true;
                    break;
                }
                index++;
            }
            if (!wasFound) {
                toRet += "$ ";
            }
        }
        return toRet.substring(0, toRet.length()-1) + "]";
	}
	
	public Set<Character> getAlphabet(){
		return buchi.getAlphabet();
	}
	
	private DeterminizationTree deepCopy(){
		DeterminizationTree toRet = new DeterminizationTree(this.buchi, this.cutoffDepth);
        toRet.buchi = this.buchi; // TODO Is buchi ever changed? Does it need to be deep copied as well?
		toRet.currentIteration = this.currentIteration;
		toRet.lastUpdated = new HashMap<>();
		toRet.lastUpdated.putAll(this.lastUpdated);
		toRet.nodelist = new LinkedList<>();
		this.nodelist.get(0).copyIntoList(null, toRet.nodelist);
		return toRet;
	}
	
	private class TreeNode{
		public Map<String, BuchiState> states; //represents states of current node
		public Map<String,BuchiState> acceptedstates;
		public List<TreeNode> children;
		public TreeNode parent;
		
		public TreeNode(Map<String, BuchiState> states, TreeNode parent){
			this.states = states;
			this.acceptedstates = new HashMap<>();
			this.parent = parent;
			this.children = new LinkedList<>();
		}
		
		public TreeNode copyIntoList(TreeNode parent, List<TreeNode> nodelist){
			TreeNode toRet = new TreeNode(this.states, parent);
			nodelist.add(toRet);
			for(TreeNode child : this.children){
				toRet.children.add(child.copyIntoList(toRet, nodelist));
			}
            return toRet;
		}

        public String toString() {
            return "parent: " + parent + ", states: " + states;
        }
	}
}
