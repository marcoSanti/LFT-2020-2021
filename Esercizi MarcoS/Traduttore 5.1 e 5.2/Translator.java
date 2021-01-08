import java.io.*;



public class Translator {
  private Lexer lex;
  private BufferedReader pbr;
  private Token look;

  SymbolTable st = new SymbolTable();
  CodeGenerator code = new CodeGenerator();
  int count = 0; //indica quanti elementi sono stati inseriti nella tabella dei simboli

  public Translator(Lexer l, BufferedReader br) {
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
    for (int i = 1; i < lex.lineChar - 1; i++) lex.currentLine += ' ';
    lex.currentLine += '^';
    throw new Error(s + " @<Line:" + lex.line + "> -> <Column:" + lex.lineChar + ">\n\n" + lex.currentLine + "\n");
  }

  void match(int t) {
    if (look.tag == t) {
      if (look.tag != Tag.EOF) move();
    } else error("syntax error at line: " + Lexer.line + "\n token: " + t + " does not match expected token: " + look.tag);
  }

  private void prog() {
    switch (look.tag) {
      //facendo così rendo il codice più leggibile e meno esteso. in pratica è come se mettessi in or tutti i case!
    case '=':
    case Tag.PRINT:
    case Tag.READ:
    case Tag.COND:
    case Tag.WHILE:
    case '{':{
        statlist();
        match(Tag.EOF);
        try {
          code.toJasmin();
        } catch(java.io.IOException e) {
          System.out.println("IO error\n");
        }
        break;
      }
    default:
      error("Error on prog(). Found: " + look.tag);
    }
  }

  private void statlist() {
    switch (look.tag) {

    case '=':
    case Tag.PRINT:
    case Tag.READ:
    case Tag.COND:
    case Tag.WHILE:
    case '{':
        stat();
        statlistp();
        break;

    default:
      error("Error on statlist(). Found: " + look.tag);
    }
  }


  private void statlistp() {
    switch (look.tag) {
    case '}':
    case Tag.EOF:
      break;

    case ';':
        match(';');
        stat();
        statlistp();
        break;


    default:
      error("Error on statlistp(). Found: " + look.tag);
    }
  }

  private void stat() {
    switch (look.tag) {
    case Tag.READ:
      {
        match(Tag.READ);
        match('(');
        if (look.tag == Tag.ID) {
          int id_addr = st.lookupAddress(((Word) look).lexeme);
          if (id_addr == -1) {
            id_addr = count;
            st.insert(((Word) look).lexeme, count++);
          }
          match(Tag.ID);
          match(')');
          code.emit(OpCode.invokestatic, 0);
          code.emit(OpCode.istore, id_addr);
        } else error("Error in grammar (stat) after read( with " + look);
        break;
      }

    case Tag.PRINT:
        match(Tag.PRINT);
        match('(');
        exprlist();
        code.emit(OpCode.invokestatic, 1);
        match(')');
        break;
      

    case '=':{
        match('='); 
        String toke_name = ((Word) look).lexeme; //mi salco id del token cos' posso poi avere anche il valore dell'id per potere avere un nuovo elemento nella tabella dei simboli
        match(Tag.ID);

        expr();

        int position_of_token_in_st = st.lookupAddress(toke_name);
        if (position_of_token_in_st == -1) {
          code.emit(OpCode.istore, count);
          st.insert(toke_name, count++); //questo significa, salva toke_name con il valore di count e successivamente incrementalo... Fosse stato ++count sarebbe stato incrementa prima count e poi salva toke_name con il valore appena calcolato
        } else {
          code.emit(OpCode.istore, position_of_token_in_st);
        }
        break;
      }

    case Tag.COND:{
        match(Tag.COND);
        int cond_done_l = code.newLabel();

        whenlist(cond_done_l);

        match(Tag.ELSE);

        stat();
        code.emitLabel(cond_done_l); //se ho fatto una roba dentro un do allora finisco qua

        break;
      }

    case Tag.WHILE:{

        int bexpr_true_l = code.newLabel();
        int bexpr_false_l = code.newLabel();
        int l_stat1_next = code.newLabel();

        match(Tag.WHILE);
        match('(');

        code.emitLabel(l_stat1_next);
        bexpr(bexpr_false_l, bexpr_true_l);

        match(')');

        code.emit(OpCode.label, bexpr_true_l);

        stat();

        code.emit(OpCode.GOto, l_stat1_next);

        code.emitLabel(bexpr_false_l);

        break;
      }

    case '{':{
        match('{');

        statlist();

        match('}');
        break;
      }

    default:
      error("Error on stat(). Found: " + look.tag);
    }
  }

