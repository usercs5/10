import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

public class ChatAppTest {
    private ChatServer server;
    private User alice;
    private User bob;
    private User charlie;

    @BeforeEach
    public void setUp() {
        server = new ChatServer();
        alice = new User("Alice", server);
        bob = new User("Bob", server);
        charlie = new User("Charlie", server);
    }

    @Test
    public void testMessagingAndMediator() {
        alice.sendMessage("Hello Bob", Arrays.asList(bob));
        
        assertEquals(1, alice.getHistory().getMessages().size(), "Alice should have 1 sent message in history.");
        assertEquals(1, bob.getHistory().getMessages().size(), "Bob should have 1 received message in history.");
        assertEquals("Hello Bob", bob.getHistory().getLastMessage().getContent(), "Last message content should match.");
    }

    @Test
    public void testMementoUndo() {
        alice.sendMessage("First message", Arrays.asList(bob));
        alice.sendMessage("Second message to undo", Arrays.asList(bob));
        
        assertEquals(2, alice.getHistory().getMessages().size());
        assertEquals("Second message to undo", alice.getHistory().getLastMessage().getContent());
        
        alice.undoLastMessage();
        
        assertEquals(1, alice.getHistory().getMessages().size(), "History size should decrease after undo.");
        assertEquals("First message", alice.getHistory().getLastMessage().getContent(), "Last message should revert to the previous one.");
    }

    @Test
    public void testBlockAndUnblockUser() {
        bob.blockUser(charlie);
        assertTrue(server.isBlocked(bob, charlie), "Charlie should be blocked by Bob.");

        int initialBobMessages = bob.getHistory().getMessages().size();
        charlie.sendMessage("Blocked message", Arrays.asList(bob));
        
        // Bob should not receive the message
        assertEquals(initialBobMessages, bob.getHistory().getMessages().size(), "Blocked messages should not reach the recipient.");

        bob.unblockUser(charlie);
        assertFalse(server.isBlocked(bob, charlie), "Charlie should now be unblocked.");

        charlie.sendMessage("Unblocked message", Arrays.asList(bob));
        assertEquals(initialBobMessages + 1, bob.getHistory().getMessages().size(), "Unblocked messages should successfully reach recipient.");
        assertEquals("Unblocked message", bob.getHistory().getLastMessage().getContent());
    }

    @Test
    public void testIteratorByUser() {
        alice.sendMessage("Msg 1 to Bob", Arrays.asList(bob));
        bob.sendMessage("Reply 1 from Bob", Arrays.asList(alice));
        alice.sendMessage("Msg to Charlie", Arrays.asList(charlie));

        // Iterate over Alice's history filtered by interactions with Bob
        Iterator<Message> iterator = alice.iterator(bob);
        int count = 0;
        while (iterator.hasNext()) {
            Message msg = iterator.next();
            assertTrue(msg.getSender().equals(alice) || msg.getRecipients().contains(bob));
            count++;
        }
        assertEquals(2, count, "Iterator should return exactly 2 messages involving both Alice and Bob.");
    }
}