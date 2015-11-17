package time.web;

import java.io.IOException;
import java.nio.file.FileSystems;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StringUtils;

import time.web.bean.BucketGroup;
import time.web.config.ComponentConfig;
import time.web.enums.Scale;
import time.web.service.BucketService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={ComponentConfig.class})
public class LuceneServiceTest {
    
    @Autowired
    BucketService bucketService;

    @Test
    public void testGetBuckets() throws IOException{
        final Scale scale = Scale.TEN3;//TEN6 lui renvoie plein de result sur le bucket enfant 0
        final String term = "vivre";
        final BucketGroup buckets = bucketService.getBuckets(scale, term);
        
        System.out.println(buckets);
    }
    
    @Test
    public void testLuceneDoc() throws IOException{
        String indexPath = "/Time/data/phrases";
        FSDirectory indexDir = FSDirectory.open(FileSystems.getDefault().getPath(indexPath));
        DirectoryReader indexReader = DirectoryReader.open(indexDir);
        IndexSearcher searcher = new IndexSearcher(indexReader);

        
        Query query = getQuery(null, "dateByTen9", -138L);
        
        TopDocs docs = searcher.search(query,10);
        System.out.println(docs.totalHits);
        for(ScoreDoc scoreDoc : docs.scoreDocs){
            Document doc = searcher.doc(scoreDoc.doc);
            System.out.println(doc);
        }
    }
    
    @Test
    public void testSearchAfter() throws IOException{
        String indexPath = "/Time/data/somephrases";
        FSDirectory indexDir = FSDirectory.open(FileSystems.getDefault().getPath(indexPath));
        DirectoryReader indexReader = DirectoryReader.open(indexDir);
        IndexSearcher searcher = new IndexSearcher(indexReader);

        
        TopDocs result = searcher.searchAfter(null, new MatchAllDocsQuery(), 10);
        display(result);
        
        ScoreDoc after = result.scoreDocs[result.scoreDocs.length-1];
        TopDocs result2 = searcher.searchAfter(after, new MatchAllDocsQuery(), 10);
        display(result2);
        
        ScoreDoc after2 = copy(result2.scoreDocs[result.scoreDocs.length-1]);
        TopDocs result3 = searcher.searchAfter(after2, new MatchAllDocsQuery(), 10);
        display(result3);
    }
    
    private void display(TopDocs result) {
        System.out.println(result.scoreDocs);
    }

    private ScoreDoc copy(ScoreDoc scoreDoc) {
        ScoreDoc result = new ScoreDoc(scoreDoc.doc, scoreDoc.score);
        
        return result;
    }

    public Query getQuery(String term, String bucketName, Long bucketValue){
        boolean noBucket = StringUtils.isEmpty(bucketName) || bucketValue == null;
        boolean noTerm = StringUtils.isEmpty(term);
        
        if(noBucket && noTerm){
            return new MatchAllDocsQuery();
        }
        
        TermQuery textQuery = noTerm ? null : new TermQuery(new Term("text", term));
        Query bucketQuery = noBucket ? null : NumericRangeQuery.newLongRange(bucketName, bucketValue, bucketValue, true, true);
        
        
        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        if(textQuery != null){
            builder.add(textQuery, Occur.MUST);
        }
        if(bucketQuery != null){
            builder.add(bucketQuery, Occur.MUST);
        }
        return builder.build();
    }

}