 public class DfaScan{
     public static boolean scan(String s){
        int status = 0, i = 0;

        while(i<s.length() && status >=0){
            
            final char ch = s.charAt(i++);

            switch(status){ 
                case 0:
                    if(isChar(ch)){

                        status = -1; //stato pozzo che indica che il nome inizia per char e non per numero

                    } else if(isNumber(ch)){ //controllo che sia un char

                        if(pari(ch)){
                            status = 1; //numero pari
                       }
                       else {
                            status = 2;
                       }

                    } else {
                        status = -1;
                    }
                    break;


                case 1: 
                    if(isAK(ch)){
                        status = 3; //stato finale
                    } else if (isLZ(ch)){

                      status = -1; //stato non finale 

                    }else if(isNumber(ch)){ //controllo che sia un char

                        if(pari(ch)){
                             status = 1; //numero pari
                        }
                        else {
                             status = 2;
                        }
                    }
                    else status = -1;
                    break;

                case 2:
                    if (isAK(ch)){
                       status = -1; 
                    } else if (isLZ(ch)){

                        status = 3; 

                    }else if(isNumber(ch)){ //controllo che sia un char

                        if(pari(ch)){
                            status = 1; //numero pari
                       }
                       else {
                            status = 2;
                       }

                    }else{
                        status = -1;
                    } 
                    break;


                case 3:
                    if(isChar(ch)){

                        status = 3; //matricola:cognome riconosciuto

                    }else{

                       status = -1; //stato pozzo cognomi non validi

                    }
                    break;

            }
           
        }

        return status == 3;
     }


     //funzioni di supporto per rendere il codice più leggibile

     private static boolean isChar(char ch){ return ((ch>='a' && ch<='z') || (ch>='A' && ch<='Z')); }

     private static boolean isNumber(char ch){ return (ch>='0' && ch<='9'); }

     private static boolean pari(char ch) { return (int)ch%2==0; }

     private static boolean isAK(char ch){ return (ch>='A' && ch<='K') || (ch>='a' && ch<='k'); }

     private static boolean isLZ(char ch){ return (ch>='L' && ch<='Z') || (ch>='l' && ch<='Z'); }

 }