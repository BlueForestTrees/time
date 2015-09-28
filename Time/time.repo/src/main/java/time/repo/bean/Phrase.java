package time.repo.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Facet;
import org.hibernate.search.annotations.FacetEncodingType;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.NumericField;
import org.hibernate.search.annotations.Store;


@Entity(name = "phrase")
@Table(name = "phrase")
@Indexed
public class Phrase {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private Long pageId;
	
	@Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO)
	private String text;
	
	/**
	 * TODO voir SimpleFacetsExample
	 * doc.add(new FacetField("Publish Date", "2010", "10", "15"));
	 * utiliser un champ pour tout les niveaux avec un 'path'
	 */
	@Field(analyze = Analyze.NO)
	@Facet(encoding = FacetEncodingType.STRING)
	private long dateByTen;

	@NumericField
	@Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO)
	private long date;
	
	private Datation datation;


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
	
	public Datation getType() {
		return datation;
	}
	
	public void setType(Datation datation) {
		this.datation = datation;
	}
	
	public long getDateByTen() {
		return dateByTen;
	}

	public void setDateByTen(long dateByTen) {
		this.dateByTen = dateByTen;
	}
}
