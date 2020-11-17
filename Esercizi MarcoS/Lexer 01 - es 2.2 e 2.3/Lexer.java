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

            //qua devo andare a controllare i  commenti sia in forma // che /**/
            //IDEA: se ho un commento salto oltre e vado al successivo simbolo che non sia più parte di un commento
            case '/':
                readch(br);
                if(peek == '/'){ //commento nella stessa linea. leggo fino a che ho un \n o un EOF
                    while(peek != (char) -1){
                        if(peek == '\n'){ //ho trovato il fine linea. leggo il char successivo ed esco
                            if(peek != (char) -1 )readch(br); //leggo il char successivo solo a condizione che non ho un eof 
                            break;
                        }
                        readch(br); //commento inline. leggo fino a che non incontro nuova linea o fino a che non finisce il file
                    } 
                }else if(peek == '*'){ //ho trovato un commento multilinea
                    boolean mLCommentClosed = false; //variabile che mi dice se ho un commento multilinea chiuso con successo. serve per verificare all'uscita del ciclo while se sono uscito per commento chiuso o per EOF trovato
                    while(peek != (char) -1){ //ciclo fino a che non ho un EOF
                        if(peek == '*'){ // se il char è una * potrei avere dopo un / ma non sono sicuro quindi controllo
                            readch(br); //leggo il simbolo dopo
                            if(peek == '/'){ 
                                //commento multilinea finito
                                //mi sposto al carattere successivo ed esco
                                readch(br);
                                mLCommentClosed = true; //setto che il commento multilina è stato chiuso correttamente
                                break; //esco dal ciclo while
                            }
                        }else{//non ho il lo / dopo quindi devo legger char. non ho un singolo readch perchè devo evitare di leggere uno, entrare nel controllo / e leggere un altro per poi leggere un terzo: avrei saltato un simbolo quindi!
                            readch(br); //carattere non trovato quindi leggo il carattere successivo
                        }
                    }
                    if((peek == (char) -1) && !mLCommentClosed){ //se il simbolo finale è EOF e se non ho chiuso il commento mi arrabbio e segnalo che non ho chiuso il commento prima della fine del file
                        System.out.println("Error: unescpected EOF: multiline comment not closed before EOF");
                        return null;
                    }
                }else{
                    peek = ' ';
                    return Token.div;
                }
                return lexical_scan(br); // se arrivo in questo punto, riavvio la scansione lessicale, dopo avere modificaro il valore in cima a peek!
                                        //se non riavvio la scansione ma lascio continuare la stessa, mancado un break o un return, entro a caso in uno switch quindi non va bene!



			        
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
                    System.err.println("Erroneous character" + " after & : " + peek + ", at line " + line);
                    return null;
                }

    
            case '|':
                readch(br);
                if (peek == '|') {
                    peek = ' ';
                    return Word.or;
                } else {
                    System.err.println("Erroneous character at or search" + " after | : " + peek + ", at line " + line);
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
                
                //SCEGLIERE COME GESTIRE CASI DEL TIPO VAR/*COMM*/IABILE = 25;
                
                /**GESTIONE DEI CARATTERI DI TESTO */
                if (Character.isLetter(peek) || (peek == '_') ) { 

                    //variabili booleane usate per verificare che non ho solo sequenza di {_}{_}*
                    //in pratica vado a controllare che stringa inizia con underscore
                    //e poi vado a controllare che vi sia almeno un carattere nell'identificatore
                    boolean letterFoundInUnderscores = false;
                    boolean startWithUnderscores = peek == '_';


                    //OTTENGO LA STRINGA FINO AL PROSSIMO CHAR NON AMMISSIBILE.
                    String lessema = "";
                    do {
                        lessema = lessema + peek; // ho un carattere nell'alfabeto quindi continuo
                        readch(br);

                        letterFoundInUnderscores = letterFoundInUnderscores || Character.isLetter(peek);

                    } while ((Character.isLetter(peek) || Character.isDigit(peek) || (peek == '_')) && (peek != ' ') && (peek != (char) - 1)); //CONDIZIONE PER CUI UN SIMBOLO RIPETTA IL PATTERN DEL LESSEMA CHE CERCO


                    //controllo che se inizio con underscore e che se non ho lettere allora è identificatore non valido
                    if(startWithUnderscores && !letterFoundInUnderscores){
                        System.out.println("Error: token with only underscores at line " + line);
                        return null;
                    }

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



                } else if (Character.isDigit(peek) ) { /**GESTIONE DELLE COSTANTI NUMERICHE */


                    String lessema = "";

                    do{
                        lessema = lessema + peek; // ho un carattere nell'alfabeto quindi continuo
                        readch(br);

                    } while (Character.isDigit(peek) && (peek != ' ') && (peek != (char) - 1));

                    
                    //RITORNO UN NUOVO NUMERO
                    
                    return new NumberTok(Tag.NUM, lessema);


                } else {
                    System.err.println("Error: unknown error!: " + peek + ", at line " + line);
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
            do{
            //continuo a leggere quello che entra nel ciclo
                tok = lex.lexical_scan(br);
                System.out.println("Scan: " + tok);
            } while (tok != null && tok.tag != Tag.EOF);


            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}