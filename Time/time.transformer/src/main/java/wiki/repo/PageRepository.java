package wiki.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import time.repo.bean.Page;

@Repository
public interface PageRepository  extends JpaRepository<Page, Long>{
	@Query("select id from page p where p.url=:url")
	public Long getIdByUrl(@Param("url")String url);

	@Modifying 
	@Query("update page p set p.nbLiensIn =:toIdCount where p.id=:toId")
	public void updateNbLiensIn(@Param("toId")Long toId,@Param("toIdCount") Long toIdCount);
}
