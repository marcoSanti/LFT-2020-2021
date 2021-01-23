public class DfaScan{
    public static boolean scan(String str){
        int status = 0;
        int i = 0;

        while(status >=0 && i<str.length()){
            char ch = str.charAt(i++);

            switch(status){
                case 0:
                    if(ch == 'm') status = 1;
                    else if(Character.isLetter(ch)) status = 6;
                    else status = -1;
                    break;

                case 1:
                    if(ch=='a') status = 2;
                    else if(Character.isLetter(ch)) status = 7;
                    else status = -1;
                    break;

                case 2:
                    if(ch == 'r') status = 3;
                    else if(Character.isLetter(ch)) status = 8;
                    else status = -1;
                    break;

                case 3:
                    if(ch=='c') status = 4;
                    else if(Character.isLetter(ch)) status = 9;
                    else status = -1;
                    break;

                case 4:
                    if(Character.isLetter(ch)) status = 5;
                    else status = -1;
                    break;

                case 5: //se sono qua e ho altre lettere da vedere ho aggiunto al nome e quindi non va bene e devo morire come automa
                    status = -1;
                    break;
               
                case 6:
                    if(ch=='a') status = 7;
                    else status = -1;
                    break;
                   
                case 7:
                    if(ch=='r') status = 8;
                    else status = -1;
                    break;

                case 8:
                    if(ch=='c') status = 9;
                    else status = -1;
                    break;
                
                case 9:
                    if(ch=='o') status = 5;
                    else status = -1;
                    break;

                default: status = -1; break;
            }


        }
        return status==5;
    }
}