import java.util.List;


public class DeterminizationTree {
/** 
 * This class reperesents the tree of the determinization procces,
 * converting from NBA to DNA
 * 
 */
	
	private Buchi buchi;
	private List<TreeNode> nodelist;
	private int number;
	private Map<BuchiState, Integer> lastUpdated;
	//TODO add other relevant fields
	
	/**
	 * Constructor for Tree.
	 * @param buchi - the NBA to be determinized
	 */
	public DeterminizationTree(Buchi buchi){
		this.nodelist = new LinkedHashedList<TreeNode>();
		this.lastUpdated = new HashMap<BuchiState, Integer>();
		Set<BuchiState> initialStates = new HashedSet<BuchiState>();
		for(BuchiState s : buchi.states){
			lastUpdated.put(s, 0);
			if(s.isInitial){
				initialStates.add(s)
			}
		}
		nodelist.add(new TreeNode(initialStates, null))
	}
	
	/**
	 * Does one step on the tree, with char s.
	 * @param s - the input char
	 * @return number for numbered automata
	 */
	public int doStep(char s){
		// if we killed all paths, return 0 (lowest false)
		if(nodelist.isEmpty()){ return 0; }
		doRecursiveStep(s, nodelist.get(0));
		return this.number;
	}
	//implementation of doStep:
	private boolean doRecursiveStep(char s, TreeNode t){
		//TODO
	}
	
	/**
	 * Returns the array representing the current state
	 * @return Array representing tree shape
	 */
	public int[] getTreeStateArray(){
		//TODO
	}
	
	/**
	 * Returns an array that represents the node that each
	 * state belongs to.
	 * @return Array of the numbers of the states
	 */
	public int[] getStateNodesArray(){
		//TODO
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
