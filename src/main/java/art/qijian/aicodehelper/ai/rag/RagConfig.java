// RagConfig.java
package art.qijian.aicodehelper.ai.rag;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.splitter.DocumentByParagraphSplitter;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RAG（检索增强生成）配置类
 * 负责初始化内容检索器，将文档切分、嵌入并存储到向量库
 */
@Configuration
public class RagConfig {

    // 注入 embedding 模型（如 Qwen Embedding）
    @Resource
    private EmbeddingModel qwenEmbeddingModel;

    // 注入向量存储（EmbeddingStore），用于存储文本片段的向量
    @Resource
    private EmbeddingStore<TextSegment> embeddingStore;

    /**
     * 构建内容检索器 ContentRetriever 的 Bean
     * 启动时会加载文档、切分、生成向量并存入向量库
     */
    @Bean
    public ContentRetriever contentRetriever(){
        // 1. 加载本地文档（此处路径可通过配置文件注入）
        Document document = FileSystemDocumentLoader.loadDocument("src/main/resources/docs");

        // 2. 按段落切分文档，参数：每段最大1000字符，重叠200字符
        DocumentByParagraphSplitter documentByParagraphSplitter =
                new DocumentByParagraphSplitter(1000, 200);

        // 3. 构建文档入库工具，设置切分器、文本转换、embedding模型和向量存储
        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .documentSplitter(documentByParagraphSplitter)
                // 文本片段转换：将文件名和正文拼接，便于后续检索时显示来源
                .textSegmentTransformer(textSegment ->
                        TextSegment.from(
                                textSegment.metadata().getString("file_name") + "\n" + textSegment.text(),
                                textSegment.metadata()))
                .embeddingModel(qwenEmbeddingModel)
                .embeddingStore(embeddingStore)
                .build();

        // 4. 执行文档入库（ingest），将切分后的片段生成向量并存入向量库
        ingestor.ingest(document);

        // 5. 构建内容检索器，设置最大返回结果数和最小相似度分数
        return EmbeddingStoreContentRetriever.builder()
                .embeddingModel(qwenEmbeddingModel)
                .embeddingStore(embeddingStore)
                .maxResults(5)      // 最多返回5条结果
                .minScore(0.75)     // 最小相似度分数为0.75
                .build();
    }
}