import java.time.LocalDateTime;
import java.util.List;

public class Message {
    private final User sender;
    private final List<User> recipients;
    private final String content;
    private final LocalDateTime timestamp;

    public Message(User sender, List<User> recipients, String content, LocalDateTime timestamp) {
        this.sender = sender;
        this.recipients = recipients;
        this.content = content;
        this.timestamp = timestamp;
    }

    public User getSender() {
        return sender;
    }

    public List<User> getRecipients() {
        return recipients;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}