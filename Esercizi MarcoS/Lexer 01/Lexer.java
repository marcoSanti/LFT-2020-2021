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
                    System.err.println("Erroneous character at or search" + " after & : "  + peek );
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
                
                do{
                    
                    if( (peek >='a' && peek <='z') || (peek >='A' && peek <= 'Z')  || Character.isDigit(peek)){

                        lessema = lessema + peek; // ho un carattere nell'alfabeto quindi continuo
                        readch(br);

                    }else{
                        System.err.println("Error: found invalid character:" + peek ); //carattere non ammesso. esco
                        return null;
                    }
                    
                }while((peek != ' ' ) && (peek != (char) -1));

                //devo fare uno switch per potere controllare che sia una keywoard oppure che sia solo un identificatore
                //controllo che lessema non sia una keyword e nel caso ritorno il tag

                switch(lessema){

                    case "cond":
                        peek = ' ';
                        return Word.cond;

                    case "when":
                        peek = ' ';
                        return Word.when;

                    case "then": 
                        peek = ' ';
                        return Word.then;

                    case "else":
                        peek = ' ';
                        return Word.elsetok;

                    case "while":
                        peek = ' ';
                        return Word.whiletok;

                    case "do":
                        peek = ' ';
                        return Word.dotok;

                    case "seq":
                        peek = ' ';
                        return Word.seq;

                    case "print":
                        peek = ' ';
                        return Word.print;

                    case "read":
                        peek = ' ';
                        return Word.read;
                        

                    default:  //ritorno idenificatore in quanto non è keyword del linguaggio
                        peek = ' ';
                        return new Word(Tag.ID, lessema);
                }

                

                } else if (Character.isDigit(peek)) {

                // ... gestire il caso dei numeri ... //
                
                String lessema = "";

                do{

                    if( (peek >='a' && peek <='z') || (peek >='A' && peek <= 'Z')  || Character.isDigit(peek)){

                        lessema = lessema + peek; // ho un carattere nell'alfabeto quindi continuo
                        readch(br);

                    }else{
                        System.err.println("Error: found invalid number: " + peek ); //carattere non ammesso. esco
                        return null;
                    }
                    
                }while((peek != ' ') && (peek != (char) -1));

                return new NumberTok(Tag.NUM, lessema);


                } else {
                        System.err.println("Error: unknown error!: " + peek );
                        return null;
                }
         }
    }





		
    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "test.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Token tok;

            //continuo a leggere quello che entra nel ciclo
            do {
                tok = lex.lexical_scan(br);
                System.out.println("Scan: " + tok);
            } while (tok != null && tok.tag != Tag.EOF);


            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }    
    }

}
