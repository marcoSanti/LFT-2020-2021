public class DfaScan{
    public static boolean scan(String str){
        int status = 0;
        int i = 0;

        while(status >=0 && i<str.length()){
            char ch = str.charAt(i++);

            switch(status){
                case 0:
                    if(ch == '/') status = 1;
                    else if(ch == 'a' || ch == '*') status = 0;
                    else status = -1;
                    break;
                
                case 1:
                    if(ch == '/') status = 1;
                    else if(ch == 'a') status = 0;
                    else if(ch == '*') status = 2;
                    else status = -1;
                    break;
                
                case 2:
                    if(ch == 'a' || ch == '/') status = 2;
                    else if(ch == '*') status = 3;
                    else status = -1;
                    break;
                
                case 3:
                    if(ch=='*') status = 3;
                    else if(ch == 'a') status = 2;
                    else if(ch == '/') status = 0;
                    else status = -1;
                    break;

               

            }
            

        }
        return status==0 || status == 1;
    }
}