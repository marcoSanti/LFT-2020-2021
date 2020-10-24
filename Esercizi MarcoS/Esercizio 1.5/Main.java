

public class Main {
    public static void main(String[] args){
        System.out.print("\n----Test Accettati----\n");
        System.out.println("Stringa \"Bianchi123456\" accettata? " + DfaScan.scan("Bianchi123456"));
        System.out.println("Stringa \"Rossi654321\"   accettata? " + DfaScan.scan("Rossi654321"));

        System.out.print("\n----Test NON Accettati----\n");
        System.out.println("Stringa \"123456Bianchi\" accettata? " + DfaScan.scan("123456Bianchi"));
        System.out.println("Stringa \"654321Rossi\"   accettata? " + DfaScan.scan("654321Rossi"));
        System.out.println("Stringa \"\"              accettata? " + DfaScan.scan(""));
        System.out.println("Stringa \"654321R8ossi\"  accettata? " + DfaScan.scan("654321R8ossi"));
        System.out.println("Stringa \"654321R ossi\"  accettata? " + DfaScan.scan("654321R ossi"));
        System.out.println("Stringa \"s654321Rossi\"  accettata? " + DfaScan.scan("s654321Rossi"));
        System.out.println("Stringa \"654321Bianchi\" accettata? " + DfaScan.scan("654321Bianchi"));
        System.out.println("Stringa \"123456Rossi\"   accettata? " + DfaScan.scan("123456Rossi"));




    }
}