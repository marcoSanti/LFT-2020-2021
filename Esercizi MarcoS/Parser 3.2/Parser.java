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
        } else error("syntax error at line: " + Lexer.line + "\n token: " + t + " does not match expected token: " + look.tag);
    }



  private void prog(){
      switch(look.tag){
        //facendo così rendo il codice più leggibile e meno esteso. in pratica è come se mettessi in or tutti i case!
        case '=':
        case Tag.PRINT:
        case Tag.READ:
        case Tag.COND:
        case Tag.WHILE:
        case '{':
            statlist();
            match(Tag.EOF);
            break;

        default : error("Error on prog(). Found: " + look.tag);
      }
  }

  private void statlist(){
    switch(look.tag){

        case '=':
        case Tag.PRINT:
        case Tag.READ:
        case Tag.COND:
        case Tag.WHILE:
        case '{':
            stat();
            statlistp();
            break;

        default : error("Error on statlist(). Found: " + look.tag);
      }
  }

  private void statlistp(){
      switch(look.tag){
        case '}':
        case Tag.EOF:
            break;

        case ';':
            match(';');
            stat();
            statlistp();
            break;

        default: error("Error on statlistp(). Found: " + look.tag);
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



        default : error("Error on stat(). Found: " + look.tag);
      }
  }

  private void whenlist() {
    switch(look.tag){
        case Tag.WHEN:
            whenitem();
            whenlistp();
            break;

        default: error("Error on whenlist(). Found: " + look.tag);
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

        default: error("Error on whenlistp(). Found: " + look.tag);
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

        default: error("Error on whenitem(). Found: " + look.tag);
      }
  }

  private void bexpr() {
      switch(look.tag){
          case Tag.RELOP:
            match(Tag.RELOP);
            expr();
            expr();
            break;

        default: error("Error on bexpr(). Found: " + look.tag);
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

        default: error("Error on expr(). Found: " + look.tag);
      }
  }

  private void exprlist() {
      switch(look.tag){
        case '+':
        case '-':
        case '*':
        case '/':
        case Tag.NUM:
        case Tag.ID:
            expr();
            exprlistp();
            break;

        default: error("Error on exprlist(). Found: " + look.tag);
      }
  }

  private void exprlistp(){
    switch(look.tag){
      case '+':
      case '-':
      case '*':
      case '/':
      case Tag.NUM:
      case Tag.ID:
          expr();
          exprlistp();
          break;

      case ')':
        break;

      default: error("Error on exprlist(). Found: " + look.tag);
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
