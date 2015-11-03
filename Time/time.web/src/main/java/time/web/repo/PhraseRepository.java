package time.web.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import time.repo.bean.Phrase;

@Repository
public interface PhraseRepository extends JpaRepository<Phrase, Long> {

}
