import java.io.*;

public class Parser {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;
    public Parser(Lexer l, BufferedReader br) {
        lex = l;
        pbr = br;
        move();
    }
    void move() {
        look = lex.lexical_scan(pbr);
        System.out.println("token = " + look);
    }
    void error(String s) {
        throw new Error(s);
    }
    void match(int t) {
        if (look.tag == t) {
            if (look.tag != Tag.EOF) move();
        } else error("syntax error");
    }



    private void start() {
        //qua lascio così per rilevare prima potenziali errori di sintassi come detto dal professore Padovani
        /*OVVERO SE TROVO UN CARATTERE CHE NON PUO ESSERE, EVITO DI CREARE UN ALTRO COSTRUTTO NELLO STACK MA DO SUBITO ERRORE. MIGLIORA EFFICENZA*/
        switch(look.tag){
            case '(':
                expr();
                match(Tag.EOF);
                break;

            case Tag.NUM:
                expr();
                match(Tag.EOF);
                break;

            default: 
                error("Parse error start at line:" + lex.line);
        }
        

    }



    private void expr() {
        switch(look.tag){
            case '(':
                term();
                exprp();
                break;

            case Tag.NUM:
                term();
                exprp();
                break;

            default:
                error("Parse error expr at line:" + lex.line);
        }
    }



    private void exprp() {
        switch(look.tag){
            case '+':
                match('+');
                term();
                exprp();
                break;
            
            case '-':
                match('-');
                term();
                exprp();
            
            case ')':
                
                break;
            
            case -1:
                
                break;

            default:
                error("Parse error exprp at line:" + lex.line);

            
        }
    }



    private void term() {
        switch(look.tag){
            case '(':
                fact();
                termp();
                break;

            case Tag.NUM:
                fact();
                termp();
                break;

            default:
                error("Parse error term at line:" + lex.line);
        }
    }


    private void termp() {
        switch(look.tag){
            case '*':
                match('*');
                fact();
                termp();
                break;
            
            case '/':
                match('/');
                fact();
                termp();
                break;
            
            case ')':
                
                break;
            
            case -1:
                
                break;

            case '+':
                
                break;

            case '-':
               
                break;

            default:
                error("Parse Error termp at line:" + lex.line);
            
            
        }
    }


    private void fact() {
        switch(look.tag){
            case '(':
                match('(');
                expr();
                match(')');
                break;

            case Tag.NUM:
                match(Tag.NUM);
                break;

            default:
                error("Parse error fact. at line:" + lex.line);
        }
    }



    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "test.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser parser = new Parser(lex, br);
            parser.start();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}