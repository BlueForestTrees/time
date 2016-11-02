package time.web.service;

import org.apache.lucene.document.LongPoint;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import time.tool.reference.Fields;
import time.web.bean.TermPeriodFilter;

import java.util.Arrays;

/**
 * Génère les requêtes lucene depuis une requête histoires
 */
@Component
public class QueryService {

    @Autowired
    QueryParser textQueryParser;
	
    /**
     * Construit une requête lucene depuis une requête histoires
     * @param termPeriodFilter
     * @return
     */
    public Query getQuery(final TermPeriodFilter termPeriodFilter) {
        final boolean hasTermFilter = termPeriodFilter.hasWords();
        final boolean hasPeriodFilter = termPeriodFilter.hasPeriod();
        final Query termQuery = hasTermFilter ? getTermQuery(termPeriodFilter.getWords().toLowerCase()) : null;
        final Query periodQuery = hasPeriodFilter ? LongPoint.newRangeQuery(Fields.DATE, termPeriodFilter.getFrom(), termPeriodFilter.getTo()) : null;

        if(hasPeriodFilter && hasTermFilter){
        	return new BooleanQuery.Builder().add(termQuery, Occur.MUST).add(periodQuery, Occur.MUST).build();
        }else if(hasPeriodFilter){
        	return periodQuery;
        }else if(hasTermFilter){
            return termQuery;
        }else{
            return new MatchAllDocsQuery();
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
            return getWordQuery(term);
        }else{
            final BooleanQuery.Builder builder = new BooleanQuery.Builder();
            Arrays.stream(term.split("\\+")).forEach(t -> builder.add(getWordQuery(t), Occur.FILTER));
            return builder.build();
        }
    }

    /**
     * Requête pour un mot.
     * @param word
     * @return
     */
	private Query getWordQuery(final String word) {
		final boolean hasWildCards = word.contains("*");
		if(hasWildCards){
			return new WildcardQuery(new Term("text", word));
		}else{
            try {
                return textQueryParser.parse(word);
            } catch (ParseException e) {
                return null;
            }
        }
	}

    private String[] words(final String term) {
        return term.replaceAll("\"", "").split(" ");
    }
}

