package time.web.service;

import java.util.Arrays;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.util.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class QueryService {
    
    @Autowired
    private IndexReader reader;
    
    /**
     * Construit une requête lucene depuis les paramètres 'Histoire/Time'
     * @param term
     * @param scale
     * @param bucketValueFrom
     * @param bucketValueTo
     * @return
     */
    public Query getQuery(final String term, final String scale, Long bucketValueFrom, Long bucketValueTo) {
        boolean noBucket = StringUtils.isEmpty(scale) || (bucketValueFrom == null && bucketValueTo == null);
        boolean noTerm = StringUtils.isEmpty(term);

        if (noBucket && noTerm) {
            return new MatchAllDocsQuery();
        }

        Query textQuery = noTerm ? null : getTermQuery(term.toLowerCase());
        Query bucketQuery = noBucket ? null : NumericRangeQuery.newLongRange(scale, bucketValueFrom, bucketValueTo, true, true);

        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        if (textQuery != null) {
            builder.add(textQuery, Occur.MUST);
        }
        if (bucketQuery != null) {
            builder.add(bucketQuery, Occur.MUST);
        }
        return builder.build();
    }

    /**
     * "civilisation moderne" => extrait de phrase
     * chien chat => chien OU chat
     * chien+chat => chien ET chat
     * @param term terme.
     * @return
     */
    protected Query getTermQuery(String term) {
        final boolean isPhrase = term.startsWith("\"") && term.endsWith("\"");
        final boolean hasOrs = term.contains(" ");
        
        if(isPhrase){
            final String[] words = words(term);
            if(words.length == 1){
                return getAndTermQuery(words[0]);
            }else{
                return new PhraseQuery(1, "text", words);
            }
        }else if(hasOrs){
            return getOrTermQuery(term);
        }else{
            return getAndTermQuery(term);
        }
    }

    protected Query getOrTermQuery(String term) {
        final BooleanQuery.Builder builder = new BooleanQuery.Builder();
        Arrays.stream(term.split(" ")).forEach(t -> builder.add(getAndTermQuery(t), Occur.SHOULD));
        return builder.build();
    }

    protected Query getAndTermQuery(String term) {
        boolean hasAnds = term.contains("+");
        if(!hasAnds){
            return new TermQuery(new Term("text", term));
        }else{
            final BooleanQuery.Builder builder = new BooleanQuery.Builder();
            Arrays.stream(term.split("\\+")).forEach(t -> builder.add(new TermQuery(new Term("text", t)), Occur.MUST));
            return builder.build();
        }
    }

    /**
     * Construit une requête de recherche de terme plus approprié.
     * @param term Le terme à améliorer
     * @return Une query fournissant des documents contenant des termes plus appropriés.
     */
    public Query getFuzzyTermQuery(String term) {
        final boolean isPhrase = term.startsWith("\"") && term.endsWith("\"");
        final boolean hasOrs = term.contains(" ");
        final boolean hasAnds = term.contains("+");
        final boolean isSimpleWord = !isPhrase && !hasOrs && !hasAnds;
        
        if(isSimpleWord){
            return new FuzzyQuery(new Term("text", term));
        }else if(isPhrase){
            return getFuzzyTermQuery(term);
        }else{
            return null;
        }
    }

    private Query getFuzzyPhraseQuery(String term) {
        return new QueryBuilder(null).createPhraseQuery("text", term);
    }

    private String[] words(String term) {
        return term.replaceAll("\"", "").split(" ");
    }
}

