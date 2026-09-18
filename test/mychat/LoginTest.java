/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package mychat;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;



/**
 *
 * @author Student
 */
public class LoginTest {
   
    private Login login;

    @Before
    public void setUp() {
        login = new Login();
        Login.resetRegisteredUser();
    }

    @Test
    public void testUsernameCorrectlyFormatted_returnsSuccessMessage() {
        login.checkUserName("kyl_1");
        assertEquals("Username successfully captured.", login.returnUsernameStatus());
    }

    @Test
    public void testUsernameIncorrectlyFormatted_returnsErrorMessage() {
        login.checkUserName("kyle!!!!!!");
        assertEquals(
            "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.",
            login.returnUsernameStatus()
        );
    }

    @Test
    public void testPasswordMeetsComplexity_returnsSuccessMessage() {
        login.checkPasswordComplexity("Ch&&sec@ke99!");
        assertEquals("Password successfully captured.", login.returnPasswordStatus());
    }

    @Test
    public void testPasswordDoesNotMeetComplexity_returnsErrorMessage() {
        login.checkPasswordComplexity("password");
        assertEquals(
            "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.",
            login.returnPasswordStatus()
        );
    }

    @Test
    public void testCellPhoneCorrectlyFormatted_returnsSuccessMessage() {
        login.checkCellPhoneNumber("+27838968976");
        assertEquals("Cell phone number successfully added.", login.returnCellPhoneNumberStatus());
    }

    @Test
    public void testCellPhoneIncorrectlyFormatted_returnsErrorMessage() {
        login.checkCellPhoneNumber("08966553");
        assertEquals(
            "Cell phone number incorrectly formatted or does not contain international code; please correct the number and try again.",
            login.returnCellPhoneNumberStatus()
        );
    }

    @Test
    public void testUsernameCorrectlyFormatted_returnsTrue() {
        assertTrue(login.checkUserName("kyl_1"));
    }

    @Test
    public void testUsernameIncorrectlyFormatted_returnsFalse() {
        assertFalse(login.checkUserName("kyle!!!!!!"));
    }

    @Test
    public void testPasswordMeetsComplexity_returnsTrue() {
        assertTrue(login.checkPasswordComplexity("Ch&&sec@ke99!"));
    }

    @Test
    public void testPasswordDoesNotMeetComplexity_returnsFalse() {
        assertFalse(login.checkPasswordComplexity("password"));
    }

    @Test
    public void testCellPhoneCorrectlyFormatted_returnsTrue() {
        assertTrue(login.checkCellPhoneNumber("+27838968976"));
    }

    @Test
    public void testCellPhoneIncorrectlyFormatted_returnsFalse() {
        assertFalse(login.checkCellPhoneNumber("08966553"));
    }

    @Test
    public void testLoginSuccessful_returnsTrue() {
        Login registration = new Login();
        registration.registerUser("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");

        Login attempt = new Login();
        attempt.setUsername("kyl_1");
        attempt.setPassword("Ch&&sec@ke99!");

        assertTrue(attempt.loginUser());
    }

    @Test
    public void testLoginFailed_returnsFalse() {
        Login registration = new Login();
        registration.registerUser("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");

        Login attempt = new Login();
        attempt.setUsername("kyl_1");
        attempt.setPassword("wrongPassword1!");

        assertFalse(attempt.loginUser());
    }

    @Test
    public void testLoginSuccessful_returnsWelcomeMessage() {
        Login registration = new Login();
        registration.registerUser("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");

        Login attempt = new Login();
        attempt.setUsername("kyl_1");
        attempt.setPassword("Ch&&sec@ke99!");

        assertEquals("Welcome Kyle, Smith it is great to see you again.", attempt.returnLoginStatus());
    }

    @Test
    public void testLoginFailed_returnsErrorMessage() {
        Login registration = new Login();
        registration.registerUser("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");

        Login attempt = new Login();
        attempt.setUsername("kyl_1");
        attempt.setPassword("wrongPassword1!");

        assertEquals("Username or password incorrect, please try again.", attempt.returnLoginStatus());
    }


}

