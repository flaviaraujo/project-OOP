package src;

import java.util.Scanner;
import java.util.NoSuchElementException;

public class IO {

    public String readString(Scanner sc) {
        String option = "";
        try {
            option = sc.nextLine();
        }
        catch (NoSuchElementException e) {
            System.out.println("\nEOF detected, exiting...");
            sc.close();
            System.exit(0);
        }
        catch (IllegalStateException e) {
            System.out.println("Scanner is closed, exiting...");
            System.exit(0);
        }
        return option;
    }

    public int readInt(Scanner sc) {
        int option = 0;
        try {
            option = Integer.parseInt(readString(sc));
        }
        catch (NumberFormatException e) {
            return -1;
        }

        return option;
    }

    public String readYesNo(Scanner sc) {
        String option = "";
        while (true) {
            option = readString(sc).toLowerCase();
            if (option.equals("y") || option.equals("n")) {
                break;
            }
            System.out.print("Invalid option. Please enter 'y' or 'n': ");
        }
        return option;
    }
}
