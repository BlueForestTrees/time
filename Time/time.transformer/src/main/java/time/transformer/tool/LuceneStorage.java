package time.transformer.tool;

import java.io.IOException;
import java.nio.file.FileSystems;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.facet.FacetsConfig;
import org.apache.lucene.facet.sortedset.SortedSetDocValuesFacetField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import time.repo.bean.Phrase;

@Component
public class LuceneStorage implements IStorage {

    Analyzer analyzer;
    Directory directory;
    IndexWriterConfig indexWriterConfig;
    IndexWriter iwriter;
    FacetsConfig config;
    
    @Autowired
    private String indexPath;

    @Override
    public void start() throws IOException {
        analyzer = new StandardAnalyzer();
        directory = FSDirectory.open(FileSystems.getDefault().getPath(indexPath));
        indexWriterConfig = new IndexWriterConfig(analyzer);
        indexWriterConfig.setOpenMode(OpenMode.CREATE);
        indexWriterConfig.setRAMBufferSizeMB(256.0);
        
        iwriter = new IndexWriter(directory, indexWriterConfig);
        
        config = new FacetsConfig();
    }

    @Override
    public void store(Phrase phrase) throws IOException {

        final Document doc = new Document();
        doc.add(new TextField("text", phrase.getText(), Store.YES));
        doc.add(new LongField("date", phrase.getDate(), Store.YES));
        
        doc.add(new LongField("dateByTen", phrase.getDateByTen(), Store.NO));
        doc.add(new LongField("dateByTen3", phrase.getDateByTen3(), Store.NO));
        doc.add(new LongField("dateByTen6", phrase.getDateByTen6(), Store.NO));
        doc.add(new LongField("dateByTen9", phrase.getDateByTen9(), Store.NO));
        
        doc.add(new SortedSetDocValuesFacetField("dateByTen", String.valueOf(phrase.getDateByTen())));
        doc.add(new SortedSetDocValuesFacetField("dateByTen3", String.valueOf(phrase.getDateByTen3())));
        doc.add(new SortedSetDocValuesFacetField("dateByTen6", String.valueOf(phrase.getDateByTen6())));
        doc.add(new SortedSetDocValuesFacetField("dateByTen9", String.valueOf(phrase.getDateByTen9())));

        iwriter.addDocument(config.build(doc));
    }

    @Override
    public void end() throws IOException {
        iwriter.forceMerge(1);
        iwriter.close();
    }

}