  private void whenlist(int done_l) {
    switch (look.tag) {
        case Tag.WHEN:{

            whenitem(done_l);
            whenlistp(done_l);

            break;
        }

        default:
        error("Error on whenlist(). Found: " + look.tag);
    }
  }

  private void whenlistp(int done_l) {
    switch (look.tag) {
        case Tag.WHEN:{

            whenitem(done_l);

            whenlistp(done_l);

            break;
        }

        case Tag.ELSE:
        break;

        default: error("Error on whenlistp(). Found: " + look.tag);
    }
  }

  private void whenitem(int whenitem_done_l) {
    switch (look.tag) {
        case Tag.WHEN:{
            match(Tag.WHEN);
            match('(');

            int bexpr_true_l = code.newLabel();
            int bexpr_false_l = code.newLabel();
            bexpr(bexpr_false_l, bexpr_true_l);

            match(')');

            match(Tag.DO);
            code.emitLabel(bexpr_true_l);
            stat(); //cambiare in 0
            code.emit(OpCode.GOto, whenitem_done_l);

            code.emitLabel(bexpr_false_l);

            break;
        }

        default: error("Error on whenitem(). Found: " + look.tag);
    }

  }

  private void bexpr(int bexpr_false_l, int bexpr_true_l) {
    boolean relOp_is_negated = look.tag == Token.not.tag;

    if(relOp_is_negated) match(Token.not.tag); //controllo che abbia una negazione. se la ho alllora mi sposto al simbolo successivo altrimenti ignoro

    /*idea: uso una sdd con cortocircuito come quelle illusttate dal professore padovani. controllo comunque che non abbia fatto un !&& e !||, anche se semplicemente basterebbe invertire le etichette per ottenerlo */


    switch (look.tag) {

        case Tag.AND:{

          if(relOp_is_negated) error("Error: illegal use of NOT operator before AND operator!"); //contollo che non ho negato una operazione booleana

          match(Tag.AND);
          int bexpr1_true_label = code.newLabel();

          match('(');
          bexpr(bexpr_false_l, bexpr1_true_label);
          match(')');

          code.emitLabel(bexpr1_true_label);

          match('(');
          bexpr(bexpr_false_l, bexpr_true_l);
          match(')');

          break;
        }

        case Tag.OR:{
 
         if(relOp_is_negated) error("Error: illegal use of NOT operator before AND operator!"); //controllo che non ho negato una operazione booleana

          match(Tag.OR);

          int bexpr1_false_label = code.newLabel();

          match('(');
          bexpr(bexpr1_false_label, bexpr_true_l);
          match(')');

          code.emitLabel(bexpr1_false_label);

          match('(');
          bexpr(bexpr_false_l, bexpr_true_l);
          match(')');

          break;
        }

        case Tag.RELOP:{

          String relOperator = ((Word) look).lexeme;
          match(Tag.RELOP);


          expr();
          expr();

          //controllo tutte le possibili relop e infine aggiungo l'etichetta del falso
          //se invece ho una negazione di un operatore, sto ad emettere l'operazione opposta... NB il caso ! <> equivale a ==
          //un altra alternativa a emettere operazione inversa è quella di sostituire invertire le etichette, ma questo richiede piu computazione rispetto a una emissione diretta dell'operazione opposta...
          
          if (relOperator.equals(Word.lt.lexeme)){
            if(relOp_is_negated) code.emit(OpCode.if_icmpge, bexpr_true_l);
            else code.emit(OpCode.if_icmplt, bexpr_true_l);
          }    

          else if (relOperator.equals(Word.gt.lexeme)){
              if(relOp_is_negated) code.emit(OpCode.if_icmple, bexpr_true_l);
              else  code.emit(OpCode.if_icmpgt, bexpr_true_l);
          }   

          else if (relOperator.equals(Word.eq.lexeme)){
            if(relOp_is_negated) code.emit(OpCode.if_icmpne, bexpr_true_l);
            else  code.emit(OpCode.if_icmpeq, bexpr_true_l);
          }

          else if (relOperator.equals(Word.le.lexeme)){
            if(relOp_is_negated) code.emit(OpCode.if_icmpgt, bexpr_true_l);
            else code.emit(OpCode.if_icmple, bexpr_true_l);
          }    

          else if (relOperator.equals(Word.ne.lexeme)){
            if(relOp_is_negated) code.emit(OpCode.if_icmpeq, bexpr_true_l);
            else code.emit(OpCode.if_icmpne, bexpr_true_l);
          }    
              
          else if (relOperator.equals(Word.ge.lexeme)){
            if(relOp_is_negated) code.emit(OpCode.if_icmplt, bexpr_true_l);
            else code.emit(OpCode.if_icmpge, bexpr_true_l);
          }    
              
          else error("Error: unknown RelOp!");
          
          code.emit(OpCode.GOto, bexpr_false_l); //se falso, evito di scrivere la linea ovunque ma la scrivo una singola volta

          break;
        }

        default: error("Error on bexpr(). Found: " + look.tag);

    }
  }

