import java.util.Arrays;
import java.util.Iterator;

public class ChatDriver {
    public static void main(String[] args) {
        System.out.println("=== Starting Chat Application Driver ===");

        ChatServer server = new ChatServer();

        User alice = new User("Alice", server);
        User bob = new User("Bob", server);
        User charlie = new User("Charlie", server);

        System.out.println("\n--- 1. Sending Messages ---");
        alice.sendMessage("Hello Bob and Charlie!", Arrays.asList(bob, charlie));
        bob.sendMessage("Hi Alice, got your message.", Arrays.asList(alice));

        System.out.println("\n--- 2. Demonstrating Memento (Undo Last Message) ---");
        charlie.sendMessage("This is a secret message I want to take back.", Arrays.asList(alice));
        System.out.println("Charlie's last message before undo: " + 
            (charlie.getHistory().getLastMessage() != null ? charlie.getHistory().getLastMessage().getContent() : "None"));
        charlie.undoLastMessage();
        System.out.println("Charlie's last message after undo: " + 
            (charlie.getHistory().getLastMessage() != null ? charlie.getHistory().getLastMessage().getContent() : "None"));

        System.out.println("\n--- 3. Demonstrating Mediator (Blocking Users) ---");
        bob.blockUser(charlie);
        charlie.sendMessage("Hey Bob, can you read this?", Arrays.asList(bob));

        bob.unblockUser(charlie);
        charlie.sendMessage("Hey Bob, can you read this now?", Arrays.asList(bob));

        System.out.println("\n--- 4. Demonstrating Iterator by User ---");
        alice.sendMessage("How are you doing Bob?", Arrays.asList(bob));

        System.out.println("Iterating over Alice's chat history with Bob:");
        Iterator<Message> aliceBobIterator = alice.iterator(bob);
        while (aliceBobIterator.hasNext()) {
            Message msg = aliceBobIterator.next();
            System.out.println(" - [From: " + msg.getSender().getName() + "]: " + msg.getContent());
        }

        System.out.println("\n=== Chat Application Driver Finished ===");
    }
}