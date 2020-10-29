public class Es_1_6{

    public static boolean scan(String s){
          int state = 0, i=0;
             while(state >= 0 && i < s.length())
             {
                 final char ch = s.charAt(i++);
                 switch(state){
                               
                              //caso 0 --> sono in q0 leggo a, oppure leggo b, ma se leggo una lettera che non è nè a nè b vado a -1;
                              case 0: 
                                       if(ch == 'a')
                                          state = 3;
                                       else if(ch == 'b')
                                          state = 1;
                                       else state = -1;
                                     break;
                              
                              case 1:  //se riconosco a vado in stato 3 e se in stato 3 riconosco #a o #b resto in stato 3
                                      if(ch == 'a'){
                                         state = 3;//mi posiziono nello stato 3 già
                                         if( ch == 'a' || ch == 'b')
                                            state = 3;
                                      }
                                      else if(ch == 'b'){
                                             state = 1;
                                             //se leggo una a vado in stato q3
                                             if(ch == 'a')
                                               state = 3;//sono in q3
                                             else state = 2;
                                            }
                                      else state = -1;//leggo altri caratteri
                              
                              case 2: 
                                      if(ch == 'a'){
                                         state = 3;//mi posiziono nello stato 3 già
                                         if( ch == 'a' || ch == 'b')
                                           state = 3;
                                      }
                                      else if(ch == 'b'){
                                             state = 1;
                                             //se leggo una a vado in stato q3
                                             if(ch == 'a')
                                               state = 3;//sono in q3
                                             else if(ch == 'b')
                                                state = 2;
                                                if(ch == 'a')
                                                 state = 3;
                                            }
                                      else state = -1;//leggo altri caratteri                                      
                               
                 }
             }
            //ritorno true se lo stato è finale (ossia = 3)    
            return state == 3;
    }
    
    public static void main(String[] args){
		String s ="bbbababab";
		System.out.println(scan(s) ? "ACCEPTED" : "NOT ACCEPTED");
    }

}