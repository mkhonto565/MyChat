/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mychat;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Student
 */
public class Messages {
     private String messageID;
    private String recipient;
    private String messageText;
    private String messageHash;
    private String status;

    private static int totalMessages = 0;
    private static final List<Messages> sentMessages = new ArrayList<>();
    private static final List<Messages> storedMessages = new ArrayList<>();
    private static final List<Messages> disregardedMessages = new ArrayList<>();
    private static final Random random = new Random();

    public Messages() {
    }

    public Messages(String recipient, String messageText) {
        this.messageID = generateMessageID();
        this.recipient = recipient;
        this.messageText = messageText;
        this.status = "Pending";
    }

    private String generateMessageID() {
        return String.format("%010d", Math.abs(random.nextLong()) % 10_000_000_000L);
    }

    public boolean checkMessageID() {
        return messageID != null
                && messageID.matches("\\d{1,10}");
    }

    public boolean checkRecipientCell() {
        return recipient != null
                && recipient.matches("\\+27\\d{9}");
    }

    public boolean checkMessageLength() {
        return messageText != null
                && messageText.length() > 0
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

        String firstTwoDigits = messageID.substring(0, 2);

        return (firstTwoDigits + ":" + totalMessages + ":"
                + firstWord + lastWord).toUpperCase();
    }

    public String sentMessage(int option) {
        if (option == 1) {
            if (!checkMessageID()) {
                return "Invalid message ID.";
            }

            if (!checkRecipientCell()) {
                return "Invalid recipient cellphone number.";
            }

            if (!checkMessageLength()) {
                return "Message must contain between 1 and 250 characters.";
            }

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
            status = "Stored";
            storedMessages.add(this);

            try {
                storeMessage();
                return "Message successfully stored.";
            } catch (IOException e) {
                return "Message could not be stored: " + e.getMessage();
            }
        }

        return "Invalid option.";
    }

    public void storeMessage() throws IOException {
        try (FileWriter writer = new FileWriter("messages.json", true)) {
            String json = String.format(
                    "{\"messageID\":\"%s\",\"recipient\":\"%s\","
                    + "\"messageText\":\"%s\",\"messageHash\":\"%s\","
                    + "\"status\":\"%s\"}%n",
                    escapeJson(messageID),
                    escapeJson(recipient),
                    escapeJson(messageText),
                    escapeJson(messageHash == null ? "" : messageHash),
                    escapeJson(status));

            writer.write(json);
        }
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

    public static String printMessages() {
        StringBuilder output = new StringBuilder();

        for (Messages message : sentMessages) {
            output.append("Message ID: ").append(message.messageID).append("\n");
            output.append("Recipient: ").append(message.recipient).append("\n");
            output.append("Message: ").append(message.messageText).append("\n");
            output.append("Hash: ").append(message.messageHash).append("\n");
            output.append("-----------------------------\n");
        }

        if (output.length() == 0) {
            return "No sent messages available.";
        }

        return output.toString();
    }

    public static int returnTotalMessages() {
        return totalMessages;
    }

    public String getMessageID() {
        return messageID;
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
        return new ArrayList<>(storedMessages);
    }

    public static List<Messages> getDisregardedMessages() {
        return new ArrayList<>(disregardedMessages);
    }   

    String getMessagesID() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
