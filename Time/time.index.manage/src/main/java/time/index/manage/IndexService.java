package time.index.manage;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import time.conf.ConfEnum;
import time.conf.ConfManager;
import time.conf.Resolver;
import time.domain.IndexCreation;
import time.domain.IndexRebuild;
import time.domain.Merge;
import time.domain.TimeWebConf;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Opérations sur les indexs
 */
public class IndexService {

    private static final Logger LOG = LogManager.getLogger(IndexService.class);

    public void rebuildScale(final IndexRebuild indexRebuild){
        final String path = indexRebuild.getIndexPath();
        LOG.info("REBUILD {}", path);
        //TODO continue
    }

    public IndexCreation append(final IndexCreation indexCreation) throws IOException {
        final File sourceFile = new File(Resolver.get(indexCreation.getSourceIndexDir()));
        final Directory sourceDirectory = directoryFromFile(sourceFile);
        final File destFile = new File(Resolver.get(new ConfManager().get(ConfEnum.TIMEWEB, TimeWebConf.class).getIndexDir()));
        final Directory destDirectory = directoryFromFile(destFile);
        final IndexWriterConfig indexWriterConfig = indexWriterConfig();

        LOG.info("FROM {}", sourceFile);
        LOG.info("TO {}", destFile);
        final IndexWriter destIndexWriter = new IndexWriter(destDirectory, indexWriterConfig);
        LOG.info("adding indexes. . .");
        destIndexWriter.addIndexes(sourceDirectory);
        LOG.info("indexWriter.forceMerge(1) . . .");
        destIndexWriter.forceMerge(1);
        LOG.info("indexWriter.close() . . .");
        destIndexWriter.close();

        return indexCreation;
    }

	public void merge(final Merge merge) throws IOException{

        LOG.info(merge);

		final File[] allIndexes = allIndexes(Resolver.get(merge.getMergeableIndexesDir()));
		final File destPath = new File(Resolver.get(merge.getMergedIndexDir()));
		final File biggerIndex = Arrays.stream(allIndexes).max((f1,f2) -> Long.compare(FileUtils.sizeOfDirectory(f1), FileUtils.sizeOfDirectory(f2))).get();
		final Directory destDirectory = directoryFromFile(destPath);
    	final File[] mergeableIndexesFile = Arrays.stream(allIndexes).filter(f -> f != biggerIndex).toArray(File[]::new);
    	final Directory[] mergeableIndexes = Arrays.stream(mergeableIndexesFile).map(this::directoryFromFile).toArray(Directory[]::new);
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
        indexWriterConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);
        indexWriterConfig.setRAMBufferSizeMB(256.0);
        return indexWriterConfig;
    }
	
}
