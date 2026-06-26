package com.alumni.portal.repository;

import com.alumni.portal.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findBySenderEmailAndReceiverEmailOrderBySentAtAsc(
        String senderEmail, String receiverEmail);

    List<Message> findByReceiverEmailAndIsReadFalse(String receiverEmail);

    List<Message> findBySenderEmailOrReceiverEmailOrderBySentAtDesc(
        String senderEmail, String receiverEmail);
}