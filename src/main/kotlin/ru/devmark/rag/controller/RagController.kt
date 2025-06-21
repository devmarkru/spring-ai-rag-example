package ru.devmark.rag.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.devmark.rag.dto.MessageDto
import ru.devmark.rag.service.RagService

@RestController
@RequestMapping("/rag")
class RagController(
    private val ragService: RagService,
) {
    @PutMapping
    fun saveDocumentsToVectorStore(): MessageDto {
        ragService.saveDocumentsToVectorStore()
        return MessageDto(text = "Documents saved to vector store")
    }

    @PostMapping
    fun getAnswer(@RequestBody request: MessageDto): MessageDto =
        MessageDto(
            text = ragService.getAnswer(request.text),
        )
}