package time.web.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.apache.lucene.search.Query;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.SortField.Type;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import time.repo.bean.Phrase;
import time.web.enums.Scale;
import time.web.enums.Sens;
import time.web.tool.QueryHelper;


@Service
public class PhraseService {

	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	private EntityManager entityManager;

	@Autowired
	private int pageSize;
	
	@Autowired
	private QueryHelper queryHelper;
	
	private static final Logger LOG = LoggerFactory.getLogger(PhraseService.class);
	
	@Transactional
	public List<Phrase> find(final Scale scale, final Long bucket, final String word, final Sens sens, final Long page) {
		LOG.debug("find...");
		//Factories
		final FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
		final QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Phrase.class).get();
		
		final Query query = queryHelper.getQuery(scale, bucket, word, queryBuilder);
		final FullTextQuery fullTextQuery = fullTextEntityManager.createFullTextQuery(query, Phrase.class);
		
		//Query settings
		fullTextQuery.setSort(new org.apache.lucene.search.Sort(new SortField("date", Type.LONG, sens == Sens.AVANT)));
		fullTextQuery.setMaxResults(pageSize);
		if (page != null) {
			int startPosition = (int) (page * pageSize);
			fullTextQuery.setFirstResult(startPosition);
		}

		@SuppressWarnings("unchecked")
		final List<Phrase> result = (List<Phrase>) fullTextQuery.getResultList();

		LOG.debug("find   done");
		return result;
	}

}
