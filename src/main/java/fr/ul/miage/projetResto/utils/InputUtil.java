package fr.ul.miage.projetResto.utils;


import java.util.Scanner;

public class InputUtil {
    private static final Scanner scanner = new Scanner(System.in);

    public static String getUserInput(){
        return scanner.next();
    }

    public static void closeScanner(){
        scanner.close();
    }
}
