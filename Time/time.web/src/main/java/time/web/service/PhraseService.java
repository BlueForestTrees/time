package time.web.service;

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
import org.hibernate.search.query.engine.spi.FacetManager;
import org.hibernate.search.query.facet.Facet;
import org.hibernate.search.query.facet.FacetSortOrder;
import org.hibernate.search.query.facet.FacetingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import time.repo.bean.Phrase;
import time.web.bean.FacetsDTO;
import time.web.enums.Scale;
import time.web.enums.Sens;
import time.web.transformer.FacetTransformer;

@Service
public class PhraseService {

	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	private EntityManager entityManager;

	@Autowired
	private int pageSize;
	
	@Autowired
	private FacetTransformer facetTransformer;
	
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

	@Transactional
	public List<Facet> timeFacets(Scale scale, String word, Long page) {
		//Factories
		final FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
		final QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Phrase.class).get();
		
		//Facet query
		final FacetingRequest phraseFacetingRequest = queryBuilder.facet()
			    .name("phrases")
			    .onField(scale.getField())
			    .discrete()
			    .orderedBy(FacetSortOrder.FIELD_VALUE)
			    .includeZeroCounts(false)
			    .createFacetingRequest();
		
		// Query
		Query query = null;
		if (!StringUtils.isEmpty(word)) {
			query = queryBuilder.keyword().onFields("text").matching(word).createQuery();
		}else{
			query = queryBuilder.all().createQuery();
		}
		final FullTextQuery fullTextQuery = fullTextEntityManager.createFullTextQuery(query, Phrase.class);
		
		// retrieve facet manager and apply faceting request
		final FacetManager facetManager = fullTextQuery.getFacetManager();
		facetManager.enableFaceting(phraseFacetingRequest);
		
		final List<Facet> facets = facetManager.getFacets("phrases");
		return facets;
	}

	@Transactional
	public List<Phrase> find(Long date, String word, Sens sens, Long page) {
		//Factories
		final FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
		final QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Phrase.class).get();
		
		//Query
		Query dateQuery = null;
		if (sens == Sens.apres) {
			dateQuery = queryBuilder.range().onField("date").above(date).createQuery();
		} else {
			dateQuery = queryBuilder.range().onField("date").below(date).createQuery();
		}
		Query wordQuery = null;
		if (!StringUtils.isEmpty(word)) {
			wordQuery = queryBuilder.keyword().onFields("text").matching(word).createQuery();
		}
		final BooleanQuery query = getAndQuery(wordQuery, dateQuery);
		final FullTextQuery fullTextQuery = fullTextEntityManager.createFullTextQuery(query, Phrase.class);
		
		//Query settings
		fullTextQuery.setSort(new org.apache.lucene.search.Sort(new SortField("date", Type.LONG, sens == Sens.avant)));
		fullTextQuery.setMaxResults(pageSize);
		if (page != null) {
			int startPosition = (int) (long) (page * pageSize);
			fullTextQuery.setFirstResult(startPosition);
		}

		@SuppressWarnings("unchecked")
		final List<Phrase> result = (List<Phrase>) fullTextQuery.getResultList();

		return result;
	}

	public FacetsDTO timeFacetsDTO( Scale scale, String word, Long page) {
		final List<Facet> timeFacets = timeFacets(scale, word, page);
		return facetTransformer.toFacetsDTO(timeFacets);
	}

	private BooleanQuery getAndQuery(final Query... queries) {
		final BooleanQuery.Builder andQuery = new BooleanQuery.Builder();
		for (Query query : queries) {
			if (query != null) {
				andQuery.add(query, Occur.MUST);
			}
		}
		return andQuery.build();
	}
}
