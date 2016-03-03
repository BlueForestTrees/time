package time.web.service;

import java.util.Arrays;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class QueryService {
    
    /**
     * Construit une requête lucene depuis les paramètres 'Histoire/Time'
     * @param term
     * @param field
     * @param from
     * @param to
     * @return
     */
    public Query getQuery(final String term, final String field, final Long from, final Long to) {
        final boolean hasTermFilter = !StringUtils.isEmpty(term);
        final boolean hasBucketFilter = !StringUtils.isEmpty(field) && (from != null || to != null);
        final Query termQuery = hasTermFilter ? getTermQuery(term.toLowerCase()) : null;
        final Query bucketQuery = hasBucketFilter ? NumericRangeQuery.newLongRange(field, from, to, true, true) : null;

        if (hasBucketFilter && hasTermFilter) {
            return new BooleanQuery.Builder().add(termQuery, Occur.MUST).add(bucketQuery, Occur.MUST).build();
        }else if(hasBucketFilter && !hasTermFilter){
            return bucketQuery;
        }else if(!hasBucketFilter && hasTermFilter){
            return termQuery;
        }else{
            return new MatchAllDocsQuery();
        }
    }

    /**
     * "civilisation moderne" => extrait de phrase
     * chien chat => chien OU chat
     * chien+chat => chien ET chat
     * @param term terme.
     * @return
     */
    protected Query getTermQuery(final String term) {
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

    protected Query getOrTermQuery(final String term) {
        final BooleanQuery.Builder builder = new BooleanQuery.Builder();
        Arrays.stream(term.split(" ")).forEach(t -> builder.add(getAndTermQuery(t), Occur.SHOULD));
        return builder.build();
    }

    protected Query getAndTermQuery(final String term) {
        boolean hasAnds = term.contains("+");
        if(!hasAnds){
            return new TermQuery(new Term("text", term));
        }else{
            final BooleanQuery.Builder builder = new BooleanQuery.Builder();
            Arrays.stream(term.split("\\+")).forEach(t -> builder.add(new TermQuery(new Term("text", t)), Occur.FILTER));
            return builder.build();
        }
    }

    /**
     * Construit une requête de recherche de terme plus approprié.
     * @param term Le terme à améliorer
     * @return Une query fournissant des documents contenant des termes plus appropriés.
     */
    public Query getFuzzyTermQuery(final String term) {
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

    private String[] words(final String term) {
        return term.replaceAll("\"", "").split(" ");
    }

	public Query getQueryForFirstPhrase(String term) {
		final Query rangeQuery = NumericRangeQuery.newLongRange("date", Long.MIN_VALUE, null, true, true);
		final Query termQuery = getTermQuery(term.toLowerCase());
		return new BooleanQuery.Builder().add(rangeQuery, Occur.FILTER).add(termQuery, Occur.FILTER).build();
	}
}

