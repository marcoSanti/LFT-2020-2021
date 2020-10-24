

 public class DfaScan{ 
     public static boolean scan(String s){
        int status = 0, i = 0;

        while(i<s.length() && status >=0){
            
            final char ch = s.charAt(i++);

            switch(status){

                case 0: //caso iniziale
                   if(isNumber(ch)){
                       if(pari(ch)) status = 1;
                       else status = 2;
                   }else status = -1; //non implemento uno stato pozzo ma faccio uscire tutto il programma direttamente
                   break;
                
                case 1:
                   if(isNumber(ch)){
                    if(pari(ch)) status = 1;
                    else status = 2;
                   }else if(isSpace(ch)) status = 1;
                   else if(isAK(ch) && isUpperCase(ch)) status = 3;
                   else status = -1;
                   break;
                
                case 2:
                    if(isNumber(ch)){
                        if(pari(ch)) status = 1;
                        else status = 2;
                    }else if(isSpace(ch)) status = 2;
                    else if(isLZ(ch) && isUpperCase(ch)) status = 3;
                    else status = -1;
                    break;

                case 3:
                    if(isLowerCase(ch)) status = 3;
                    else if(isSpace(ch)) status = 4;
                    else status = -1;
                    break;

                case 4:
                    if(isUpperCase(ch)) status = 3;
                    else if(isSpace(ch)) status = 4;
                    else if(isAK(ch) || isLZ(ch)) status = 3;
                    else status = -1;
                    break;
                    

            }
              
        }

        return status == 3;
     }


     //funzioni di supporto per non impazzire

     private static boolean isLowerCase(char ch){ return (ch>='a' && ch<='z'); } // aggiungo a char anche lo spazio per comodità

     private static boolean isNumber(char ch){ return (ch>='0' && ch<='9'); }

     private static boolean pari(char ch) { return (int)ch%2==0; }

     private static boolean isAK(char ch){ return (ch>='A' && ch<='K') || (ch>='a' && ch<='k'); }

     private static boolean isLZ(char ch){ return (ch>='L' && ch<='Z') || (ch>='l' && ch<='Z'); }

     private static boolean isSpace(char ch){ return ch == ' '; }

     private static boolean isUpperCase(char ch){ return (ch >= 'A') && (ch <= 'Z'); }

 }