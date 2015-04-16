package wiki.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity(name = "page")
@Table(name = "page")
public class Page {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@Column(name = "url", nullable = false, unique = true)
	private String url;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "text")
	private String text;
	
	@Column(name = "liens")
	private String liens;
	
	@Column(name="nbLiens")
	private Integer nbLiens;
	
	public Integer getNbLiens() {
		return nbLiens;
	}

	public void setNbLiens(Integer nbLiens) {
		this.nbLiens = nbLiens;
	}

	@Column(name="depth")
	private Integer depth;
	
	public Integer getDepth() {
		return depth;
	}

	public void setDepth(Integer depth) {
		this.depth = depth;
	}

	public String getLiens() {
		return liens;
	}

	public void setLiens(String liens) {
		this.liens = liens;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
