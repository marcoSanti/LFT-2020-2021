import java.io.*; 
import java.util.*;

public class Lexer {

    public static int line = 1;   
    private char peek = ' ';   
    
    private void readch(BufferedReader br) {  
        try {
            peek = (char) br.read();  //leggo un char e lo metto in peek
        } catch (IOException exc) {
            peek = (char) -1; // ERROR
        }
    }

    public Token lexical_scan(BufferedReader br) {




        //questo lo eseguo fino a che hp un carattere WS. quando non lo ho più esco dal ciclo
        while (peek == ' ' || peek == '\t' || peek == '\n'  || peek == '\r') {  
            if (peek == '\n') line++;
            readch(br);
        }
        





        switch (peek) {

            //se leggo un ! ritorno la costante che indica il not
            case '!':
                peek = ' ';  //questo peek lo metto per poter ritornare il giro successivo al while a inizio codice
                return Token.not;



	        // ... gestire i casi di (, ), {, }, +, -, *, /, ; ... //

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




            //se ho un caso di && 
            case '&':
                readch(br);
                if (peek == '&') {
                    peek = ' ';
                    return Word.and;
                } else {
                    System.err.println("Erroneous character" + " after & : "  + peek );
                    return null;
                }





	        // ... gestire i casi di ||, <, >, <=, >=, ==, <>, = ... //


            //questi sono automi del tipo 

            //                 s1       s2     __
            //       ---->Q0------>Q1-------->|Q2|
            //                                ----


            /**Caso di || */
            case '|':
                readch(br);
                if(peek == '|'){
                    peek = ' ';
                    return Word.or;
                }else{
                    System.err.println("Erroneous character" + " after & : "  + peek );
                    return null;
                }


            case '<':
                readch(br);
                if(peek == ' '){
                    peek = ' ';
                    return Word.lt;
                }else if(peek == '>'){
                    peek = ' ';
                    return Word.ne;
                }else if(peek == '='){
                    peek = ' ';
                    return Word.le;
                }

            
            case '>':
                readch(br);
                if(peek == ' '){
                    peek = ' ';
                    return Word.gt;
                }else if(peek == '='){
                    peek = ' ';
                    return Word.ge;
                }

            
            case '=':
                readch(br);
                if(peek == ' '){
                    peek = ' ';
                    return Token.assign;
                }else if(peek == '='){
                    peek = ' ';
                    return Word.eq;
                }



            //se ho finito e ho un -1 allora ritorno un tag eof e chiudo
            case (char)-1:
                return new Token(Tag.EOF);





            default:
                if (Character.isLetter(peek)) {

	            // ... gestire il caso degli identificatori e delle parole chiave //

                //ottengo la stringa
                String lessema = "";
                while(peek != ' ' || peek != (char) -1){
                    lessema += peek;
                    readch(br);
                }

                //devo fare uno switch per potere controllare che sia una keywoard oppure che sia solo un identificatore



                } else if (Character.isDigit(peek)) {

	            // ... gestire il caso dei numeri ... //

                } else {
                        System.err.println("Erroneous character: " + peek );
                        return null;
                }
         }
    }





		
    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "...path..."; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Token tok;

            //continuo a leggere quello che entra nel ciclo
            do {
                tok = lex.lexical_scan(br);
                System.out.println("Scan: " + tok);
            } while (tok.tag != Tag.EOF);


            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }    
    }

}
