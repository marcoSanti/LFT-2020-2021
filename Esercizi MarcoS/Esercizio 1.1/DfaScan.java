/**
 * automa che riconosce le stringhe che non hanno sequenze di tre zeri
 * la differenza con l'automa dello stato precedente in quanto il complementare
 * si fa sugli stati finali e le transizioni sono le medesime
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
                
                default:
                    break;
                
            }

        }

        return (state==0) || (state==1) || (state==2);

    }
    
}