  private void expr() {
    switch (look.tag) {
    case '+':
      match('+');
      match('(');

      exprlist();

      match(')');
      code.emit(OpCode.iadd);
      break;

    case '-':
      match('-');

      expr();

      expr();

      code.emit(OpCode.isub);
      break;

    case '*':
      match('*');
      match('(');

      exprlist();

      match(')');

      code.emit(OpCode.imul);
      break;

    case '/':
      match('/');

      expr();

      expr();

      code.emit(OpCode.idiv);
      break;

    case Tag.NUM:{
        NumberTok number = (NumberTok) look;
        match(Tag.NUM);
        code.emit(OpCode.ldc, number.number);
        break;
      }

    case Tag.ID:{
        String id_name = ((Word) look).lexeme;
        match(Tag.ID);
        int identifier = st.lookupAddress(id_name);
        if (identifier == -1) {
          error("Trying to load non existing variable: " + id_name);
        } else {
          code.emit(OpCode.iload, identifier);
        }
        break;
      }

    default:
      error("Error on expr(). Found: " + look.tag);
    }
  }

  private void exprlist() {
    switch (look.tag) {
    case '+':
    case '-':
    case '*':
    case '/':
    case Tag.NUM:
    case Tag.ID:{
        expr();

        exprlistp();

        break;
      }

    default: error("Error on exprlist(). Found: " + look.tag);
    }
  }

  private void exprlistp() {
    switch (look.tag) {
    case '+':
    case '-':
    case '*':
    case '/':
    case Tag.NUM:
    case Tag.ID:{
        expr();

        exprlistp();
        break;
      }

    case ')': break;

    default: error("Error on exprlist(). Found: " + look.tag);
    }
  }

  public static void main(String[] args) {
    Lexer lex = new Lexer();
    String path;
    if(args.length == 0){ //se non specifico filename allora apro il file default.lft altrimenti apro args[0]
       path= "./default.lft";
    }else{
       path = args[0];
    }
    
    try {
      BufferedReader br = new BufferedReader(new FileReader(path));
      Translator translator = new Translator(lex, br);
      translator.prog();
      br.close();
    } catch(IOException e) {
      e.printStackTrace();
    }
  }

}