package ru.devmark.rag.reader

import org.springframework.ai.document.Document
import org.springframework.ai.reader.tika.TikaDocumentReader
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component
import java.time.YearMonth

@Component
class CustomDocumentReader {
    fun getDocuments(resource: Resource, yearMonth: YearMonth): List<Document> {
        val tikaDocumentReader = TikaDocumentReader(resource)
        val documents = tikaDocumentReader.read()
        documents.forEach {
            it.metadata["year"] = yearMonth.year
            it.metadata["month"] = yearMonth.monthValue
        }
        return documents
    }
}
