import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class User implements IterableByUser {
    private final String name;
    private final ChatServer mediator;
    private final ChatHistory history;
    private final Stack<MessageMemento> mementoStack;

    public User(String name, ChatServer mediator) {
        this.name = name;
        this.mediator = mediator;
        this.history = new ChatHistory();
        this.mementoStack = new Stack<>();
        mediator.registerUser(this);
    }

    public String getName() {
        return name;
    }

    public ChatHistory getHistory() {
        return history;
    }

    public void sendMessage(String content, List<User> recipients) {
        mediator.sendMessage(this, recipients, content);
    }

    public void receiveMessage(Message message) {
        history.addMessage(message);
        System.out.println("[" + name + " received message]: \"" + message.getContent() + "\" from " + message.getSender().getName());
    }

    public void addMemento(MessageMemento memento) {
        mementoStack.push(memento);
    }

    public void undoLastMessage() {
        if (mementoStack.isEmpty()) {
            System.out.println("[" + name + "] No message to undo.");
            return;
        }

        MessageMemento lastMemento = mementoStack.pop();
        Message lastMsg = history.getLastMessage();
        if (lastMsg != null && lastMsg.getSender().equals(this) && lastMsg.getContent().equals(lastMemento.getContent())) {
            history.removeLastMessage();
            System.out.println("[" + name + "] Undid last sent message: \"" + lastMemento.getContent() + "\"");
        } else {
            System.out.println("[" + name + "] Could not find matching message in history to undo.");
        }
    }

    public void blockUser(User userToBlock) {
        mediator.blockUser(this, userToBlock);
    }

    public void unblockUser(User userToUnblock) {
        mediator.unblockUser(this, userToUnblock);
    }

    @Override
    public Iterator<Message> iterator(User userToSearchWith) {
        return history.iterator(userToSearchWith);
    }
}