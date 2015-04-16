package wiki.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import wiki.entity.Lien;

@Repository
public interface LienRepository  extends JpaRepository<Lien, Long>{

}
