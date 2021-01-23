

 public class DfaScan{
     public static boolean scan(String s){
        int status = 0, i = 0;

        while(i<s.length() && status >=0){
            
            final char ch = s.charAt(i++);

            switch(status){
                
                case 0:
                    if(isSpace(ch)) status = 0;
                    else if(isAK(ch)) status = 1;
                    else if(isLZ(ch)) status = 2;
                    else status = -1;
                    break;

                case 1:
                    if(isChar(ch)) status = 1;
                    else if(isNumber(ch)){
                        if(pari(ch)) status = 3;
                        else status = 4;
                    } else status = -1;
                    break;

                case 2:
                    if(isChar(ch)) status = 2;
                    else if(isNumber(ch)){
                        if(pari(ch)) status = 6;
                        else status = 5;
                    } else status = -1;
                    break;

                case 3:
                    if(isNumber(ch)){
                        if(pari(ch)) status = 3;
                        else status = 4;
                    }else status = -1;
                    break;
                
                case 4:
                    if(isNumber(ch)){
                        if(pari(ch)) status = 3;
                        else status = 4;
                    }else status = -1;
                    break;

                case 5:
                    if(isNumber(ch)){
                        if(pari(ch)) status = 6;
                        else status = 5;
                    }else status = -1;
                    break;
                
                case 6:
                    if(isNumber(ch)){
                        if(pari(ch)) status = 6;
                        else status = 5;
                    }else status = -1;
                    break;
    

            }
              
           
        }

        return status == 3 || status == 5;
     }


     //funzioni di supporto per non impazzire

     private static boolean isChar(char ch){ return ((ch>='a' && ch<='z') || (ch>='A' && ch<='Z') || (ch == ' ')); } // aggiungo a char anche lo spazio per comodità

     private static boolean isNumber(char ch){ return (ch>='0' && ch<='9'); }

     private static boolean pari(char ch) { return (int)ch%2==0; }

     private static boolean isAK(char ch){ return (ch>='A' && ch<='K') || (ch>='a' && ch<='k'); }

     private static boolean isLZ(char ch){ return (ch>='L' && ch<='Z') || (ch>='l' && ch<='Z'); }

     private static boolean isSpace(char ch){ return ch == ' '; }

 }