/*
	Classe che consente di stampare un tag già formattato e che
	contiene delle definizioni di token già fatte
	NOTA BENE: IL NOME DI QUESTI TOKEN CORRISPONDE AL NUMERO CHE HANNO NELLA TABELLA ASCII
*/

public class Token {

    public final int tag;

    public Token(int t) { tag = t;  }

    public String toString() {return "< " + tag + " >";}

    public static final Token
		not       = new Token('!'),
		lpt       = new Token('('),
		rpt       = new Token(')'),
		lpg       = new Token('{'),
		rpg       = new Token('}'),
		plus      = new Token('+'),
		minus     = new Token('-'),
		mult      = new Token('*'),
		div       = new Token('/'),
		assign    = new Token('='),
		semicolon = new Token(';');    
}
