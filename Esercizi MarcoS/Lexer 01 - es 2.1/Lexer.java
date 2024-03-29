import java.io.*;
import java.util.*;

public class Lexer {

    public static int line = 1;
    private char peek = ' ';

    private void readch(BufferedReader br) {
        try {
            peek = (char) br.read(); //leggo un char e lo metto in peek
        } catch (IOException exc) {
            peek = (char) - 1; // ERROR
        }
    }

    public Token lexical_scan(BufferedReader br) {


        //questo lo eseguo fino a che hp un carattere WS. quando non lo ho più esco dal ciclo
        while (peek == ' ' || peek == '\t' || peek == '\n' || peek == '\r') {
            if (peek == '\n') line++;
            readch(br);
        }


        switch (peek) {

            /**GESTISCO I CASI DI SIMBOLI SINGOLI*/
        
            case '!':
                peek = ' '; //questo peek lo metto per poter ritornare il giro successivo al while a inizio codice
                return Token.not;


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




            /**GESTISCO I CASI DI SINGOLI SIMBOLI E DELL'= E DEI < E > IN QUANTO DEVO DISAMBIGUARE SE SONO >= O <= */
            //questi sono automi del tipo 
            //                                ______
            //                 s1       s2    |    |
            //       ---->Q0------>Q1-------->| Q2 |
            //                                |____|

            
            case '&':
                readch(br);
                if (peek == '&') {
                    peek = ' ';
                    return Word.and;
                } else {
                    System.err.println("Erroneous character" + " after & : " + peek);
                    return null;
                }

    
            case '|':
                readch(br);
                if (peek == '|') {
                    peek = ' ';
                    return Word.or;
                } else {
                    System.err.println("Erroneous character at or search" + " after | : " + peek);
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


            //se ho finito e ho un -1 allora ritorno un tag eof e chiudo
            case (char) - 1:
                return new Token(Tag.EOF);





            default:
            
                /**GESTIONE DEI CARATTERI DI TESTO */
                if (Character.isLetter(peek)) { 

                    //OTTENGO LA STRINGA FINO AL PROSSIMO CHAR NON AMMISSIBILE.
                    String lessema = "";
                    do {
                        lessema = lessema + peek; // ho un carattere nell'alfabeto quindi continuo
                        readch(br);

                    } while (((peek >= 'a' && peek <= 'z') || (peek >= 'A' && peek <= 'Z') || Character.isDigit(peek)) && (peek != ' ') && (peek != (char) - 1)); //CONDIZIONE PER CUI UN SIMBOLO RIPETTA IL PATTERN DEL LESSEMA CHE CERCO



                    //controllo che non sono uscito dal ciclo while a colpa di un carattere che non sta nel mio alfabeto:
                    //qua praticamente passo tutti i simboli che sono nel mio alfabeto, eccetto quelli char e numerici, in quanto
                    //se sono uscito dal ciclo prima, di sicuro non è un char o un numero che mi ha fatto uscire
                    //occhio che devo mettere anche uno spazio in fondo per evitare che uno spazio mi faccia uscire
                    //poi valuto con java se ho presente nella stringa il carattere peek. se non ce l'ho
                    //il simbolo che mi ha fatto uscire non è nell'alfabeto
                    //e quindi posso dire che ho un errore.
                    //questa versione è più snella che controlare con tutti if

                    if (("|&!(){}+-*/=;><_ ".indexOf(peek) < 0) && !(peek == ' ' || peek == '\t' || peek == '\n' || peek == '\r' || peek == (char)-1)){  
						System.err.println("Error: found invalid character:" + peek + " at line " + line); //carattere non ammesso. esco
						System.err.println(String.format("%04x", (int) peek));
                        return null;
                    }



                    //CONTROLLO CHE IL MIO LESSEMA NON SIA UNA KEYWORD E NEL CASO LO SIA RITORNO LA KEYWORD, ALTRIMENTI RITORNO UN IDENTIFICATORE

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



                } else if (Character.isDigit(peek)) { /**GESTIONE DELLE COSTANTI NUMERICHE */


                    String lessema = "";

                    do{
                        lessema = lessema + peek; // ho un carattere nell'alfabeto quindi continuo
                        readch(br);

                    } while (Character.isDigit(peek) && (peek != ' ') && (peek != (char) - 1));


                    
                    
                    //RITORNO UN NUOVO NUMERO
                    return new NumberTok(Tag.NUM, lessema);


                } else {
                    System.err.println("Error: unknown error!: " + peek);
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