import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class searchMessagesByUser implements Iterator<Message> {
    private final List<Message> filteredMessages;
    private int currentIndex = 0;

    public searchMessagesByUser(List<Message> allMessages, User userToSearchWith) {
        // Filter messages sent by or received by the specified user
        this.filteredMessages = allMessages.stream()
            .filter(msg -> (msg.getSender() != null && msg.getSender().equals(userToSearchWith)) ||
                           (msg.getRecipients() != null && msg.getRecipients().contains(userToSearchWith)))
            .collect(Collectors.toList());
    }

    @Override
    public boolean hasNext() {
        return currentIndex < filteredMessages.size();
    }

    @Override
    public Message next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more messages found for this user.");
        }
        return filteredMessages.get(currentIndex++);
    }
}