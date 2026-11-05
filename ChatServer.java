import java.time.LocalDateTime;
import java.util.*;

public class ChatServer {
    private final Map<String, User> registeredUsers = new HashMap<>();
    private final Map<User, Set<User>> blockedUsersMap = new HashMap<>();

    public void registerUser(User user) {
        registeredUsers.put(user.getName(), user);
        blockedUsersMap.putIfAbsent(user, new HashSet<>());
    }

    public void unregisterUser(User user) {
        registeredUsers.remove(user.getName());
        blockedUsersMap.remove(user);
    }

    public void sendMessage(User sender, List<User> recipients, String content) {
        if (!registeredUsers.containsKey(sender.getName())) {
            System.out.println("Sender " + sender.getName() + " is not registered.");
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        Message message = new Message(sender, recipients, content, now);

        // Record message in sender's history and save memento snapshot
        sender.getHistory().addMessage(message);
        sender.addMemento(new MessageMemento(content, now));

        // Forward message to recipients through the mediator
        for (User recipient : recipients) {
            if (registeredUsers.containsKey(recipient.getName())) {
                Set<User> recipientBlockedList = blockedUsersMap.get(recipient);
                if (recipientBlockedList != null && recipientBlockedList.contains(sender)) {
                    System.out.println("[Server] Message from " + sender.getName() + " to " + recipient.getName() + " was blocked.");
                    continue;
                }
                recipient.receiveMessage(message);
            } else {
                System.out.println("[Server] Recipient " + recipient.getName() + " is not registered.");
            }
        }
    }

    public void blockUser(User owner, User userToBlock) {
        blockedUsersMap.computeIfAbsent(owner, k -> new HashSet<>()).add(userToBlock);
        System.out.println("[Server] " + owner.getName() + " has blocked " + userToBlock.getName());
    }

    public void unblockUser(User owner, User userToUnblock) {
        if (blockedUsersMap.containsKey(owner)) {
            blockedUsersMap.get(owner).remove(userToUnblock);
            System.out.println("[Server] " + owner.getName() + " has unblocked " + userToUnblock.getName());
        }
    }

    public boolean isBlocked(User owner, User user) {
        Set<User> blocked = blockedUsersMap.get(owner);
        return blocked != null && blocked.contains(user);
    }
}