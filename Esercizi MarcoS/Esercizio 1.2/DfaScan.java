/**
 * Progettare e implementare un DFA che riconosca il linguaggio degli identificatori
in un linguaggio in stile Java: un identificatore e una sequenza non vuota di lettere, numeri, ed il `
simbolo di “underscore” _ che non comincia con un numero e che non puo essere composto solo `
dal simbolo _. Compilare e testare il suo funzionamento su un insieme significativo di esempi.
Esempi di stringhe accettate: “x”, “flag1”, “x2y2”, “x 1”, “lft lab”, “ temp”, “x 1 y 2”,
“x ”, “ 5”
Esempi di stringhe non accettate: “5”, “221B”, “123”, “9 to 5”, “ ”
 */

class DfaScan{
    public static boolean scan(String s){
        int state = 0;
        int i = 0;

        while(state >=0 && i<s.length()){
            final char ch = s.charAt(i++);

            switch(state){
                
                case 0: //stato di inizio
                    if((ch>='a' && ch<='z') || (ch>='A' && ch<='Z')) state = 1; //stato finale
                    else if(ch=='_') state = 2; //stato verifica che non ho solo _
                    else if(ch >= '0' && ch <='9') state = -1; //stato pozzo IN QUANTO HO INSERITO UN NUMERO. ESCO DIRETTAMENTE A -1 E RISPARMIO CICLI DI CLOCK
                    else state = -1;
                    break;

                case 1: //stato finale. qua rimango qualsiasi sia il prossimo simbolo di imput su Σ
                    if((ch>='a' && ch<='z') || (ch >='A' && ch <='Z') || (ch>='0' && ch <='9') || (ch=='_')) state = 1; //se ch sta nell'alfabeto
                    else state = -1;
                    break;
                
                case 2:
                    if((ch>='a' && ch<='z') || (ch >='A' && ch <='Z') || (ch>='0' && ch <='9')) state = 1; //se ch non è _ allora vado nello stato finale
                    else if(ch == '_') state = 2;
                    else state = -1;
                    break;
/* IGNORABILE IN QUANTO STATO 3 è POSSO MA LO IMPLEMENTO MANDADO DIRETTAMENTE SUBITO A -1
MANTENGO PER RIFERIMENTO A SCHEMA SU QUADERNO E PER COMPLETEZZA
                case 3:
                    if((ch>='a' && ch<='z') || (ch >='A' && ch <='Z') || (ch>='0' && ch <='9') || (ch=='_')) state = 3; //caso pozzo. non posso uscire e rimango
                    else state = -1; //esco se non ho su sigma
                    break;

*/
            }
        }


        return state == 1;
    }
}