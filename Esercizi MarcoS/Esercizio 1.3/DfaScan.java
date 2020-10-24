

 public class DfaScan{
     public static boolean scan(String s){
        int status = 0, i = 0;

        while(i<s.length() && status >=0){
            
            final char ch = s.charAt(i++);

            switch(status){ 
                case 0:
                    if(isChar(ch)){

                        status = 1; //stato pozzo che indica che il nome inizia per char e non per numero

                    } else if(isNumber(ch)){ //controllo che sia un char

                        if(pari(ch)){
                            status = 2; //numero pari
                       }
                       else {
                            status = 3;
                       }

                    } else {
                        status = -1;
                    }
                    break;
                




                case 1:
                    if(isChar(ch)){
                        status = 1; //stato pozzo per ingreso primo char e non num
                    } else{
                        status = -1;
                    } 
                    break;





                case 2: //occhio che prima devo controllare se è un char accettato!
                    if(isAK(ch)){
                        status = 4; //stato finale per T2
                    } else if (isLZ(ch)){

                       status = 5; //stato non finale per T4 

                    }else if(isNumber(ch)){ //controllo che sia un char

                        if(pari(ch)){
                             status = 2; //numero pari
                        }
                        else {
                             status = 3;
                        }
                    }
                    else status = -1;
                    break;




                case 3:
                    if (isAK(ch)){

                        status = 5; //stato finale per T1
                    } else if (isLZ(ch)){

                        status = 4; //stato non finale per T3

                    }else if(isNumber(ch)){ //controllo che sia un char

                        if(pari(ch)){
                            status = 2; //numero pari
                       }
                       else {
                            status = 3;
                       }

                    }else{
                        status = -1;
                    } 
                    break;





                case 4:
                    if(isChar(ch)){

                        status = 4; //pozzo per matricola:cognome rriconosciuto

                    }else  if (isNumber(ch)){

                        status = 6; //stato pozzo cognomi non validi

                    }else{
                        
                        status = -1;
                    }
                    break;
                


                case 5:
                    if(isChar(ch)){

                        status = 5; //pozzo per matricola:cognome non rriconosciuto

                    }else if (isNumber(ch)){

                        status = 6; //stato pozzo cognomi non validi

                    }else{

                        status = -1;

                    } 
                    break;
                


                case 6:
                    if(isValid(ch)){
                        status = 6; //stato pozzo per cognomi non validi
                    }else{
                        status = -1;
                    } 
                    break;


            }
           
        }

        return status == 4;
     }


     //funzioni di supporto per non impazzire

     private static boolean isChar(char ch){ return ((ch>='a' && ch<='z') || (ch>='A' && ch<='Z')); }

     private static boolean isNumber(char ch){ return (ch>='0' && ch<='9'); }

     private static boolean pari(char ch) { return (int)ch%2==0; }

     private static boolean isAK(char ch){ return (ch>='A' && ch<='K') || (ch>='a' && ch<='k'); }

     private static boolean isLZ(char ch){ return (ch>='L' && ch<='Z') || (ch>='l' && ch<='Z'); }

     private static boolean isValid(char ch){ return (ch>='0' && ch <='9')||(ch >='a' && ch <='z')||(ch >= 'A' && ch <='Z'); }


 }