public class Es_1_9{

   public static boolean scan(String s){
   
      int state = 0, i = 0;
      
      while(state >= 0 && i < s.length())
          {
              final char ch = s.charAt(i++);
              char slash = '/';
              char ast =  '*';
              char mychar = 'a';
              switch(state){
              
                             case 0: //sono in q0 e leggo slash
                                     if(ch == slash)
                                       state = 1;
                                     else state = -1;
                                     break;
                                     
                             case 1: 
                                     if(ch == ast)
                                        state = 2;
                                     else state = -1;
                                     break;
                             
                             case 2: 
                                     if(ch == ast)
                                        state = 3;
                                     else if(ch == slash)
                                        state = 5;
                                     else state = 2;
                                     break;
                             
                             case 3: 
                                     if(ch == ast)
                                       state = 3;
                                     else if(ch == slash)
                                       state = 4;
                                     else state = 2;
                                     
                                     break;
                            
                             case 4: 
                                     if(ch == ast || ch == slash || ch != ast || ch != slash)
                                          state = -1;
                                     break;
                             
                             case 5:
                                    if(ch == ast)
                                       state = 3;
                                    else if(ch == slash)
                                       state = 5;
                                    else state = 2;
                                    break;
                                    
              }
       }
     return state == 4;
   } 
   
   public static void main(String[] args){
      String s= "/*/";
      System.out.println(scan(s) ? "ACCEPTED" : "NOT ACCEPTED");
   
   }


}