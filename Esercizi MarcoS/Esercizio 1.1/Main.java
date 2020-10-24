import java.io.*;

public class Main {
    public static void main(String[] args){
        Boolean result;
        result = DfaScan.scan("01000110");
        
        System.out.println("La stringa 01000110 è accettata? "+ result);

        result = DfaScan.scan("abc");
        System.out.println("La stringa abc è accettata? "+ result);

        result = DfaScan.scan("01001001");
        System.out.println("La stringa 01001001 è accettata? "+ result);
    }
}
