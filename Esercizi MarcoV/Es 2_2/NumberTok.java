public class NumberTok extends Token {
	int number;

	public String toString() { return "< " + tag + " , " + number + " >"; }

	public NumberTok(int tag, String lessema){
		super(tag);
		number = Integer.valueOf(lessema);
	}
}
