

public class Main {
    public static void main(String[] args){
        System.out.print("\n----Test Accettati----\n");
        System.out.println("Stringa \"aaa\"     accettata? " + DfaScan.scan("aaa"));
        System.out.println("Stringa \"abb\"     accettata? " + DfaScan.scan("abb"));
        System.out.println("Stringa \"bab\"     accettata? " + DfaScan.scan("bab"));
        System.out.println("Stringa \"bbabbba\" accettata? " + DfaScan.scan("bbabbba"));

        System.out.print("\n----Test NON Accettati----\n");
        System.out.println("Stringa \"bbbabbb\" accettata? " + DfaScan.scan("bbbabbb"));
        System.out.println("Stringa \"\"        accettata? " + DfaScan.scan(""));
        System.out.println("Stringa \"bbbbbbb\" accettata? " + DfaScan.scan("bbbbbbb"));
      




    }
}