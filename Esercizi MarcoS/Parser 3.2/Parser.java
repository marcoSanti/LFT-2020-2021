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

        case '=':
            statlist();
            match(Tag.EOF);
            break;

        case Tag.PRINT:
            statlist();
            match(Tag.EOF);
            break;
        
        case Tag.READ:
            statlist();
            match(Tag.EOF);
            break;

        case Tag.COND:
            statlist();
            match(Tag.EOF);
            break;

        case Tag.WHILE:
            statlist();
            match(Tag.EOF);
            break;
        
        case '{':
            statlist();
            match(Tag.EOF);
            break;

        default : error("Error on prog() at line: " + lex.line);
      }
  }

  private void statlist(){
    switch(look.tag){

        case '=':
            match('=');
            stat();
            statlistp();
            break;

        case Tag.PRINT:
            match(Tag.PRINT);
            stat();
            statlistp();
            break;
        
        case Tag.READ:
            match(Tag.READ);
            stat();
            statlistp();
            break;

        case Tag.COND:
            match(Tag.COND);
            stat();
            statlistp();
            break;

        case Tag.WHILE:
            match(Tag.WHILE);
            stat();
            statlistp();
            break;
        
        case '{':
            match('{');
            stat();
            statlistp();
            break;

        default : error("Error on statlist() at line: " + lex.line);
      }
  }

  private void statlistp(){
      switch(look.tag){
          case '}':
            break;

        case Tag.EOF:
            break;

        case ';':
            match(';');
            stat();
            statlistp();
            break;

        default: error("Error on statlistp() at line: " + lex.line);
      }
  }

  private void stat(){
      switch(look.tag){
          case '=':
            match('=');
            match(Tag.ID);
            expr();
            break;

        case Tag.PRINT:
            match(Tag.PRINT);
            match('(');
            exprlist();
            match(')');
            break;

        case Tag.READ:
            match(Tag.READ);
            match('(');
            match(Tag.ID);
            match(')');
            break;

        case Tag.COND:
            match(Tag.COND);
            whenlist();
            match(Tag.ELSE);
            stat();
            break;

        case Tag.WHILE:
            match(Tag.WHILE);
            match('(');
            bexpr();
            match(')');
            stat();
            break;

        case '{':  
            match('{');
            statlist();
            match('}');
            break;

        default : error("Error on stat() at line: "+ lex.line);
      }
  }

  private void whenlist() {
    switch(look.tag){
        case Tag.WHEN:
            whenitem();
            whenlistp();
            break;

        default: error("Error on whenlist() at line: "+lex.line);
    }
  }

  private void whenlistp() {
      switch(look.tag){
          case Tag.WHEN:
            whenitem();
            whenlistp();
            break;

        case Tag.ELSE:
            break;

        default: error("Error on whenlistp() at line: " + lex.line);
      }
  }

  private void whenitem(){
      switch(look.tag){
          case Tag.WHEN:
            match(Tag.WHEN);
            match('(');
            bexpr();
            match(')');
            match(Tag.DO);
            stat();
            break;

        default: error("Error on whenitem() at line: " + lex.line);
      }
  }

  private void bexpr() {
      switch(look.tag){
          case Tag.RELOP:
            match(Tag.RELOP);
            expr();
            expr();
            break;

        default: error("Error on bexpr() at line: " + lex.line);
      }
  }

  private void expr() {
      switch(look.tag){
          case '+':
            match('+');
            match('(');
            exprlist();
            match(')');
            break;

        case '-':
            match('-');
            expr();
            expr();
            break;

        case '*':
            match('*');
            match('(');
            exprlist();
            match(')');
            break;

        case '/':
            match('/');
            expr();
            expr();
            break;

        case Tag.NUM:
            match(Tag.NUM);
            break;

        case Tag.ID:
            match(Tag.ID);
            break;

        default: error("Error on expr() at line: " + lex.line);
      }
  }

  private void exprlist() {
      switch(look.tag){
          case '+':
            expr();
            exprlist();
            break;

        case '-':
            expr();
            exprlist();
            break;

        case '*':
            expr();
            exprlist();
            break;

        case '/':
            expr();
            exprlist();
            break;

        case Tag.NUM:
            expr();
            exprlist();
            break;

        case Tag.ID:
            expr();
            exprlist();
            break;

        default: error("Error on exprlist() at line: " + lex.line);
      }
  }

  private void exprlistp(){
    switch(look.tag){
        case '+':
          expr();
          exprlist();
          break;

      case '-':
          expr();
          exprlist();
          break;

      case '*':
          expr();
          exprlist();
          break;

      case '/':
          expr();
          exprlist();
          break;

      case Tag.NUM:
          expr();
          exprlist();
          break;

      case Tag.ID:
          expr();
          exprlist();
          break;

      case ')':
        break;

      default: error("Error on exprlist() at line: " + lex.line);
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