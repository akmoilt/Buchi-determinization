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
	private Map<BuchiState, Integer> lastUpdated; // TODO can this map ids instead of states?
	private int currentIteration;
	//TODO add other relevant fields
	
	/**
	 * Constructor for Tree.
	 * @param buchi - the NBA to be determinized
	 */
	public DeterminizationTree(Buchi buchi){
        this.buchi = buchi;
		this.currentIteration = 0;
		this.nodelist = new LinkedList<TreeNode>();
		this.lastUpdated = new HashMap<BuchiState, Integer>();
		Map<String, BuchiState> initialStates = new HashMap<>();
		for(Map.Entry<String, BuchiState> stateEntry : buchi.states.entrySet()) {
            BuchiState s = stateEntry.getValue();
			lastUpdated.put(s, 0);
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
	public DeterminizationTree doStep(char s){
		this.currentIteration++;
		DeterminizationTree toRet = this.deepCopy();
		if(nodelist.isEmpty()) {
            toRet.number = 0;
            return toRet;
        }
        toRet.number = buchi.states.size()*2 + 3;
		toRet.doRecursiveStep(s, toRet.nodelist.iterator().next());
		return toRet;
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
		Map<String, BuchiState> accStates = new LinkedHashMap<>();
		Map<String, BuchiState> otherStates = new LinkedHashMap<>();
        for(BuchiState currState : t.states.values()) {
            Set<String> nextStates = currState.transitions.get(s);
            if (nextStates == null) {
                // There are no transitions with the character s
                continue;
            }
            for(String id : nextStates){
                BuchiState state = buchi.states.get(id);
                if(lastUpdated.get(state) < this.currentIteration){
                    lastUpdated.put(state, this.currentIteration);
                    if(state.isFinal){
                        accStates.put(state.id, state);
                    } else {
                        otherStates.put(state.id, state);
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
				t.states.putAll(accStates);
				TreeNode next;
				ListIterator<TreeNode> iter = t.children.listIterator();
				while(iter.hasNext()){
					// remove all children:
					next = iter.next();
					t.states.putAll(next.states);
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
		int[] toRet = new int[this.nodelist.size()-1];
		for(int i = 0; i < this.nodelist.size()-1; i++){
			toRet[i] = this.nodelist.indexOf(this.nodelist.get(i+1).parent);
		}
        return toRet;
	}
	
	public String getStateMappingString(){
        String toRet = "[";
        for (String id : buchi.states.keySet()) {
            int index = 0;
            boolean wasFound = false;
            for (TreeNode node : nodelist) {
                if (node.states.containsKey(id)) {
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
		DeterminizationTree toRet = new DeterminizationTree(this.buchi);
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
		public List<TreeNode> children;
		public TreeNode parent;
		
		public TreeNode(Map<String, BuchiState> states, TreeNode parent){
			this.states = states;
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
	}
}
