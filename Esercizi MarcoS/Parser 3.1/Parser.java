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
        /*classe modificata per potere mostrare anche la posizione dell'errore trovato sia come numero che visivamente */
        lex.currentLine += "\n";
        for(int i = 1; i<lex.lineChar-1; i++)lex.currentLine += ' ';
        lex.currentLine+='^';
        throw new Error(s + " @<Line:" + lex.line+ "> -> <Column:" + lex.lineChar+">\n\n" + lex.currentLine + "\n");
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
            case '(': //non serve un corpo in quanto viene eseguito lo stesso codice e in quanto sarebbe come fare case Tag.NUM || '('
            case Tag.NUM:
                expr();
                match(Tag.EOF);
                break;

            default: 
                error("Parse error start Found: " + look.tag);
        }
        

    }



    private void expr() {
        switch(look.tag){
            case '(':
            case Tag.NUM:
                term();
                exprp();
                break;

            default:
                error("Parse error expr Found: " + look.tag);
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
            case -1:
                break;

            default:
                error("Parse error exprp Found: " + look.tag);

            
        }
    }



    private void term() {
        switch(look.tag){
            case '(':
            case Tag.NUM:
                fact();
                termp();
                break;

            default:
                error("Parse error term Found: " + look.tag);
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
            case -1:
            case '+':
            case '-':
                break;

            default:
                error("Parse Error termp Found: " + look.tag);
            
            
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
                error("Parse error fact. Found: " + look.tag);
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