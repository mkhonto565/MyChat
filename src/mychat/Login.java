/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mychat;
import java.util.regex.Pattern;

/**
 *
 * @author Student
 */
public class Login {
     private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String cellPhoneNumber;

    private static Login registeredUser;

    private boolean usernameValid;
    private boolean passwordValid;
    private boolean cellPhoneNumberValid;

    public Login() {
    }

    public Login(String firstName, String lastName, String username, String password, String cellPhoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.cellPhoneNumber = cellPhoneNumber;
    }

    public boolean checkUserName(String username) {
        this.username = username;
        String regex = "^(?=.{1,5}$)(?=.*_).*$";
        usernameValid = username != null && Pattern.matches(regex, username);
        return usernameValid;
    }

    public boolean checkPasswordComplexity(String password) {
        this.password = password;
        String regex = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()\\-_=+{}\\[\\]|:;\"'<>,.?/~`]).{8,}$";
        passwordValid = password != null && Pattern.matches(regex, password);
        return passwordValid;
    }

    public boolean checkCellPhoneNumber(String cellPhoneNumber) {
        this.cellPhoneNumber = cellPhoneNumber;
        String regex = "^\\+\\d{1,3}\\d{1,10}$";
        cellPhoneNumberValid = cellPhoneNumber != null && Pattern.matches(regex, cellPhoneNumber);
        return cellPhoneNumberValid;
    }

    public String returnUsernameStatus() {
        if (usernameValid) {
            return "Username successfully captured.";
        }
        return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
    }

    public String returnPasswordStatus() {
        if (passwordValid) {
            return "Password successfully captured.";
        }
        return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
    }

    public String returnCellPhoneNumberStatus() {
        if (cellPhoneNumberValid) {
            return "Cell phone number successfully added.";
        }
        return "Cell phone number incorrectly formatted or does not contain international code; please correct the number and try again.";
    }
    // Validates and stores a new user's details if all fields meet the required format
    public boolean registerUser(String firstName, String lastName, String username, String password, String cellPhoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;

        boolean usernameOk = checkUserName(username);
        boolean passwordOk = checkPasswordComplexity(password);
        boolean cellPhoneOk = checkCellPhoneNumber(cellPhoneNumber);

        if (usernameOk && passwordOk && cellPhoneOk) {
            registeredUser = this;
            return true;
        }
        return false;
    }

    public Boolean loginUser() {
        if (registeredUser == null || this.username == null || this.password == null) {
            return false;
        }
        return this.username.equals(registeredUser.username)
                && this.password.equals(registeredUser.password);
    }

    public String returnLoginStatus() {
        if (loginUser()) {
            return "Welcome " + registeredUser.firstName + ", " + registeredUser.lastName + " it is great to see you again.";
        }
        return "Username or password incorrect, please try again.";
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCellPhoneNumber() {
        return cellPhoneNumber;
    }

    static void resetRegisteredUser() {
        registeredUser = null;
    }
    
}


   
