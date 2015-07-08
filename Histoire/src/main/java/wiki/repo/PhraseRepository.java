package wiki.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import wiki.entity.Phrase;

@Repository
public interface PhraseRepository  extends JpaRepository<Phrase, Long>{
	
}
