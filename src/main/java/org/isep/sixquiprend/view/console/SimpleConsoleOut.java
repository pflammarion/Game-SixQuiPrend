package org.isep.sixquiprend.view.console;

import java.io.IOException;

public class SimpleConsoleOut {
    public void print(String txt){
        System.out.println(txt);
    }

    public void printNoLine(String txt){
        System.out.print(txt);
    }

    public void pressEnterToContinue() {
        print("Press Enter key to continue...");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //function to clear console (technically skipping / doing empty lines)
    public void clearConsole() {
        for (int i = 0; i < 100; i++)
            print("");
    }

    //function for separator
    public void printSeparator(int n) {
        for (int i = 0; i < n; i++)
            printNoLine("-");
        print("");
    }

    //function to print a heading
    public void printHeading(String title) {
        printSeparator(title.length());
        print(title);
        printSeparator(title.length());
    }

    //clears Console and does the heading afterwards
    public void printHeader(String title){
        clearConsole();
        printHeading(title);
    }
}

