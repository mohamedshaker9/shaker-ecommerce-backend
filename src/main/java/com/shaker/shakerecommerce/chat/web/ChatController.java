package com.shaker.shakerecommerce.chat.web;


import com.shaker.shakerecommerce.chat.ChatAttributes;
import com.shaker.shakerecommerce.chat.ChatContext;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;
import static org.springframework.http.HttpHeaders.SET_COOKIE;

@RestController
@RequestMapping("/api/v1")
public class ChatController {

    private final ChatClient chatClient;
    private final ChatContext chatContext;
    private final VectorStore vectorStore;

    @Value("classpath:./promptTemplates/systemPromptTemplate.st")
    Resource systemPromptTemplate;


    public ChatController(ChatClient chatClient, ChatContext chatContext, VectorStore vectorStore) {
        this.chatClient = chatClient;
        this.chatContext = chatContext;
        this.vectorStore = vectorStore;
    }

    @GetMapping("/chat")
    public ResponseEntity<String> chat(@RequestParam String message) {
        // we used retrieval augmentation advisor to retrieve relevant documents
//        SearchRequest searchRequest = SearchRequest.builder()
//                .topK(3)
//                .similarityThreshold(0.7)
//                .query(message)
//                .build();
//        List<Document> documents = vectorStore.similaritySearch(searchRequest);
//
//
//        String similarity = documents.stream()
//                .map(Document::getText)
//                .collect(Collectors.joining("\n"));

        ChatContext chatContext = this.chatContext;

        return ResponseEntity.ok()
                .body(chatClient.prompt()
                .advisors(advisorSpec -> advisorSpec.param(CONVERSATION_ID, chatContext.getConversationId()))
//                        .system(
//                                promptSystemSpec -> promptSystemSpec.text(systemPromptTemplate)
//                                        .param("documents", similarity)
//                        )
                        .user(message)
                .call()
                .content());

    }

}
