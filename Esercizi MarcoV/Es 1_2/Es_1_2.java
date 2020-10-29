public class Es_1_2{
    public static boolean scan(String s){ 
        int state = 0;//stato iniziale q0 (l'automa non ha ricevuto niente in input!
	int i = 0;
        while (state >= 0 && i < s.length()) {
	    final char ch = s.charAt(i++);
	    switch(state) 
            {
                case 0:
                    if(ch == 95)
                        state = 2;//q0
                    else if((ch >= 65 && ch <= 90) || (ch >= 97 && ch <= 122))
                        state = 1;//q1
                    else if((ch >= 48 && ch <= 57))
                        state = -1;//q2 = stato pozzo
                    else 
                        state = -1;//q2
                break;

                case 1:
                    if((ch >= 65 && ch <= 90) || (ch >= 97 && ch <= 122))
                        state = 1;
                    else if(ch == 95 || (ch >= 48 && ch <= 57))
                        state = 1;
                    else 
                        state = -1;
                break;

                case 2:
                    if(ch == 95)//legge _
                        state = 2;
                    else if((ch >= 65 && ch <= 90) || (ch >= 97 && ch <= 122))//legge A...Z oppure a...z
                        state = 1;
                    else if(ch >= 48 && ch <= 57)//legge 0...9
                        state = 1;
                    else 
                        state = -1;//sttao pozzo
                break;
            }
        }
        return state == 1 ;
    } 
    
    public static void main(String args[]){
        String s = "abba_";
       System.out.println(scan(s) ? "ACCEPTED" : "NOT ACCEPTED");
    
    }

}