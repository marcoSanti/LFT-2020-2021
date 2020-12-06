import java.io.*;

public class Translator {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;
    
    SymbolTable st = new SymbolTable();
    CodeGenerator code = new CodeGenerator();
    int count=0;

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
        for(int i = 1; i<lex.lineChar-1; i++)lex.currentLine += ' ';
        lex.currentLine+='^';
        throw new Error(s + " @<Line:" + lex.line+ "> -> <Column:" + lex.lineChar+">\n\n" + lex.currentLine + "\n");
    }

    void match(int t) {
        if (look.tag == t) {
            if (look.tag != Tag.EOF) move();
        } else error("syntax error at line: " + Lexer.line + "\n token: " + t + " does not match expected token: " + look.tag);
    }



    public void prog() {        
	 switch(look.tag){
            //facendo così rendo il codice più leggibile e meno esteso. in pratica è come se mettessi in or tutti i case!
            case '=':
            case Tag.PRINT:
            case Tag.READ:
            case Tag.COND:
            case Tag.WHILE:
            case '{':{
                int lnext_prog = code.newLabel();
                statlist(lnext_prog);
                code.emitLabel(lnext_prog);
                match(Tag.EOF);
                try {
                    code.toJasmin();
                }
                catch(java.io.IOException e) {
                    System.out.println("IO error\n");
                }
            }
            default : error("Error on prog(). Found: " + look.tag);
        }
    }


    public void statlist(int l_statlist_next){
        switch(look.tag){

            case '=':
            case Tag.PRINT:
            case Tag.READ:
            case Tag.COND:
            case Tag.WHILE:
            case '{':{
                int l_stat_next= code.newLabel();
                stat();
                code.emitLabel(l_stat_next);
                statlistp(l_statlist_next);
                break;
            }
    
            default : error("Error on statlist(). Found: " + look.tag);
          }
    }

    public void statlistp(int l_statlistp_label){
        switch(look.tag){
            case '}':
            case Tag.EOF:
                break; //NON SO CHE FARE PER LA PRODUZIONE IN EPS
    
            case ';':{
                match(';');
                int l_stat_next = code.newLabel();
                stat();
                code.emitLabel(l_stat_next);
                statlistp(l_statlistp_label);

                break;
            }
    
            default: error("Error on statlistp(). Found: " + look.tag);
          }
    }

    public void stat( int l_Stat_next) {
        switch(look.tag) {
            case Tag.READ:{
                match(Tag.READ);
                match('(');
                if (look.tag==Tag.ID) {
                    int id_addr = st.lookupAddress(((Word)look).lexeme);
                    if (id_addr==-1) {
                        id_addr = count;
                        st.insert(((Word)look).lexeme,count++);
                    }                    
                    match(Tag.ID);
                    match(')');
                    code.emit(OpCode.invokestatic,0);
                    code.emit(OpCode.istore,id_addr);   
                }
                else
                    error("Error in grammar (stat) after read( with " + look);
                break;
            }
            
            case Tag.PRINT:{
                match(Tag.PRINT);
                match('(');
                exprlist();
                code.emit(OpCode.invokestatic, 1);
                match(')');
                break;
            }
            //COMPLETAREEEEEEEEEEE
        }
     }

    private void expr( /* completare */ ) {
        switch(look.tag) {
	// ... completare ...
            case '-':
                match('-');
                expr();
                expr();
                code.emit(OpCode.isub);
                break;
	// ... completare ...
        }
    }

// ... completare ...
}

