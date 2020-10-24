import java.io.*;

public class Main {
    public static void main(String[] args){
        Boolean result;
        result = DfaScan.scan("0100011101");
        
        System.out.println("La stringa 0100011101 è accettata? "+ result);

        result = DfaScan.scan("abc");
        System.out.println("La stringa abc è accettata? "+ result);

        result = DfaScan.scan("01010101");
        System.out.println("La stringa 01010101 è accettata? "+ result);
    }
}
