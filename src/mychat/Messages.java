/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mychat;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

/**
 *
 * @author Student
 */
public class Messages {
     private String messageID;
    private String sender;
    private String recipient;
    private String messageText;
    private String messageHash;
    private String status;

    private static int totalMessages = 0;

    private static final ArrayList<Messages> sentMessages = new ArrayList<>();
    private static final ArrayList<Messages> storedMessages = new ArrayList<>();
    private static final ArrayList<Messages> disregardedMessages = new ArrayList<>();

    private static final String FILE_NAME = "messages.json";

    public Messages() {
    }

    public Messages(String recipient, String messageText) {
        this("Developer", recipient, messageText);
    }

    public Messages(String sender, String recipient, String messageText) {
        this.messageID = generateMessageID();
        this.sender = sender;
        this.recipient = recipient;
        this.messageText = messageText;
        this.status = "Pending";
    }

    private String generateMessageID() {
        return String.format("%010d",
                new Random().nextInt(1_000_000_000));
    }

    public boolean checkMessageID() {
        return messageID != null && messageID.matches("\\d{1,10}");
    }

    public boolean checkRecipientCell() {
        return recipient != null && recipient.matches("\\+27\\d{9}");
    }

    public boolean checkMessageLength() {
        return messageText != null
                && !messageText.trim().isEmpty()
                && messageText.length() <= 250;
    }

    public String createMessageHash() {
        if (messageID == null || messageText == null
                || messageText.trim().isEmpty()) {
            return "";
        }

        String[] words = messageText.trim().split("\\s+");
        String firstWord = words[0];
        String lastWord = words[words.length - 1];

        return (messageID.substring(0, 2) + ":"
                + (totalMessages + 1) + ":"
                + firstWord + lastWord).toUpperCase();
    }

    public String sentMessage(int option) {

        if (!checkMessageID()) {
            return "Invalid message ID.";
        }

        if (!checkRecipientCell()) {
            return "Invalid recipient cellphone number.";
        }

        if (!checkMessageLength()) {
            return "Message must contain between 1 and 250 characters.";
        }

        if (option == 1) {
            messageHash = createMessageHash();
            status = "Sent";
            sentMessages.add(this);
            totalMessages++;
            return "Message successfully sent.";
        }

        if (option == 2) {
            status = "Disregarded";
            disregardedMessages.add(this);
            return "Message disregarded.";
        }

        if (option == 3) {
            messageHash = createMessageHash();
            status = "Stored";

            if (!storedMessages.contains(this)) {
                storedMessages.add(this);
            }

            try {
                storeMessage();
                return "Message successfully stored.";
            } catch (IOException e) {
                storedMessages.remove(this);
                return "Error storing message: " + e.getMessage();
            }
        }

        return "Invalid option.";
    }

    // Save a stored message as a JSON object, one object per line.
    public void storeMessage() throws IOException {

        String json = "{"
                + "\"messageID\":\"" + escapeJson(messageID) + "\","
                + "\"sender\":\"" + escapeJson(sender) + "\","
                + "\"recipient\":\"" + escapeJson(recipient) + "\","
                + "\"messageText\":\"" + escapeJson(messageText) + "\","
                + "\"messageHash\":\"" + escapeJson(messageHash) + "\","
                + "\"status\":\"" + escapeJson(status) + "\""
                + "}";

        Files.write(Paths.get(FILE_NAME),
                (json + System.lineSeparator()).getBytes("UTF-8"),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND);
    }

    private String escapeJson(String value) {
        if (value == null) {
            return "";
        }

        return value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private static String unescapeJson(String value) {
        return value.replace("\\\"", "\"")
                .replace("\\n", "\n")
                .replace("\\r", "\r")
                .replace("\\t", "\t")
                .replace("\\\\", "\\");
    }

    private static String readJsonField(String json, String field) {
        Pattern pattern = Pattern.compile(
                "\"" + field + "\"\\s*:\\s*\"((?:\\\\.|[^\"\\\\])*)\"");
        Matcher matcher = pattern.matcher(json);

        if (matcher.find()) {
            return unescapeJson(matcher.group(1));
        }

        return "";
    }

    // Load stored messages from the JSON file.
    public static void loadStoredMessages() {

        storedMessages.clear();

        Path path = Paths.get(FILE_NAME);

        if (!Files.exists(path)) {
            return;
        }

        try {
            List<String> lines = Files.readAllLines(path);

            for (String line : lines) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                Messages message = new Messages();

                message.messageID = readJsonField(line, "messageID");
                message.sender = readJsonField(line, "sender");
                message.recipient = readJsonField(line, "recipient");
                message.messageText = readJsonField(line, "messageText");
                message.messageHash = readJsonField(line, "messageHash");
                message.status = readJsonField(line, "status");

                storedMessages.add(message);
            }

        } catch (IOException e) {
            System.out.println("Could not load stored messages: "
                    + e.getMessage());
        }
    }

