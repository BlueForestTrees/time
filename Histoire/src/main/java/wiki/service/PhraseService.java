package wiki.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.SortField.Type;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import wiki.entity.Phrase;
import wiki.enums.Sens;

@Service
public class PhraseService {

	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	private EntityManager entityManager;

	@Autowired
	private int pageSize;

	@Transactional
	public List<Phrase> find(Long date, String word, Sens sens) {
		final FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
		final QueryBuilder qb = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Phrase.class).get();

		return getResultList(fullTextEntityManager, getAndQuery(getWordQuery(word, qb), getDateQuery(date, qb, sens)), sens);
	}


	private BooleanQuery getAndQuery(final Query... queries) {
		final BooleanQuery andQuery = new BooleanQuery();
		for(Query query : queries){
			if(query != null){
				andQuery.add(query, Occur.MUST);
			}
		}
		return andQuery;
	}
	
	private Query getDateQuery(Long date, final QueryBuilder qb, Sens sens) {
		if(date == null){
			return null;
		}
		if(sens == Sens.apres){
			return qb.range().onField("date").above(date).createQuery();
		}else{
			return qb.range().onField("date").below(date).createQuery();
		}
	}

	private Query getWordQuery(String word, final QueryBuilder qb) {
		if(StringUtils.isEmpty(word)){
			return null;
		}
		return qb.keyword().onFields("text").matching(word).createQuery();
	}
	private List<Phrase> getResultList(final FullTextEntityManager fullTextEntityManager, final Query query, Sens sens) {
		final FullTextQuery fullTextQuery = fullTextEntityManager.createFullTextQuery(query, Phrase.class);
		fullTextQuery.setSort(new org.apache.lucene.search.Sort(new SortField("date", Type.LONG, sens == Sens.avant)));
		fullTextQuery.setMaxResults(pageSize);

		@SuppressWarnings("unchecked")
		final List<Phrase> result = (List<Phrase>) fullTextQuery.getResultList();

		return result;
	}

	
	public String reIndex() {
		String result = null;
		final FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
		try {
			fullTextEntityManager.createIndexer().startAndWait();
			result = "OK";
		} catch (InterruptedException e) {
			e.printStackTrace();
			result = e.getMessage();
		}
		return result;
	}

}
