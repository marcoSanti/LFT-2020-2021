public class Es_1_5{
	public static boolean scan(String s){
	
		 int state = 0, i=0;
		 while(state >= 0 && i < s.length())
		 {
			 final char ch = s.charAt(i++);

			 switch(state){
				case 0:
				if((ch >= 65 && ch <= 75))
					state = 1;
				else if((ch >= 76 && ch <= 90))
					state = 2;
				else 
					state = -1;
				break;

			case 1:
				if((ch >= 65 && ch <= 75))
					state = 1;
				else if((ch >= 76 && ch <= 90))
					state = 2;
				else if((ch >= 48 && ch <= 57) && (ch%2==0))
					return true;
				else if((ch >= 48 && ch <= 57) && !(ch%2==0))
					return false;
				else 
					state = -1;
				break;

			case 2:
				if((ch >= 65 && ch <= 75))
					state = 1;
				 else if((ch >= 76 && ch <= 90))
					state = 2;
				else if((ch >= 48 && ch <= 57) && !(ch%2==0))
					return false;
				else if((ch >= 48 && ch <= 57) && (ch%2==0))
					return true;
				else 
					state = -1;
				break;
		   }
	   }
	   
	   return true;
   }
  
   public static void main(String args[]){
		String s ="123456Rossi";
		System.out.println(scan(s) ? "ACCEPTED" : "NOT ACCEPTED");

   }
}