    // Display all sent messages.
    public static String printMessages() {
        StringBuilder output = new StringBuilder();

        for (Messages message : sentMessages) {
            output.append("Message ID: ").append(message.messageID).append("\n");
            output.append("Recipient: ").append(message.recipient).append("\n");
            output.append("Message: ").append(message.messageText).append("\n");
            output.append("Hash: ").append(message.messageHash).append("\n");
            output.append("-----------------------------\n");
        }

        return output.length() == 0
                ? "No sent messages available."
                : output.toString();
    }

    // Display sender and recipient of stored messages.
    public static void displayStoredSendersAndRecipients() {
        loadStoredMessages();

        if (storedMessages.isEmpty()) {
            System.out.println("No stored messages available.");
            return;
        }

        for (Messages message : storedMessages) {
            System.out.println("Sender: " + message.sender);
            System.out.println("Recipient: " + message.recipient);
            System.out.println("-----------------------------");
        }
    }

    // Find the longest stored message.
    public static void displayLongestStoredMessage() {
        loadStoredMessages();

        if (storedMessages.isEmpty()) {
            System.out.println("No stored messages available.");
            return;
        }

        Messages longest = storedMessages.get(0);

        for (Messages message : storedMessages) {
            if (message.messageText.length()
                    > longest.messageText.length()) {
                longest = message;
            }
        }

        System.out.println("Longest stored message:");
        System.out.println(longest.messageText);
        System.out.println("Length: " + longest.messageText.length());
    }

    // Search for a message using its ID.
    public static void searchMessageByID(String id) {
        loadStoredMessages();

        for (Messages message : storedMessages) {
            if (message.messageID.equals(id)) {
                System.out.println("Message ID: " + message.messageID);
                System.out.println("Recipient: " + message.recipient);
                System.out.println("Message: " + message.messageText);
                return;
            }
        }

        System.out.println("Message ID not found.");
    }

    // Search for all messages sent to a particular recipient.
    public static void searchMessagesByRecipient(String recipient) {
        loadStoredMessages();

        boolean found = false;

        for (Messages message : storedMessages) {
            if (message.recipient.equals(recipient)) {
                System.out.println("Message: " + message.messageText);
                System.out.println("Message ID: " + message.messageID);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No messages found for this recipient.");
        }
    }

    // Delete a stored message using its message hash.
    public static void deleteMessageByHash(String hash) {
        loadStoredMessages();

        boolean removed = storedMessages.removeIf(
                message -> message.messageHash.equals(hash));

        if (removed) {
            saveAllStoredMessages();
            System.out.println("Message successfully deleted.");
        } else {
            System.out.println("Message hash not found.");
        }
    }

    // Rewrite the JSON file after a deletion.
    private static void saveAllStoredMessages() {

        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(FILE_NAME, false))) {

            for (Messages message : storedMessages) {

                String json = "{"
                        + "\"messageID\":\"" + message.escapeJson(message.messageID) + "\","
                        + "\"sender\":\"" + message.escapeJson(message.sender) + "\","
                        + "\"recipient\":\"" + message.escapeJson(message.recipient) + "\","
                        + "\"messageText\":\"" + message.escapeJson(message.messageText) + "\","
                        + "\"messageHash\":\"" + message.escapeJson(message.messageHash) + "\","
                        + "\"status\":\"" + message.escapeJson(message.status) + "\""
                        + "}";

                writer.write(json);
                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println("Could not update JSON file: " + e.getMessage());
        }
    }

    // Display a full report of stored messages.
    public static void displayStoredMessageReport() {
        loadStoredMessages();

        if (storedMessages.isEmpty()) {
            System.out.println("No stored messages available.");
            return;
        }

        System.out.println("========== STORED MESSAGE REPORT ==========");

        for (Messages message : storedMessages) {
            System.out.println("Message ID: " + message.messageID);
            System.out.println("Sender: " + message.sender);
            System.out.println("Recipient: " + message.recipient);
            System.out.println("Message: " + message.messageText);
            System.out.println("Message Hash: " + message.messageHash);
            System.out.println("Status: " + message.status);
            System.out.println("-------------------------------------------");
        }
    }

    public static int returnTotalMessages() {
        return totalMessages;
    }

    public String getMessageID() {
        return messageID;
    }

    // Keep this method because your existing MyChat.java calls it.
    public String getMessagesID() {
        return messageID;
    }

    public String getSender() {
        return sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getMessageHash() {
        return messageHash;
    }

    public String getStatus() {
        return status;
    }

    public static List<Messages> getSentMessages() {
        return new ArrayList<>(sentMessages);
    }

    public static List<Messages> getStoredMessages() {
        loadStoredMessages();
        return new ArrayList<>(storedMessages);
    }

    public static List<Messages> getDisregardedMessages() {
        return new ArrayList<>(disregardedMessages);
    }   

}
 
