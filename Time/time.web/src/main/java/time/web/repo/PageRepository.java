package time.web.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import time.repo.bean.Page;

@Repository
public interface PageRepository  extends JpaRepository<Page, Long>{
	public Page findByUrl(String url);
}
