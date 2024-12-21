package com.kylemall.shop.controller;

import com.kylemall.shop.domain.ChatMessage;
import com.kylemall.shop.mapper.ChatMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import lombok.extern.slf4j.Slf4j;
import java.sql.Timestamp;
import java.util.List;
import jakarta.servlet.http.HttpSession;

@Controller
@Slf4j
public class ChatController {

    @Autowired
    private ChatMapper chatMapper;

    @GetMapping("/chat")
    public String chatPage(Model model, HttpSession session) {
        // 로그인 체크
        if(session.getAttribute("member") == null) {
            return "redirect:/loginForm";
        }

        try {
            // 최근 메시지 50개 불러오기
            List<ChatMessage> recentMessages = chatMapper.getRecentMessages("public", 50);
            model.addAttribute("recentMessages", recentMessages);
        } catch (Exception e) {
            log.error("Error loading chat messages", e);
        }
        return "views/chat";
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        try {
            log.info("Received message: {}", chatMessage);
            chatMessage.setType("CHAT");
            chatMessage.setSentAt(new Timestamp(System.currentTimeMillis()));
            chatMapper.insertMessage(chatMessage);
        } catch (Exception e) {
            log.error("Error saving chat message", e);
        }
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, 
                             SimpMessageHeaderAccessor headerAccessor) {
        try {
            log.info("User joined: {}", chatMessage.getSender());
            headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
            chatMessage.setType("JOIN");
            chatMessage.setSentAt(new Timestamp(System.currentTimeMillis()));
            chatMapper.insertMessage(chatMessage);
        } catch (Exception e) {
            log.error("Error saving join message", e);
        }
        return chatMessage;
    }
} 