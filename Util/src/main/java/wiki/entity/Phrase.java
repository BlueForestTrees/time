package wiki.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import wiki.tool.phrasefinder.PhraseFinder;


@Entity(name = "phrase")
@Table(name = "phrase")
//@Indexed
public class Phrase {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private Long pageId;
	
	//@Field(index=Index.YES, analyze=Analyze.YES)
	private String text;
	
	//@Field(index=Index.YES)
	private long date;
	
	private PhraseFinder type;


	public Long getPageId() {
		return pageId;
	}

	public void setPageId(Long pageId) {
		this.pageId = pageId;
	}
	
	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public PhraseFinder getType() {
		return type;
	}
	
	public void setType(PhraseFinder type) {
		this.type = type;
	}
}
