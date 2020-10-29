public class Es_1_4 {
    public static boolean scan(String s)
    {
        int state = 0, i = 0;

        while(i < s.length() && state >= 0)
        {
            final char ch = s.charAt(i++);
            switch(state)
            {
                case 0:
                    if(ch == 32)
                        state = 0;
                    else if((ch >= 48 && ch <= 57)&& (ch%2==0))
                        state = 1;
                    else if((ch >= 48 && ch <= 57)&& !(ch%2==0))
                        state = 2;
                    else 
                        state = -1;
                break;

                case 1:
                    if((ch >= 48 && ch <= 57)&& (ch%2==0))
                        state = 1;
                    else if((ch >= 48 && ch <= 57)&& !(ch%2==0))
                        state = 2;
                    else if(ch == 32)
                        state = 3;
                    else 
                        state = -1;
                break;

                case 2:
                    if((ch >= 48 && ch <= 57)&& (ch%2==0))
                        state = 1;
                    else if((ch >= 48 && ch <= 57)&& !(ch%2==0))
                        state = 2;
                    else if(ch == 32)
                        state = 4;
                    else 
                        state = -1;
                break;

                case 3:
                    if(ch == 32)
                        state = 3;
                    else if((ch >= 65 && ch <= 75))
                        state = 5;
                    else 
                        state = -1;
                break;

                case 4:
                    if(ch == 32)
                        state = 4;
                    else if((ch >= 76 && ch <= 90))
                        state = 6;
                    else 
                        state = -1;
                break;

                case 5:
                    if((ch >= 97 && ch <= 122))
                        state = 5;
                    else 
                        state = -1;
                break;

                case 6:
                    if((ch >= 97 && ch <= 122))
                        state = 6;
                    else 
                        state = -1;
                break;
            }
        }

        return (state == 5 || state == 6);
    }
    
    public static void main(String args[]){
		// Non accetta cognomi spezzati come in esempio
        String s ="123456De Gasperi";
        System.out.println(scan(s) ? "ACCEPTED" : "NOT ACCEPTED");
    
       }
}