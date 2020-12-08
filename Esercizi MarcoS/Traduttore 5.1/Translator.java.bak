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



    public void prog() {
        switch (look.tag) {
            //facendo così rendo il codice più leggibile e meno esteso. in pratica è come se mettessi in or tutti i case!
            case '=':
            case Tag.PRINT:
            case Tag.READ:
            case Tag.COND:
            case Tag.WHILE:
            case '{':
                {
                    int lnext_prog = code.newLabel();
                    statlist(lnext_prog);
                    // code.emitLabel(lnext_prog);
                    match(Tag.EOF);
                    try {
                        code.toJasmin();
                    } catch (java.io.IOException e) {
                        System.out.println("IO error\n");
                    }
                    break;
                }
            default:
                error("Error on prog(). Found: " + look.tag);
        }
    }


    public void statlist(int l_statlist_next) {
        switch (look.tag) {

            case '=':
            case Tag.PRINT:
            case Tag.READ:
            case Tag.COND:
            case Tag.WHILE:
            case '{':
                {
                    int l_stat_next = code.newLabel();
                    stat(l_stat_next);
                    // code.emitLabel(l_stat_next);

                    int l_statlistp_next = code.newLabel();
                    statlistp(l_statlistp_next);
                    //  code.emitLabel(l_statlistp_next);

                    //    code.emit(OpCode.GOto,l_statlist_next); //finito l'esecuzione di goto, vado a statlist.next
                    break;
                }

            default:
                error("Error on statlist(). Found: " + look.tag);
        }
    }





    public void statlistp(int l_statlistp_label) {
        switch (look.tag) {
            case '}':
            case Tag.EOF:
                break;

            case ';':
                {
                    match(';');

                    int l_stat_next = code.newLabel();
                    stat(l_stat_next);
                    //  code.emitLabel(l_stat_next);

                    int l_statlistp1_label = code.newLabel();
                    statlistp(l_statlistp1_label);
                    //    code.emitLabel(l_statlistp1_label);

                    break;
                }

            default:
                error("Error on statlistp(). Found: " + look.tag);
        }

        //  code.emit(OpCode.GOto, l_statlistp_label);
    }






    public void stat(int l_Stat_next) {
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
                {
                    match(Tag.PRINT);
                    match('(');

                    int exprlist_next_l = code.newLabel();
                    exprlist(exprlist_next_l);
                    // code.emitLabel(exprlist_next_l);

                    code.emit(OpCode.invokestatic, 1);

                    match(')');
                    break;
                }

            case '=':
                {
                    match('=');
                    Token id_token = look; //mi salco id del token cos' posso poi avere anche il valore dell'id per potere avere un nuovo elemento nella tabella dei simboli
                    String toke_name = ((Word) id_token).lexeme;
                    match(Tag.ID);

                    int expr_next_l = code.newLabel();
                    expr(expr_next_l); //valuto espressione
                    //code.emitLabel(expr_next_l);


                    int position_of_token_in_st = st.lookupAddress(toke_name);
                    if (position_of_token_in_st == -1) {
                        code.emit(OpCode.istore, count);
                        st.insert(toke_name, count++);
                    } else {
                        code.emit(OpCode.istore, position_of_token_in_st);
                    }
                    break;
                }


            case Tag.COND:
                {
                    match(Tag.COND);
                    int cond_done_l = code.newLabel();

                    int whenlist_next_l = code.newLabel();
                    whenlist(whenlist_next_l, cond_done_l);
                    //code.emitLabel(whenlist_next_l);

                    match(Tag.ELSE);

                    int stat_next_l = code.newLabel(); //ramo else

                    //code.emitLabel(stat_next_l);
                    stat(stat_next_l);
                    code.emitLabel(cond_done_l); //se ho fatto una roba dentro un do allora finisco qua




                    break;
                }

            case Tag.WHILE:
                {
                    int bexpr_true_l = code.newLabel();
                    int bexpr_false_l = code.newLabel();
                    int l_stat1_next = code.newLabel();

                    match(Tag.WHILE);
                    match('(');

                    code.emitLabel(l_stat1_next);
                    bexpr(bexpr_false_l, bexpr_true_l);

                    match(')');

                    code.emit(OpCode.label, bexpr_true_l);

                    stat(l_stat1_next);

                    code.emit(OpCode.GOto, l_stat1_next);

                    code.emitLabel(bexpr_false_l);


                    break;
                }


            case '{':
                {
                    match('{');

                    int statlist_next_l = code.newLabel();
                    statlist(statlist_next_l);
                    //code.emitLabel(statlist_next_l);

                    match('}');
                    break;
                }

            default:
                error("Error on stat(). Found: " + look.tag);
        }

        //   code.emit(OpCode.GOto, l_Stat_next);
    }





    public void whenlist(int whenlist_next_l, int done_l) {
        switch (look.tag) {
            case Tag.WHEN:
                {
                    int whenitem_next_l = code.newLabel();
                    whenitem(whenitem_next_l, done_l);
                    //code.emitLabel(whenitem_next_l);

                    int whenlistp_next_l = code.newLabel();
                    whenlistp(whenlistp_next_l, done_l);
                    //code.emitLabel(whenlistp_next_l);

                    //code.emit(OpCode.GOto, whenlist_next_l);
                    break;
                }

            default:
                error("Error on whenlist(). Found: " + look.tag);
        }
    }





    public void whenlistp(int whenlistp_next_l, int done_l) {
        switch (look.tag) {
            case Tag.WHEN:
                {
                    int whenitem_next_l = code.newLabel();
                    whenitem(whenitem_next_l, done_l);
                    //code.emitLabel(whenitem_next_l);

                    int whenlistp1_next_label = code.newLabel();
                    whenlistp(whenlistp1_next_label, done_l);
                    //code.emitLabel(whenlistp1_next_label);

                    break;
                }

            case Tag.ELSE:
                break;

            default:
                error("Error on whenlistp(). Found: " + look.tag);
        }
        //code.emit(OpCode.GOto, whenlistp_next_l);
    }






    public void whenitem(int whenitem_next_l, int whenitem_done_l) {
        switch (look.tag) {
            case Tag.WHEN:
                {
                    match(Tag.WHEN);
                    match('(');

                    int bexpr_true_l = code.newLabel();
                    int bexpr_false_l = code.newLabel();
                    bexpr(bexpr_false_l, bexpr_true_l);

                    match(')');

                    match(Tag.DO);
                    code.emitLabel(bexpr_true_l);
                    stat(whenitem_done_l); //cambiare in 0
                    code.emit(OpCode.GOto, whenitem_done_l);

                    code.emitLabel(bexpr_false_l);


                    break;
                }

            default:
                error("Error on whenitem(). Found: " + look.tag);
        }

    }



    public void bexpr(int bexpr_false_l, int bexpr_true_l) {
        switch (look.tag) {
            case Tag.RELOP:
                {
                    String relOperator = ((Word) look).lexeme;
                    match(Tag.RELOP);
                    int expr_l_next = code.newLabel(); //inizio di nuovo la valutazione
                    expr(expr_l_next);
                    //code.emitLabel(expr_l_next);
                    expr_l_next = code.newLabel();
                    expr(expr_l_next);
                    if (relOperator.equals(Word.or.lexeme)) {

                        code.emit(OpCode.ior, bexpr_true_l); //se vero
                        code.emit(OpCode.GOto, bexpr_false_l); //se falso 

                    } else if (relOperator.equals(Word.and.lexeme)) {

                        code.emit(OpCode.iand, bexpr_true_l);
                        code.emit(OpCode.GOto, bexpr_false_l);

                    } else if (relOperator.equals(Word.lt.lexeme)) {

                        code.emit(OpCode.if_icmplt, bexpr_true_l);
                        code.emit(OpCode.GOto, bexpr_false_l);

                    } else if (relOperator.equals(Word.gt.lexeme)) {

                        code.emit(OpCode.if_icmpgt, bexpr_true_l);
                        code.emit(OpCode.GOto, bexpr_false_l);

                    } else if (relOperator.equals(Word.eq.lexeme)) {

                        code.emit(OpCode.if_icmpeq, bexpr_true_l);
                        code.emit(OpCode.GOto, bexpr_false_l);

                    } else if (relOperator.equals(Word.le.lexeme)) {

                        code.emit(OpCode.if_icmple, bexpr_true_l);
                        code.emit(OpCode.GOto, bexpr_false_l);

                    } else if (relOperator.equals(Word.ne.lexeme)) {

                        code.emit(OpCode.if_icmpne, bexpr_true_l);
                        code.emit(OpCode.GOto, bexpr_false_l);

                    } else if (relOperator.equals(Word.ge.lexeme)) {

                        code.emit(OpCode.if_icmpge, bexpr_true_l);
                        code.emit(OpCode.GOto, bexpr_false_l);

                    } else {
                        error("Error: unknown RelOp!");
                    }
                    break;
                }



            default:
                error("Error on bexpr(). Found: " + look.tag);
        }
    }



    public void expr(int expr_l_next) {
        switch (look.tag) {
            case '+':
                match('+');
                match('(');

                int exprlist_next_l = code.newLabel();
                exprlist(exprlist_next_l);
                //code.emitLabel(exprlist_next_l);

                match(')');
                code.emit(OpCode.iadd);
                break;

            case '-':
                match('-');

                int expr1_next1_l = code.newLabel();
                expr(expr1_next1_l);
                //code.emitLabel(expr1_next1_l);

                int expr1_next2_l = code.newLabel();
                expr(expr1_next2_l);
                // code.emitLabel(expr1_next2_l);

                code.emit(OpCode.isub);
                break;

            case '*':
                match('*');
                match('(');

                int exprlist_next1_l = code.newLabel();
                exprlist(exprlist_next1_l);
                //code.emitLabel(exprlist_next1_l);

                match(')');

                code.emit(OpCode.imul);
                break;

            case '/':
                match('/');

                int expr1_next_label = code.newLabel();
                expr(expr1_next_label);
                //code.emitLabel(expr1_next_label);

                int expr2_next_label = code.newLabel();
                expr(expr2_next_label);
                //code.emitLabel(expr2_next_label);

                code.emit(OpCode.idiv);
                break;

            case Tag.NUM:
                {
                    NumberTok number = (NumberTok) look;
                    match(Tag.NUM);
                    code.emit(OpCode.ldc, number.number);
                    break;
                }


            case Tag.ID:
                {
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

        // code.emit(OpCode.GOto, expr_l_next);
    }



    public void exprlist(int exprlist_next_l) {
        switch (look.tag) {
            case '+':
            case '-':
            case '*':
            case '/':
            case Tag.NUM:
            case Tag.ID:
                {
                    int expr_next_l = code.newLabel();
                    expr(expr_next_l);
                    // code.emitLabel(expr_next_l);

                    int exprlist1_next_label = code.newLabel();
                    exprlistp(exprlist1_next_label);
                    //code.emitLabel(exprlist1_next_label);

                    break;
                }

            default:
                error("Error on exprlist(). Found: " + look.tag);
        }
        // code.emit(OpCode.GOto, exprlist_next_l);
    }


    public void exprlistp(int exprlistp_next_l) {
        switch (look.tag) {
            case '+':
            case '-':
            case '*':
            case '/':
            case Tag.NUM:
            case Tag.ID:
                {
                    int expr_next_l = code.newLabel();
                    expr(expr_next_l);
                    //code.emitLabel(expr_next_l);

                    int exprlist1_next_label = code.newLabel();
                    exprlistp(exprlist1_next_label);
                    // code.emitLabel(exprlist1_next_label);

                    break;
                }


            case ')':
                break;

            default:
                error("Error on exprlist(). Found: " + look.tag);
        }
        //code.emit(OpCode.GOto, exprlistp_next_l);
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();

        String path = "./test.lft"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Translator translator = new Translator(lex, br);
            translator.prog();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}