

public class Main {
    public static void main(String[] args){
        System.out.print("\n----Test Accettati----\n");
        System.out.println("Stringa \"/****/\"        accettata? " + DfaScan.scan("/****/"));
        System.out.println("Stringa \"/*a*a*/\"       accettata? " + DfaScan.scan("/*a*a*/"));
        System.out.println("Stringa \"/*a/**/\"       accettata? " + DfaScan.scan("/*a/**/"));
        System.out.println("Stringa \"/**a///a/a**/\" accettata? " + DfaScan.scan("/**a///a/a**/"));
        System.out.println("Stringa \"/*/*/\"         accettata? " + DfaScan.scan("/*/*/"));
        System.out.println("Stringa \"/**/\"          accettata? " + DfaScan.scan("/**/"));

        System.out.print("\n----Test NON Accettati----\n");
        System.out.println("Stringa \"bbbabbb\"  accettata? " + DfaScan.scan("bbbabbb"));
        System.out.println("Stringa \"\"         accettata? " + DfaScan.scan(""));
        System.out.println("Stringa \"bbbbbbb\"  accettata? " + DfaScan.scan("bbbbbbb"));
        System.out.println("Stringa \"/*/\"      accettata? " + DfaScan.scan("/*/"));
        System.out.println("Stringa \"/**/***/\" accettata? " + DfaScan.scan("/**/***/"));
      




    }
}