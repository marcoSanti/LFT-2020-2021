/**
 * Progettare e implementare un DFA che riconosca il linguaggio degli identificatori
in un linguaggio in stile Java: un identificatore e una sequenza non vuota di lettere, numeri, ed il `
simbolo di “underscore” _ che non comincia con un numero e che non puo essere composto solo `
dal simbolo _. Compilare e testare il suo funzionamento su un insieme significativo di esempi.
Esempi di stringhe accettate: “x”, “flag1”, “x2y2”, “x 1”, “lft lab”, “ temp”, “x 1 y 2”,
“x ”, “ 5”
Esempi di stringhe non accettate: “5”, “221B”, “123”, “9 to 5”, “ ”
 */


public class Main {
    public static void main(String[] args){
        System.out.print("\n----Test Accettati----\n");
        System.out.println("Stringa \"x\"       accettata? " + DfaScan.scan("x"));
        System.out.println("Stringa \"flag1\"   accettata? " + DfaScan.scan("flag1"));
        System.out.println("Stringa \"x2y2\"    accettata? " + DfaScan.scan("x2y2"));
        System.out.println("Stringa \"x_1\"     accettata? " + DfaScan.scan("x_1"));
        System.out.println("Stringa \"lft_lab\" accettata? " + DfaScan.scan("lft_lab"));
        System.out.println("Stringa \"temp\"    accettata? " + DfaScan.scan("temp"));
        System.out.println("Stringa \"x_1_y_2\" accettata? " + DfaScan.scan("x_1_y_2"));
        System.out.println("Stringa \"x_\"      accettata? " + DfaScan.scan("x_"));
        System.out.println("Stringa \"_5\"      accettata? " + DfaScan.scan("_5"));
        System.out.println("Stringa \"Bau\"     accettata? " + DfaScan.scan("Bau"));
        System.out.print("\n----Test NON Accettati----\n");
        System.out.println("Stringa \"5\"       accettata? " + DfaScan.scan("5"));
        System.out.println("Stringa \"221B\"    accettata? " + DfaScan.scan("221B"));
        System.out.println("Stringa \"123\"     accettata? " + DfaScan.scan("123"));
        System.out.println("Stringa \"9_to_5\"  accettata? " + DfaScan.scan("9_to_5"));
        System.out.println("Stringa \"___\"     accettata? " + DfaScan.scan("___"));
        System.out.println("Stringa \"\"        accettata? " + DfaScan.scan(""));



    }
}
