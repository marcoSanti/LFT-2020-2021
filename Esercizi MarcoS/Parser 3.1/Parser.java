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
        throw new Error("near line " + lex.line + ": " + s);
    }
    void match(int t) {
        if (look.tag == t) {
            if (look.tag != Tag.EOF) move();
        } else error("syntax error");
    }



    public void start() {
        switch(look.tag){
            case '(':
                expr();
                match(Tag.EOF);
                break;

            case 256:
                expr();
                match(Tag.EOF);
                break;

            default:
                error("Parse error start");
        }
        

    }



    private void expr() {
        switch(look.tag){
            case '(':
                term();
                exprp();
                break;

            case 256:
                term();
                exprp();
                break;

            default:
                error("Parse error expr");
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
                match(')');
                break;
            
            case '$':
                match('$');
                break;

            default:
                error("Parse error exprp");

            
        }
    }



    private void term() {
        switch(look.tag){
            case '(':
                fact();
                termp();
                break;

            case 256:
                fact();
                termp();
                break;

            default:
                error("Parse error term");
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
                match(')');
                break;
            
            case '$':
                match('$');
                break;

            case '+':
                match('+');
                break;

            case '-':
                match('-');
                break;

            default:
                error("Parse Error termp");
            
            
        }
    }


    private void fact() {
        switch(look.tag){
            case '(':
                match('(');
                expr();
                break;

            case 256:
                match(256);
                break;

            default:
                error("Parse error fact. token fount: " + look.tag);
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