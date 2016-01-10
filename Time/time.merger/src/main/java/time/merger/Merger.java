package time.merger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Merger {
    private static final Log LOG = LogFactory.getLog(Merger.class);

    @Autowired
    private ApplicationArguments argz;
    
    @Bean
    protected File[] allIndexes(){
        final File parent = new File(argz.getOptionValues("src").get(0));
        if(parent.isDirectory()){
            return Arrays.stream(parent.listFiles()).filter(f -> !"histoires".equals(f.getName())).filter(f -> !"ignore".equals(f.getName())).toArray(File[]::new);
        }else{
            return null;
        }
    }
    
    @Bean
    protected File biggerIndex(){
        return Arrays.stream(allIndexes()).max((f1,f2) -> Long.compare(FileUtils.sizeOfDirectory(f1), FileUtils.sizeOfDirectory(f2))).get();
    }
    
    @Bean
    protected Directory[] mergeableIndexes() throws IOException{
        return Arrays.stream(mergeableIndexesFile()).map(f -> directoryFromFile(f)).toArray(Directory[]::new);
    }
    
    @Bean
    protected File[] mergeableIndexesFile(){
       return Arrays.stream(allIndexes()).filter(f -> f != biggerIndex()).toArray(File[]::new);
    }
    
    protected Directory directoryFromFile(File f) {
        try {
            return FSDirectory.open(Paths.get(f.getPath()));
        } catch (IOException e) {
            LOG.error(e);
            return null;
        }
    }

    @Bean
    protected File destPath() throws IOException{
        return new File(argz.getOptionValues("dest").get(0));
    }
    
    @Bean
    protected IndexWriterConfig indexWriterConfig(){
        final IndexWriterConfig indexWriterConfig = new IndexWriterConfig(new StandardAnalyzer());
        indexWriterConfig.setOpenMode(OpenMode.APPEND);
        indexWriterConfig.setRAMBufferSizeMB(256.0);
        return indexWriterConfig;
    }
    
    public static void main(String... args) throws Exception {
        final ApplicationContext ctx = SpringApplication.run(Merger.class, args);
        ctx.getBean(Merger.class).go();
    }
    
    private void go() throws IOException{
        LOG.info("FileUtils.deleteDirectory(" + destPath() + ") . . .");
        FileUtils.deleteDirectory(destPath());
        LOG.info("FileUtils.copyDirectory(" + biggerIndex() + " -> " + destPath() + ") . . .");
        FileUtils.copyDirectory(biggerIndex(), destPath());
        LOG.info("creation de indexWriter sur " + destPath() + " . . .");
        final IndexWriter indexWriter = new IndexWriter(directoryFromFile(destPath()), indexWriterConfig());
        LOG.info("indexWriter.addIndexes("+Arrays.toString(mergeableIndexesFile())+") . . .");
        indexWriter.addIndexes(mergeableIndexes());
        LOG.info("indexWriter.forceMerge(1) . . .");
        indexWriter.forceMerge(1);
        LOG.info("indexWriter.close() . . .");
        indexWriter.close();
        LOG.info("Ok.");
    }

}
