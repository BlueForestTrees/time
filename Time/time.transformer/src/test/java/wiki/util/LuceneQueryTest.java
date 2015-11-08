package wiki.util;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.facet.FacetResult;
import org.apache.lucene.facet.Facets;
import org.apache.lucene.facet.FacetsCollector;
import org.apache.lucene.facet.sortedset.DefaultSortedSetDocValuesReaderState;
import org.apache.lucene.facet.sortedset.SortedSetDocValuesFacetCounts;
import org.apache.lucene.facet.sortedset.SortedSetDocValuesReaderState;
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
import org.springframework.util.StringUtils;

public class LuceneQueryTest {

    //String term = "civilisation";
    String term;
    String bucketName = "dateByTen6";
    //String bucketName;
    Long bucketValue = -167L;
    //String bucketValue;
    
    @Test
    public void testLuceneFacets() throws IOException {
        String indexPath = "/Time/data/phrases";
        FSDirectory indexDir = FSDirectory.open(FileSystems.getDefault().getPath(indexPath));
        DirectoryReader indexReader = DirectoryReader.open(indexDir);
        IndexSearcher searcher = new IndexSearcher(indexReader);
        SortedSetDocValuesReaderState readerState = new DefaultSortedSetDocValuesReaderState(indexReader);
        FacetsCollector facetsCollector = new FacetsCollector();
        
        
        FacetsCollector.search(searcher, getQuery(term, bucketName, bucketValue), 10, facetsCollector);
        Facets facets = new SortedSetDocValuesFacetCounts(readerState, facetsCollector);

        List<FacetResult> allDims = facets.getAllDims(10);
        System.out.println(allDims);
        
        indexReader.close();
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
