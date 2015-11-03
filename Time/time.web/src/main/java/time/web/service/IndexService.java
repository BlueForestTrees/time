package time.web.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class IndexService {

	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	private EntityManager entityManager;

	private static final Logger LOG = LoggerFactory.getLogger(IndexService.class);
	
	public String reIndex() {
		LOG.debug("reIndex...");
		String result = null;
		final FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
		try {
			fullTextEntityManager.createIndexer().startAndWait();
			result = "OK";
		} catch (InterruptedException e) {
			LOG.error("fullTextEntityManager.createIndexer().startAndWait();", e);
			result = e.getMessage();
		}
		
		LOG.debug("reIndex   done.");
		return result;
	}
}
