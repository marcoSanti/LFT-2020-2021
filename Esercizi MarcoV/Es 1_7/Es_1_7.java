public class Es_1_7{

    public static boolean scan(String s){
          int state = 0, i=0;
             while(state >= 0 && i < s.length())
             {
                 final char ch = s.charAt(i++);

                 switch(state){
                               
                              //caso 0 --> sono in q0 se leggo a vado in q1 (stato finale), oppure se leggo b resto in q0, ma se leggo una lettera che non è nè a nè b vado a -1;
                              case 0: 
                                       if(ch == 'a')
                                         state = 1;//1° stato finale.
                                       else if(ch == 'b') 
                                         state = 0;
                                       break;
                              
                              case 1: 
                                       if(ch == 'a')
                                         state = 1;//1° stato finale.
                                       else if(ch == 'b')//leggo b e vado in state Q3
                                             state = 2;//2° stato finale
                                       break;
                             
                              case 2: 
                                      if(ch == 'a')
                                         state = 1;
                                      else if(ch == 'b')//leggo b e vado in state Q3
                                         state = 3;
                                      break;
                              case 3: 
                                      if(ch == 'a')
                                         state = 1;
                                      else if(ch == 'b')//leggo b e vado in state Q3
                                         state = 0;
                                      break;
                 }
             }
            //ritorno true se lo stato è finale (ossia o 1 o 2 o 3)    
            return state == 1 || state == 2 || state == 3;
    }
    
    public static void main(String[] args){
		String s= "abbbbbbbb";
		System.out.println(scan(s) ? "ACCEPTED" : "NOT ACCEPTED");
	}
}
