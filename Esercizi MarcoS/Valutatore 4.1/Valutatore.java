import java.io.*;

public class Valutatore {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Valutatore(Lexer l, BufferedReader br) {
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

    public void start() {
        int expr_val;

        switch(look.tag){
          case '(':
          case Tag.NUM:
            expr_val = expr();
            match(Tag.EOF);
            System.out.println(expr_val);
            break;

          default:
            error("Parse error start at line:" + lex.line);

        }
    }



    private int expr() {
        int term_val = 0, exprp_val = 0;

        switch(look.tag){
            case '(':
            case Tag.NUM:
                term_val = term();
                exprp_val = exprp(term_val);
                break;

            default:
                error("Parse error expr at line:" + lex.line);
        }

        // ... completare ...
        return exprp_val;
    }






    private int exprp(int exprp_i) {
        int term_val=0, exprp_val=0;
        switch (look.tag) {
            case '+':
                match('+');
                term_val = term();
                exprp_val = exprp(exprp_i + term_val);
                break;


            case '-':
              match('-');
              term_val = term();
              exprp_val = exprp(exprp_i - term_val);
              break;

            case ')':
            case -1:
                  exprp_val = exprp_i;
                  break;

              default:
                  error("Parse error exprp at line:" + lex.line);

        }


        return exprp_val;
    }






    private int term() {
      int fact_val=0, termp_val=0;
      switch(look.tag){
          case '(':
          case Tag.NUM:
              fact_val = fact();
              termp_val = termp(fact_val);
              break;

          default:
              error("Parse error term at line:" + lex.line);
      }

      return termp_val;
    }






    private int termp(int termp_i) {
      int termp_val=0, fact_val=0;
      switch(look.tag){
          case '*':
              match('*');
              fact_val = fact();
              termp_val = termp(termp_i * fact_val);
              break;

          case '/':
              match('/');
              fact_val = fact();
              termp_val = termp(termp_i / fact_val);
              break;

          case ')':
          case -1:
          case '+':
          case '-':
              termp_val = termp_i;
              break;

          default:
              error("Parse Error termp at line:" + lex.line);


      }

      return termp_val;
    }







    private int fact() {
      int fact_val=0;
      switch(look.tag){
          case '(':
              match('(');
              fact_val = expr();
              match(')');
              break;

          case Tag.NUM:
              fact_val = ((NumberTok)look).number;
              match(Tag.NUM);
              break;

          default:
              error("Parse error fact. at line:" + lex.line);
      }

      return fact_val;
    }





    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "test.lft"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Valutatore valutatore = new Valutatore(lex, br);
            valutatore.start();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
