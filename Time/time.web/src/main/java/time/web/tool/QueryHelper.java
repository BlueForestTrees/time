package time.web.tool;

import java.util.Arrays;

import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import time.web.enums.Scale;

@Component
public class QueryHelper {
	
	/**
	 * La requete fulltext sur laquelle sera généré les facets.
	 * 
	 * @param scale
	 * @param bucket
	 * @param word
	 * @param queryBuilder
	 * @return
	 */
	public Query getQuery(final Scale scale, final Long bucket, final String word, final QueryBuilder queryBuilder) {
		final Query textFilter = StringUtils.isEmpty(word) ? null : queryBuilder.keyword().onField("text").matching(word).createQuery();
		final Query bucketFilter = (scale.getParent() == null || bucket == null) ? null : queryBuilder.keyword().onField(scale.getParent().getField()).matching(bucket).createQuery();
		final Query finalQuery = getAndQuery(textFilter, bucketFilter);
		
		return finalQuery != null ? finalQuery : queryBuilder.all().createQuery();
	}
	
	public BooleanQuery getAndQuery(final Query... queries) {

		if(Arrays.stream(queries).allMatch(o -> o == null)){
			return null;
		}
		
		final BooleanQuery.Builder andQuery = new BooleanQuery.Builder();
		for (Query query : queries) {
			if (query != null) {
				andQuery.add(query, Occur.MUST);
			}
		}

		return andQuery.build();
	}
}
