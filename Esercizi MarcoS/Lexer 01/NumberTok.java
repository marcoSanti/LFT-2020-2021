/**
	qua devo fare la classe dei token numerici!
	ovvero discernere i lessemi dei numeri!
*/

public class NumberTok extends Token {
	// ... completare ...

	int number;

	public String toString() { return "< " + tag + " , " + number + " >"; }

	public NumberTok(int tag, String lessema){
		super(tag);
		number = Integer.valueOf(lessema);
	}

	
}
