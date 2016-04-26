package time.merger.service;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

public class MergeIndexService {
	
	private static final Log LOG = LogFactory.getLog(MergeIndexService.class);
	   
	public void merge(final String src, final String dest) throws IOException{
		final File destPath = new File(dest);
		final File[] allIndexes = allIndexes(src);
		final File biggerIndex = Arrays.stream(allIndexes).max((f1,f2) -> Long.compare(FileUtils.sizeOfDirectory(f1), FileUtils.sizeOfDirectory(f2))).get();
		final Directory destDirectory = directoryFromFile(destPath);
    	final File[] mergeableIndexesFile = Arrays.stream(allIndexes).filter(f -> f != biggerIndex).toArray(File[]::new);
    	final Directory[] mergeableIndexes = Arrays.stream(mergeableIndexesFile).map(f -> directoryFromFile(f)).toArray(Directory[]::new);
    	final IndexWriterConfig indexWriterConfig = indexWriterConfig();
    	
    	
        LOG.info("FileUtils.deleteDirectory(" + destPath + ") . . .");
        FileUtils.deleteDirectory(destPath);
		LOG.info("FileUtils.copyDirectory(" + biggerIndex + " -> " + destPath + ") . . .");
        FileUtils.copyDirectory(biggerIndex, destPath);
        LOG.info("creation de indexWriter sur " + destPath + " . . .");
		final IndexWriter indexWriter = new IndexWriter(destDirectory, indexWriterConfig);
		LOG.info("indexWriter.addIndexes("+Arrays.toString(mergeableIndexesFile)+") . . .");
		indexWriter.addIndexes(mergeableIndexes);
        LOG.info("indexWriter.forceMerge(1) . . .");
        indexWriter.forceMerge(1);
        LOG.info("indexWriter.close() . . .");
        indexWriter.close();
        LOG.info("Ok.");
	}
	
    protected File[] allIndexes(final String src){
        final File parent = new File(src);
        if(parent.isDirectory()){
            return Arrays.stream(parent.listFiles()).filter(f -> !f.getName().startsWith(".")).filter(f -> !"ignore".equals(f.getName())).toArray(File[]::new);
        }else{
            return null;
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
