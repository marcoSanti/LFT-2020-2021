import java.io.*; 
import java.util.*;

public class Lexer {

    public static int line = 1;
    private char peek = ' ';
    
    private void readch(BufferedReader br) {
        try {
            peek = (char) br.read();
        } catch (IOException exc) {
            peek = (char) - 1; // ERROR
        }
    }

    public Token lexical_scan(BufferedReader br) {
        while (peek == ' ' || peek == '\t' || peek == '\n'  || peek == '\r') {
            if (peek == '\n') line++;
            readch(br);
        }
        
        switch (peek) {
            case '!':
                peek = ' ';
                return Token.not;

    // ... gestire i casi di (, ), {, }, +, -, *, /, ; ... 
            
            case '(':
                peek = ' ';
                return Token.lpt;

            case ')':
               peek = ' ';
               return Token.rpt;

            case '{':
                peek = ' ';
                return Token.lpg;

            case '}':
                peek = ' ';
                return Token.rpg;

            case '+':
                peek = ' ';
                return Token.plus;

            case '-':
                peek = ' ';
                return Token.minus;

            case '*':
                peek = ' ';
                return Token.mult;

            case '/':
                peek = ' ';
                return Token.div;

            case ';':
                peek = ' ';
                return Token.semicolon;

    // ... gestire i casi di ||, <, >, <=, >=, ==, <>, = ... //
    
            case '&':
                readch(br);
                if (peek == '&') {
                    peek = ' ';
                    return Word.and;
                } else {
                    System.err.println("Erroneous character" + " after & : "  + peek );
                    return null;
                }


            case '|':
                readch(br);
                if (peek == '|') {
                    peek = ' ';
                    return Word.or;
                } else {
                    System.err.println("Erroneous character at or search" + " after & : " + peek);
                    return null;
                }


            case '<':
                readch(br);
                if (peek == '>') {
                    peek = ' ';
                    return Word.ne;
                } else if (peek == '=') {
                    peek = ' ';
                    return Word.le;
                }else{
                    return Word.lt;
                }



            case '>':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.ge;
                }else{
                    return Word.gt;
                }


            case '=':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.eq;
                }else{
                    return Token.assign;
                }
            
            case (char)-1:
                return new Token(Tag.EOF);

            default:
                if (Character.isLetter(peek)) {

    // ... gestire il caso degli identificatori e delle parole chiave 
                    String lessema = "";
                    do {
                        lessema = lessema + peek; // ho un carattere nell'alfabeto quindi continuo
                        readch(br);

                    } while (((peek >= 'a' && peek <= 'z') || (peek >= 'A' && peek <= 'Z') || Character.isDigit(peek)) && (peek != ' ') && (peek != (char) - 1));
                    
                    if (("|&!(){}+-*/=;><_ ".indexOf(peek) < 0) && !(peek == ' ' || peek == '\t' || peek == '\n' || peek == '\r' || peek == (char)-1)){  
						System.err.println("Error: found invalid character:" + peek + " at line " + line); //carattere non ammesso. esco
						System.err.println(String.format("%04x", (int) peek));
                        return null;
                    }

                    switch (lessema) {

                        case "cond":
							return Word.cond;

                        case "when":
                            return Word.when;

                        case "then":							
                            return Word.then;

                        case "else":							
                            return Word.elsetok;

                        case "while":							
                            return Word.whiletok;

                        case "do":							
                            return Word.dotok;

                        case "seq":							
                            return Word.seq;

                        case "print":							
                            return Word.print;

                        case "read":							
                            return Word.read;


                        default:                            
                            return new Word(Tag.ID, lessema);
                    }


                } else if (Character.isDigit(peek)) {

    // ... gestire il caso dei numeri ... 
                    String lessema = "";
                    do{
                    lessema = lessema + peek; // ho un carattere nell'alfabeto quindi continuo
                    readch(br);
                    } while (Character.isDigit(peek) && (peek != ' ') && (peek != (char) - 1));

    
    //RITORNO UN NUOVO NUMERO
                    return new NumberTok(Tag.NUM, lessema);
                    } else {
                        System.err.println("Erroneous character: " + peek );
                        return null;
                }
         }
    }
		
    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "prova.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Token tok;
            do {
                tok = lex.lexical_scan(br);
                System.out.println("Scan: " + tok);
            } while (tok.tag != Tag.EOF);
            br.close();
        } catch (IOException e) {e.printStackTrace();}    
    }

}
