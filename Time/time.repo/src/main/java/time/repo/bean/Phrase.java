package time.repo.bean;

import javax.persistence.Column;
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
import org.hibernate.search.annotations.SortableField;
import org.hibernate.search.annotations.Store;

@Entity(name = "phrase")
@Table(name = "phrase")
@Indexed
public class Phrase {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long pageId;

    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    @Column(length = 4000)
    private String text;

    @Field(analyze = Analyze.NO)
    @Facet(encoding = FacetEncodingType.STRING)
    private long dateByTen;

    @Field(analyze = Analyze.NO)
    @Facet(encoding = FacetEncodingType.STRING)
    private long dateByTen3;

    @Field(analyze = Analyze.NO)
    @Facet(encoding = FacetEncodingType.STRING)
    private long dateByTen6;

    @Field(analyze = Analyze.NO)
    @Facet(encoding = FacetEncodingType.STRING)
    private long dateByTen9;

    @NumericField
    @SortableField
    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
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
