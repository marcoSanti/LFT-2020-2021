

public class Main {
    public static void main(String[] args){
        System.out.print("\n----Test Accettati----\n");
        System.out.println("Stringa \"654321 Rossi Ma\"       accettata? " + DfaScan.scan("654321 Rossi Ma"));
        System.out.println("Stringa \"123455  Bianchi \"      accettata? " + DfaScan.scan("123456  Bianchi"));
        System.out.println("Stringa \"654321 Ve Rossi\"       accettata? " + DfaScan.scan("654321 Ve Rossi"));

        System.out.print("\n----Test NON Accettati----\n");
        System.out.println("Stringa \"\"              accettata? " + DfaScan.scan(""));
        System.out.println("Stringa \"654321 De rossi\" accettata? " + DfaScan.scan("654321 De Rossi"));
        System.out.println("Stringa \"654321R8ossi\"    accettata? " + DfaScan.scan("654321R8ossi"));
        System.out.println("Stringa \"s654321Rossi\"    accettata? " + DfaScan.scan("s654321Rossi"));
        System.out.println("Stringa \"654321Bianchi\"   accettata? " + DfaScan.scan("654321Bianchi"));
        System.out.println("Stringa \"123456Rossi\"     accettata? " + DfaScan.scan("123456Rossi"));




    }
}