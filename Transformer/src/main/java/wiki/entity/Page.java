package wiki.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.Transient;


@Entity(name = "page")
@Table(name = "page")
public class Page {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@Column(name = "url")
	private String url;
	
	@Column(name="depth")
	private Integer depth;
	
	@Column(name="nbLiensOut")
	private Integer nbLiensOut;
	
	@Transient
	private transient List<String> liens;
	

	public List<String> getLiens() {
		return liens;
	}

	public void setLiens(List<String> liens) {
		this.liens = liens;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getDepth() {
		return depth;
	}

	public void setDepth(Integer depth) {
		this.depth = depth;
	}

	public Integer getNbLiensOut() {
		return nbLiensOut;
	}

	public void setNbLiensOut(Integer nbLiensOut) {
		this.nbLiensOut = nbLiensOut;
	}
	
}
