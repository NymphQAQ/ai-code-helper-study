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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RAG（检索增强生成）配置类。
 * 负责文档的切分、向量化和内容检索器的初始化。
 * 支持通过配置文件灵活调整文档路径、切分参数和检索参数。
 */
@Configuration
public class RagConfig {

    // 注入大模型的EmbeddingModel
    @Resource
    private EmbeddingModel qwenEmbeddingModel;

    // 注入嵌入向量存储对象
    @Resource
    private EmbeddingStore<TextSegment> embeddingStore;

    // 文档路径、分割参数、检索参数通过配置文件注入
    @Value("${rag.docs-path:src/main/resources/docs}")
    private String docsPath;
    @Value("${rag.split-size:1000}")
    private int splitSize;
    @Value("${rag.split-overlap:200}")
    private int splitOverlap;
    @Value("${rag.max-results:5}")
    private int maxResults;
    @Value("${rag.min-score:0.75}")
    private double minScore;

    /**
     * 可选：项目启动时手动 ingest 文档（如需自动化，可放开 @Bean 注解）
     * 负责将本地文档切分、向量化并写入向量库。
     * 异常处理保证启动健壮性。
     */
    //@Bean
    public CommandLineRunner ingestDocuments() {
        return args -> {
            try {
                // 加载本地文档
                Document document = FileSystemDocumentLoader.loadDocument(docsPath);
                // 按段落切分文档
                DocumentByParagraphSplitter splitter = new DocumentByParagraphSplitter(splitSize, splitOverlap);
                // 构建嵌入存储摄取器
                EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                        .documentSplitter(splitter)
                        // 拼接文件名和正文，便于检索结果展示来源
                        .textSegmentTransformer(textSegment -> TextSegment.from(textSegment.metadata().getString("file_name") + "\n" + textSegment.text(), textSegment.metadata()))
                        .embeddingModel(qwenEmbeddingModel)
                        .embeddingStore(embeddingStore)
                        .build();
                // 执行摄取，将文档内容写入向量存储
                ingestor.ingest(document);
                System.out.println("[RAG] 文档已成功 ingest 到向量库");
            } catch (Exception e) {
                System.err.println("[RAG] 文档 ingest 失败: " + e.getMessage());
            }
        };
    }

    /**
     * 构建内容检索器 Bean，依赖已准备好的 embeddingStore。
     * 支持通过配置文件灵活调整检索参数。
     */
    @Bean
    public ContentRetriever contentRetriever() {
        return EmbeddingStoreContentRetriever.builder()
                .embeddingModel(qwenEmbeddingModel)
                .embeddingStore(embeddingStore)
                .maxResults(maxResults) // 最大返回结果数
                .minScore(minScore)     // 最小相似度分数
                .build();
    }
}