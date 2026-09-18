/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package mychat;
import java.util.Scanner;

/**
 *
 * @author Student
 */
public class MyChat {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
          Scanner scanner = new Scanner(System.in);

        System.out.println("=== Registration ===");
        System.out.print("First name: ");
        String firstName = scanner.nextLine();
        System.out.print("Last name: ");
        String lastName = scanner.nextLine();
        System.out.print("Username (must contain '_' and be <= 6 characters): ");
        String username = scanner.nextLine();
        System.out.print("Password (8+ chars, capital letter, number, special char): ");
        String password = scanner.nextLine();
        System.out.print("Cell number (e.g. +27838968976): ");
        String cellPhoneNumber = scanner.nextLine();
     // Prompt the user to log in using the credentials they just registered
        Login registration = new Login();
        boolean registered = registration.registerUser(firstName, lastName, username, password, cellPhoneNumber);

        System.out.println(registration.returnUsernameStatus());
        System.out.println(registration.returnPasswordStatus());
        System.out.println(registration.returnCellPhoneNumberStatus());

        if (!registered) {
            System.out.println("\nRegistration failed - fix the fields above and restart.");
            return;
        }

        System.out.println("\n=== Login ===");
        System.out.print("Username: ");
        String loginUsername = scanner.nextLine();
        System.out.print("Password: ");
        String loginPassword = scanner.nextLine();

        Login loginAttempt = new Login();
        loginAttempt.setUsername(loginUsername);
        loginAttempt.setPassword(loginPassword);

        System.out.println(loginAttempt.returnLoginStatus());
    }
    }
       



      
