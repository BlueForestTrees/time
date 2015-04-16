package wiki.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import wiki.entity.Page;

@Repository
public interface PageRepository  extends JpaRepository<Page, Long>{
	public Page findByUrl(String url);
}
