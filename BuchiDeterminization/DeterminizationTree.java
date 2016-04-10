import java.util.*;

public class DeterminizationTree {
/** 
 * This class reperesents the tree of the determinization procces,
 * converting from NBA to DNA
 * 
 */
	
	private Buchi buchi;
	private List<TreeNode> nodelist;
	private int number;
	private Map<BuchiState, Integer> lastUpdated; // TODO can this map ids instead of states?
	private int currentIteration;
	//TODO add other relevant fields
	
	/**
	 * Constructor for Tree.
	 * @param buchi - the NBA to be determinized
	 */
	public DeterminizationTree(Buchi buchi){
		this.currentIteration = 0;
		this.nodelist = new LinkedList<TreeNode>();
		this.lastUpdated = new HashMap<BuchiState, Integer>();
		Set<BuchiState> initialStates = new HashSet<BuchiState>();
		for(Map.Entry<String, BuchiState> stateEntry : buchi.states.entrySet()) {
            BuchiState s = stateEntry.getValue();
			lastUpdated.put(s, 0);
			if(s.isInitial){
				initialStates.add(s);
			}
		}
		nodelist.add(new TreeNode(initialStates, null));
	}
	
	/**
	 * Does one step on the tree, with char s.
	 * @param s - the input char
	 * @return number for numbered automata
	 */
	public int doStep(char s){
		// if we killed all paths, return 0 (lowest false)
		this.currentIteration++;
		if(nodelist.isEmpty()){ return 0; }
		doRecursiveStep(s, nodelist.iterator().next());
		return this.number;
	}
	//implementation of doStep:
	private boolean doRecursiveStep(char s, TreeNode t){
		//do recursivly on children:
		if(!t.children.isEmpty()){
			ListIterator<TreeNode> iter = t.children.listIterator();
			while(iter.hasNext()){
				TreeNode next = iter.next();
				if(doRecursiveStep(s, next) == false){
					int newNumber = 2*this.nodelist.indexOf(next); // return false
					this.number = (number < newNumber ? number : newNumber);
					this.nodelist.remove(next);
					iter.remove();
				}
			}
		}
		Set<BuchiState> accStates = new LinkedHashSet<BuchiState>();
		Set<BuchiState> otherStates = new LinkedHashSet<BuchiState>();
        for (TreeNode node : t.children) { // TODO is this really what we want?
            for(BuchiState currState : node.states) {
                for(String id : currState.transitions.get(s)){
                    BuchiState state = buchi.states.get(id);
                    if(lastUpdated.get(state) < this.currentIteration){
                        lastUpdated.put(state, this.currentIteration);
                        if(state.isFinal){
                            accStates.add(state);
                        } else {
                            otherStates.add(state);
                        }
                    }
                }
            }
        }

        t.states = otherStates;
		if(otherStates.isEmpty()){
			if(accStates.isEmpty() && t.children.isEmpty()){
				return false; //kill this node
			} else {
				// reached accepting state:
				t.states.addAll(accStates);
				TreeNode next;
				ListIterator<TreeNode> iter = t.children.listIterator();
				while(iter.hasNext()){
					// remove all children:
					next = iter.next();
					t.states.addAll(next.states);
					this.nodelist.remove(next);
					iter.remove();
				}
				int newNumber = 2*this.nodelist.indexOf(t)+1; // return true
				this.number = (number < newNumber ? number : newNumber);
			}
		} else {
			if(!accStates.isEmpty()){
				// Add new child with all states that accepted:
				TreeNode newChild = new TreeNode(accStates, t);
				t.children.add(newChild);
				this.nodelist.add(newChild);
			}
		}
        return true;
	}

	/**
	 * Returns the array representing the current state
	 * @return Array representing tree shape
	 */
	public int[] getTreeArray(){
		int[] toRet = new int[this.nodelist.size()];
		for(int i = 0; i < this.nodelist.size(); i++){
			toRet[i] = this.nodelist.indexOf(this.nodelist.get(i).parent);
		}
        return toRet;
	}
	
	/**
	 * Returns an array that represents the node that each
	 * state belongs to.
	 * @return Array of the numbers of the states
	 */
	public int[] getStateNodesArray(){
		//TODO
        return new int[0];
	}
	
	private class TreeNode{
		public Set<BuchiState> states; //represents states of current node
		public List<TreeNode> children;
		public TreeNode parent;
		
		public TreeNode(Set<BuchiState> states, TreeNode parent){
			this.states = states;
			this.parent = parent;
		}
		
		//TODO overide equals and hash
	}
}
