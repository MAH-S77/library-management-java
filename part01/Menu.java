package part01;

import java.util.Scanner;

public class Menu {
    private String[] options;
    private String title;
    
    public Menu(String title, String[] options) {
        this.title = title;
        this.options = options;
    }
    
    public int getChoice() {
        Scanner scanner = new Scanner(System.in);
        int choice = 0;
        boolean validInput = false;
        
        while (!validInput) {
            System.out.println("\n" + title);
            System.out.println("=".repeat(title.length()));
            
            // Display menu options
            for (int i = 0; i < options.length; i++) {
                System.out.println((i + 1) + ". " + options[i]);
            }
            
            System.out.print("\nEnter your choice (1-" + options.length + "): ");
            
            try {
                choice = Integer.parseInt(scanner.nextLine());
                if (choice >= 1 && choice <= options.length) {
                    validInput = true;
                } else {
                    System.out.println("Error: Please enter a number between 1 and " + options.length);
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid number");
            }
        }
        
        return choice;
    }
}