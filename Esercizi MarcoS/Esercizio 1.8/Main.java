

public class Main {
    public static void main(String[] args){
        System.out.print("\n----Test Accettati----\n");
        System.out.println("Stringa \"marco\"     accettata? " + DfaScan.scan("marco"));
        System.out.println("Stringa \"xarco\"     accettata? " + DfaScan.scan("xarco"));
        System.out.println("Stringa \"mxrco\"     accettata? " + DfaScan.scan("mxrco"));
        System.out.println("Stringa \"maxco\"     accettata? " + DfaScan.scan("maxco"));
        System.out.println("Stringa \"marxo\"     accettata? " + DfaScan.scan("marxo"));
        System.out.println("Stringa \"marcx\"     accettata? " + DfaScan.scan("marcx"));
        

        System.out.print("\n----Test NON Accettati----\n");
        System.out.println("Stringa \"marcox\"     accettata? " + DfaScan.scan("marcox"));
        System.out.println("Stringa \"xxarco\"     accettata? " + DfaScan.scan("xxarco"));
        System.out.println("Stringa \"mxxrco\"     accettata? " + DfaScan.scan("mxxrco"));
        System.out.println("Stringa \"maxxco\"     accettata? " + DfaScan.scan("maxxco"));
        System.out.println("Stringa \"marxxo\"     accettata? " + DfaScan.scan("marxxo"));
        System.out.println("Stringa \"marcxx\"     accettata? " + DfaScan.scan("marcxx"));
      




    }
}