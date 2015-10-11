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

	private static Logger log = LoggerFactory.getLogger(IndexService.class);
	
	public String reIndex() {
		log.debug("reIndex...");
		String result = null;
		final FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
		try {
			fullTextEntityManager.createIndexer().startAndWait();
			result = "OK";
		} catch (InterruptedException e) {
			e.printStackTrace();
			result = e.getMessage();
		}
		
		log.debug("reIndex   done.");
		return result;
	}
}
