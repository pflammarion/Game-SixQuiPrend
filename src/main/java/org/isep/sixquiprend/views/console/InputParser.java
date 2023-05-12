package org.isep.sixquiprend.views.console;

import java.io.InputStream;
import java.util.Scanner;

public class InputParser {
    Scanner sc;

    public InputParser(InputStream is) {
        this.sc = new Scanner(is);
    }

    public int getInt(){
        while(!sc.hasNextInt()){
            sc.nextLine();  // flushes line
        }
        return sc.nextInt();
    }

    public int getIntInRange(int low, int high){
        int val = getInt();
        while(val < low || val > high){
            val = getInt();
        }
        return val;
    }

    public String getString(String pattern){
        while(!sc.hasNext(pattern)){
            System.out.println("Wrong input. Try again!");
            sc.nextLine();
        }
        return sc.next(pattern);
    }

    public void closeScanner(){
        sc.close();
    }

    public static void main(String[] args) {
        InputParser inputParser = new InputParser(System.in);
        System.out.println("Enter a name");
        String answer = inputParser.getString("\\w+");
        System.out.printf("Your answer is: %s%n", answer);
    }
}