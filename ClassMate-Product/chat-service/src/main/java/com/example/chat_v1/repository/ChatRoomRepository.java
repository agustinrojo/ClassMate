package com.example.chat_v1.repository;

import com.example.chat_v1.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
//    @Query("SELECT c FROM ChatRoom c " +
//           "WHERE (c.userA = :userA AND c.userB = :userB) OR (c.userA = :userB AND c.userB = :userA)")
//    List<ChatRoom> findChatRoomBetween(User userA, User userB);

    Optional<ChatRoom> findBySenderIdAndReceiverId(Long senderId, Long recipientId);
}
