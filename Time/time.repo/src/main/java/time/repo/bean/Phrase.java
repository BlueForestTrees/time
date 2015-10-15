package time.repo.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "phrase")
@Table(name = "phrase")
//@Indexed
public class Phrase {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private Long pageId;

	//@Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
	private String text;

	/**
	 * TODO voir SimpleFacetsExample doc.add(new FacetField("Publish Date",
	 * "2010", "10", "15")); utiliser un champ pour tout les niveaux avec un
	 * 'path' Voir les dates avec résolution jour avec java.time
	 */
	//@Field(analyze = Analyze.NO)
	//@Facet(encoding = FacetEncodingType.STRING)
	private long dateByTen;

	//@Field(analyze = Analyze.NO)
	//@Facet(encoding = FacetEncodingType.STRING)
	private long dateByTen3;

	//@Field(analyze = Analyze.NO)
	//@Facet(encoding = FacetEncodingType.STRING)
	private long dateByTen6;

	//@Field(analyze = Analyze.NO)
	//@Facet(encoding = FacetEncodingType.STRING)
	private long dateByTen9;

	//@NumericField
	//@Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
	private long date;

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

	public long getDateByTen() {
		return dateByTen;
	}

	public void setDateByTen(long dateByTen) {
		this.dateByTen = dateByTen;
	}

	public long getDateByTen3() {
		return dateByTen3;
	}

	public void setDateByTen3(long dateByTen3) {
		this.dateByTen3 = dateByTen3;
	}

	public long getDateByTen6() {
		return dateByTen6;
	}

	public void setDateByTen6(long dateByTen6) {
		this.dateByTen6 = dateByTen6;
	}

	public long getDateByTen9() {
		return dateByTen9;
	}

	public void setDateByTen9(long dateByTen9) {
		this.dateByTen9 = dateByTen9;
	}

	public Long getPageId() {
		return pageId;
	}

	public void setPageId(Long pageId) {
		this.pageId = pageId;
	}
}
