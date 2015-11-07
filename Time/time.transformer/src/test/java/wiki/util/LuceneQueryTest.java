package wiki.util;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

public class LuceneTest {

	@Test
	public void testLucene() throws IOException, ParseException{
		Analyzer analyzer = new StandardAnalyzer();

	    // Store the index in memory:
	    Directory directory = new RAMDirectory();
	    // To store an index on disk, use this instead:
	    //Directory directory = FSDirectory.open("/tmp/testindex");
	    IndexWriterConfig config = new IndexWriterConfig(analyzer);
	    IndexWriter iwriter = new IndexWriter(directory, config);
	    Document doc = new Document();
	    doc.add(new Field("text", "This is the 端午節滅螻蜅 節端陽 節重五 節𢷄螻蜅 節重耳 phrase to be indexed.", TextField.TYPE_STORED));
	    doc.add(new LongField("date", 123234536L, LongField.TYPE_NOT_STORED));
	    
	    iwriter.addDocument(doc);
	    iwriter.close();
	    
	    // Now search the index:
	    DirectoryReader ireader = DirectoryReader.open(directory);
	    IndexSearcher isearcher = new IndexSearcher(ireader);
	    // Parse a simple query that searches for "text":
	    QueryParser parser = new QueryParser("text", analyzer);
	    Query query = parser.parse("午");
	    ScoreDoc[] hits = isearcher.search(query, 1000).scoreDocs;
	    assertEquals(1, hits.length);
	    // Iterate through the results:
	    for (int i = 0; i < hits.length; i++) {
	      Document hitDoc = isearcher.doc(hits[i].doc);
	      assertEquals("This is the 端午節滅螻蜅 節端陽 節重五 節𢷄螻蜅 節重耳 phrase to be indexed.", hitDoc.get("text"));
	    }
	    ireader.close();
	    directory.close();
	}
	
}
