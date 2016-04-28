/**
 * This class is an analyzer for numbered parity automata.
 */
public class NumberedAnalyzer {

	// TODO private fields
	
	public NumberedAnalyzer(Numbered numbered)
    {

    }
	
    public boolean isEmpty()
    {
        return true;
    }

    public static void main(String[] args) {
        Numbered numbered = new Numbered(System.in);
        NumberedAnalyzer analyzer = new NumberedAnalyzer(numbered);
        System.out.println(!analyzer.isEmpty());
    }
}
