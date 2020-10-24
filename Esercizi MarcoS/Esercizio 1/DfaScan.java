/**
 * automa che riconosce le stringhe che hanno sequenze di tre zeri
 */

public class DfaScan{
    
    public static boolean scan(String s){
        int state = 0;
        int i = 0;

        while(i<s.length() && state >=0){
            final char ch = s.charAt(i++);

            //switch sugli stati
            switch(state){
                case 0:
                    if(ch=='1') state = 0;
                    else if(ch=='0') state = 1;
                    else state=-1;
                    break;
                
                case 1:
                    if(ch=='1') state = 0;
                    else if(ch=='0') state=2;
                    else state = -1;
                    break;
                
                case 2:
                    if(ch=='1') state = 0;
                    else if(ch=='0') state = 3;
                    else state = -1;
                    break;
                
                case 3:
                    if(ch=='1' || ch=='0') state = 3;
                    else state = -1;
                    break;
                
                default: //aggiunto solo perchè altrimenti l'editor mi segnala un errore e mi da fastidio il sottolineato rosso. funziona anche senza
                    break;
                
            }

        }

        return state == 3;

    }
    
}