import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        boolean exit = false;
        Scanner sc = new Scanner(System.in);
        while(!exit){
            System.out.print("Test: ");
            String test = sc.nextLine();
            System.out.println(DfaScan.scan(test));
        }
      




    }
}