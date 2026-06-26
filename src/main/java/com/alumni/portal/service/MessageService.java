package com.alumni.portal.service;

import com.alumni.portal.model.Message;
import com.alumni.portal.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    PointsService pointsService;

    public void sendMessage(String senderEmail, String receiverEmail, String content) {
        Message message = new Message();
        message.setSenderEmail(senderEmail);
        message.setReceiverEmail(receiverEmail);
        message.setContent(content);
        message.setSentAt(LocalDateTime.now());
        message.setRead(false);
        messageRepository.save(message);
        pointsService.addPoints(senderEmail, 5);
    }

    public List<Message> getConversation(String user1, String user2) {
        List<Message> sent = messageRepository
            .findBySenderEmailAndReceiverEmailOrderBySentAtAsc(user1, user2);
        List<Message> received = messageRepository
            .findBySenderEmailAndReceiverEmailOrderBySentAtAsc(user2, user1);
        sent.addAll(received);
        sent.sort((a, b) -> a.getSentAt().compareTo(b.getSentAt()));
        return sent;
    }

    public long getUnreadCount(String email) {
        return messageRepository.findByReceiverEmailAndIsReadFalse(email).size();
    }

    public void markAsRead(String senderEmail, String receiverEmail) {
        List<Message> unread = messageRepository
            .findByReceiverEmailAndIsReadFalse(receiverEmail);
        unread.stream()
            .filter(m -> m.getSenderEmail().equals(senderEmail))
            .forEach(m -> {
                m.setRead(true);
                messageRepository.save(m);
            });
    }
}