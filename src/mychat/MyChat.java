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
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("=== Registration ===");
            
            System.out.print("First name: ");
            String firstName = scanner.nextLine();
            
            System.out.print("Last name: ");
            String lastName = scanner.nextLine();
            
            System.out.print("Username: ");
            String username = scanner.nextLine();
            
            System.out.print("Password: ");
            String password = scanner.nextLine();
            
            System.out.print("Cell number (e.g. +27838968976): ");
            String cellNumber = scanner.nextLine();
            
            Login registration = new Login();
            
            boolean registered = registration.registerUser(
                    firstName, lastName, username, password, cellNumber);
            
            System.out.println(registration.returnUsernameStatus());
            System.out.println(registration.returnPasswordStatus());
            System.out.println(registration.returnCellPhoneNumberStatus());
            
            if (!registered) {
                System.out.println("Registration failed. Please try again.");
                scanner.close();
                return;
            }
            
            System.out.println("\n=== Login ===");
            
            System.out.print("Username: ");
            String loginUsername = scanner.nextLine();
            
            System.out.print("Password: ");
            String loginPassword = scanner.nextLine();
            
            Login login = new Login();
            login.setUsername(loginUsername);
            login.setPassword(loginPassword);
            
            System.out.println(login.returnLoginStatus());
            
            if (!login.loginUser()) {
                scanner.close();
                return;
            }
            
            System.out.println("\nWelcome to QuickChat.");
            
            boolean running = true;
            
            while (running) {
                
                System.out.println("\n1. Send Messages");
                System.out.println("2. Show recently sent messages");
                System.out.println("3. Quit");
                System.out.print("Choose an option: ");
                
                String choice = scanner.nextLine();
                
                switch (choice) {
                    
                    case "1" -> {
                        System.out.print("How many messages would you like to enter? ");
                        
                        int numberOfMessages;
                        
                        try {
                            numberOfMessages =
                                    Integer.parseInt(scanner.nextLine());
                            
                            if (numberOfMessages <= 0) {
                                System.out.println(
                                        "Please enter a number greater than zero.");
                                break;
                            }
                            
                        } catch (NumberFormatException e) {
                            System.out.println("Please enter a valid number.");
                            break;
                        }
                        
                        for (int i = 0; i < numberOfMessages; i++) {
                            
                            System.out.println("\nMessage " + (i + 1));
                            
                            System.out.print("Enter recipient number (+27...): ");
                            String recipient = scanner.nextLine();
                            
                            System.out.print("Enter your message: ");
                            String messageText = scanner.nextLine();
                            
                            Messages messages = new Messages(recipient, messageText);
                            
                            System.out.println("Messages ID: "
                                    + messages.getMessagesID());
                            
                            if (!messages.checkRecipientCell()) {
                                System.out.println(
                                        "Invalid recipient cellphone number.");
                                i--;
                                continue;
                            }
                            
                            if (messages.checkMessageLength()) {
                            } else {
                                System.out.println(
                                        "Message must contain 1 to 250 characters.");
                                i--;
                                continue;
                            }
                            
                            System.out.println("\n1. Send Message");
                            System.out.println("2. Disregard Message");
                            System.out.println("3. Store Message");
                            System.out.print("Choose an option: ");
                            
                            String messageChoice = scanner.nextLine();
                            
                            if (messageChoice.equals("1")
                                    || messageChoice.equals("2")
                                    || messageChoice.equals("3")) {
                                
                                int option = Integer.parseInt(messageChoice);
                                
                                System.out.println(messages.sentMessage(option));
                                
                                if (option == 1) {
                                    System.out.println("Message Hash: "
                                            + messages.getMessageHash());
                                }
                                
                            } else {
                                System.out.println("Invalid message option.");
                                i--;
                            }
                        }
                        
                        System.out.println("\nTotal messages sent: "
                                + Messages.returnTotalMessages());
                    }
                        
                    case "2" -> System.out.println(
                                "Coming Soon.");
                        
                    case "3" -> {
                        running = false;
                        System.out.println("Thank you for using QuickChat.");
                    }
                        
                    default -> System.out.println(
                                "Invalid option. Please select 1, 2 or 3.");
                }
            }
        }
    }
    }