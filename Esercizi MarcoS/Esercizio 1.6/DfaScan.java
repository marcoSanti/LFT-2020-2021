public class DfaScan{
    public static boolean scan(String str){
        int status = 0;
        int i = 0;

        while(status >=0 && i<str.length()){
            char ch = str.charAt(i++);

            switch(status){
                case 0:
                    if(ch=='a') status = 3;
                    else if(ch == 'b') status = 1;
                    else status = -1;
                    break;

                case 1:
                    if(ch == 'a') status = 3;
                    else if(ch == 'b') status = 2;
                    else status = -1;
                    break;

                case 2:
                    if(ch == 'a') status = 3;
                    else status = -1;
                    break;

                case 3:
                    if(ch=='a' || ch =='b') status = 3;
                    else status = -1;
                    break;

            }


        }
        return status == 3;
    }
}