package wiki.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.SortField.Type;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import wiki.entity.Phrase;

@Service
public class PhraseService {

	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Transactional
	public List<Phrase> find(long date, short pageSize, String word) {
		final FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
		final QueryBuilder qb = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Phrase.class).get();
		final Query dateQuery = qb.range().onField("date").above(date).createQuery();
		
		if(!StringUtils.isEmpty(word)){
			final Query wordQuery = qb.keyword().onFields("text").matching(word).createQuery();
			//TODO créer plusieurs méthodes pour tout les cas de figure et construire les requêtes ailleurs
		}
		
		final FullTextQuery fullTextQuery = fullTextEntityManager.createFullTextQuery(dateQuery, Phrase.class);
		final Sort sort = new Sort(new SortField("date", Type.LONG));

		fullTextQuery.setSort(sort);
		fullTextQuery.setMaxResults(pageSize);

		final List<Phrase> result = (List<Phrase>) fullTextQuery.getResultList();

		return result;
	}
	
	
	public String rebuildIndex() {
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
