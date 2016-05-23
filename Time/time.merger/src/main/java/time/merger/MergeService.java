package time.merger;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

public class MergeService {

    private static final Logger LOG = LogManager.getLogger(MergeService.class);
	   
	public void merge(final Merge merge) throws IOException{
		final File[] allIndexes = allIndexes(merge.getMergeableIndexesDir());
		final File destPath = new File(merge.getMergedIndexDir());
		final File biggerIndex = Arrays.stream(allIndexes).max((f1,f2) -> Long.compare(FileUtils.sizeOfDirectory(f1), FileUtils.sizeOfDirectory(f2))).get();
		final Directory destDirectory = directoryFromFile(destPath);
    	final File[] mergeableIndexesFile = Arrays.stream(allIndexes).filter(f -> f != biggerIndex).toArray(File[]::new);
    	final Directory[] mergeableIndexes = Arrays.stream(mergeableIndexesFile).map(f -> directoryFromFile(f)).toArray(Directory[]::new);
    	final IndexWriterConfig indexWriterConfig = indexWriterConfig();
    	
    	
        LOG.info("FileUtils.deleteDirectory(" + destPath + ") . . .");
        FileUtils.deleteDirectory(destPath);
		LOG.info("FileUtils.copyDirectory(" + biggerIndex + " -> " + destPath + ") . . .");
        FileUtils.copyDirectory(biggerIndex, destPath);

        if(mergeableIndexesFile.length > 0) {
            LOG.info("creation de indexWriter sur " + destPath + " . . .");
            final IndexWriter indexWriter = new IndexWriter(destDirectory, indexWriterConfig);
            LOG.info("indexWriter.addIndexes(" + Arrays.toString(mergeableIndexesFile) + ") . . .");
            indexWriter.addIndexes(mergeableIndexes);
            LOG.info("indexWriter.forceMerge(1) . . .");
            indexWriter.forceMerge(1);
            LOG.info("indexWriter.close() . . .");
            indexWriter.close();
        }else{
            LOG.info("pas de merge d'index à faire.");
        }
        LOG.info("Ok.");
	}
	
    protected File[] allIndexes(final String mergeIndexSrcDir){
        final File parent = new File(mergeIndexSrcDir);
        if(parent.isDirectory()){
            return Arrays.stream(parent.listFiles()).filter(f -> !f.getName().startsWith(".")).filter(f -> !"ignore".equals(f.getName())).toArray(File[]::new);
        }else{
            throw new RuntimeException("mergeIndexSrcDir must be a directory");
        }
    }
    
    protected Directory directoryFromFile(File f) {
        try {
            return FSDirectory.open(Paths.get(f.getPath()));
        } catch (IOException e) {
            LOG.error(e);
            return null;
        }
    }
    
    protected IndexWriterConfig indexWriterConfig(){
        final IndexWriterConfig indexWriterConfig = new IndexWriterConfig(new StandardAnalyzer());
        indexWriterConfig.setOpenMode(OpenMode.APPEND);
        indexWriterConfig.setRAMBufferSizeMB(256.0);
        return indexWriterConfig;
    }
	
}
