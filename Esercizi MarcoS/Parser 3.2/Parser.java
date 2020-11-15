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
        } else error("syntax error at line: " + Lexer.line + "\n token: " + t + " does not match expected token: " + look.tag);
    }


    private void prog(){
      
        switch(look.tag){
            case '(':
                stat();
                match(Tag.EOF);
                break;

            default: 
                error("Error on prog status at line: " + Lexer.line);
        }
    }

    private void statlist(){
        switch(look.tag){
            case '(':
                stat();
                statlistp();
                break;

            default:
                error("Error on statlist at line: " + Lexer.line);
        }
    }


    private void statlistp(){
       switch(look.tag){
        case '(':
            stat();
            statlistp();
            break;

        case ')':
            break;

        default:
            error("Error on statlistp at line: " + Lexer.line);
       }

        
    }

    private void stat(){
        switch(look.tag){
            case '(':
                match(Token.lpt.tag);
                statp();
                match(Token.rpt.tag);
                break;

            default:
                error("Error on stat at line: " + Lexer.line);
        }
    }


    //VERIFICARE PERCHè NON SONO SICURO CHE FUNZIONI BENE!
    private void statp(){

        switch(look.tag){

            case '=':
                match('=');
                match(Tag.ID);
                expr();
                break;

            case Tag.COND:
                match(Tag.COND);
                bexpr();
                stat();
                elseopt();
                break;
            
            case Tag.WHILE:
                match(Tag.WHILE);
                bexpr();
                stat();
                break;

            case Tag.DO:
                match(Tag.DO);
                statlist();
                break;

            case Tag.PRINT:
                match(Tag.PRINT);
                exprlist();
                break;

            case Tag.READ:
                match(Tag.READ);
                match(Tag.ID);
                break;


            default:
                error("Error on statp at line: " + Lexer.line);
        }

    }




    private void elseopt(){

        switch(look.tag){
            case '(':
                match('(');
                match(Tag.ELSE);
                stat();
                match(')');
                break;

            case ')':
                break;

            default:
                error("Error on elseopt at line: " + Lexer.line);
        }

    }

    private void bexpr(){
        switch(look.tag){
            case '(':
                match('(');
                bexprp();
                match(')');
                break;

            default:
                error("Error on bexpr at line: " + Lexer.line);
        }

    }

    private void bexprp(){
        switch(look.tag){

            case Tag.RELOP:
                match(Tag.RELOP);
                expr();
                expr();
                break;

            default:
                error("Error on bexprp at line: " + Lexer.line);
        }

    }

    private void expr(){

        switch(look.tag){
            case Tag.NUM:
                match(Tag.NUM);
                break;

            case Tag.ID:
                match(Tag.ID);
                break;

            case '(':
                match('(');
                exprp();
                match(')');
                break;

            default: 
                error("Error at expr at line: " + Lexer.line);
        }

    }

    private void exprp(){

        switch(look.tag){

            case '+':
                match('+');
                exprlist();
                break;

            case '-':
                match('-');
                expr();
                expr();
                break;

            case '*':
                match('*');
                exprlist();
                break;

            case '/':
                match('/');
                expr();
                expr();
                break;

            default:
                error("Error on exprp at line: " + Lexer.line);
        }

    }

    private void exprlist(){
        switch(look.tag){
            case Tag.NUM:
                expr();
                exprlistp();
                break;

            case Tag.ID:
                expr();
                exprlistp();
                break;

            case '(':
                expr();
                exprlistp();
                break;

            default:
                error("Error on exprlist at line: " + Lexer.line);
        }
    }

    private void exprlistp(){

        switch(look.tag){
            case Tag.NUM:
                expr();
                exprlistp();
                break;

            case Tag.ID:
                expr();
                exprlistp();
                break;

            case '(':
                expr();
                exprlistp();
                break;

            case ')':
                break;

            default:
                error("Error on exprlistp at line: " + Lexer.line);
        }

    }




    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "test.txt"; // il percorso del file da leggere
        
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
           
            Parser parser = new Parser(lex, br);
            parser.prog();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}