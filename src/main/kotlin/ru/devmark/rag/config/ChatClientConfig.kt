package ru.devmark.rag.config

import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ChatClientConfig {
    @Bean
    fun chatClient(
        builder: ChatClient.Builder,
        vectorStore: VectorStore,
    ): ChatClient =
        builder
            .defaultAdvisors(
                SimpleLoggerAdvisor(),
                QuestionAnswerAdvisor(vectorStore),
            )
            .build()
}
