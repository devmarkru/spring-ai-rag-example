package ru.devmark.rag.service

import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor
import org.springframework.ai.chat.messages.SystemMessage
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.ai.openai.OpenAiChatOptions
import org.springframework.ai.openai.api.OpenAiApi
import org.springframework.ai.openai.api.ResponseFormat
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import ru.devmark.rag.reader.CustomDocumentReader
import java.time.YearMonth

@Service
class RagService(
    private val documentReader: CustomDocumentReader,
    private val vectorStore: VectorStore,
    private val chatClient: ChatClient,
) {
    fun saveDocumentsToVectorStore() {
        vectorStore.delete("year > 0")
        listOf(
            YearMonth.of(2024, 12),
            YearMonth.of(2025, 1),
            YearMonth.of(2025, 2),
            YearMonth.of(2025, 3),
        ).forEach { yearMonth ->
            val resource = ClassPathResource("${yearMonth.year}-${yearMonth.monthValue}.xlsx")
            val documents = documentReader.getDocuments(resource, yearMonth)
            vectorStore.add(documents)
        }
    }

    fun getAnswer(question: String): String {
        val responseFormat = ResponseFormat.builder()
            .type(ResponseFormat.Type.TEXT)
            .build()

        val chatOptions = OpenAiChatOptions.builder()
            .model(OpenAiApi.ChatModel.GPT_5_MINI)
            .temperature(1.0) // для GPT 5 температура всегда 1.0
            .responseFormat(responseFormat)
            .build()

        return chatClient.prompt(
            Prompt(
                SystemMessage(SYSTEM_PROMPT),
                chatOptions,
            )
        )
            .advisors { a -> a.param(QuestionAnswerAdvisor.FILTER_EXPRESSION, "year == 2025") }
            .user(question)
            .call()
            .content()
            ?: "Не удалось получить ответ"
    }

    private companion object {
        val SYSTEM_PROMPT = """
            Ты - умный помощник. Всегда отвечай на вопросы максимально коротко, без лишних слов и без ненужных деталей.
        """.trimIndent()
    }
}
