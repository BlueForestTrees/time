package time.repo.bean;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity(name = "page")
@Table(name = "page")
public class Page implements Serializable {

    private static final long serialVersionUID = 1591695335410659944L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long id;

    @Column
    private String url;

    @Column
    private Integer depth;

    @Column
    private Integer nbLiensOut;

    @Column
    private Integer nbLiensIn;

    @Transient
    private transient List<String> liens;

    @Transient
    private transient String pageContent;

    public String getPageContent() {
        return pageContent;
    }

    public void setPageContent(String text) {
        this.pageContent = text;
    }

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

    public Integer getNbLiensIn() {
        return nbLiensIn;
    }

    public void setNbLiensIn(Integer nbLiensIn) {
        this.nbLiensIn = nbLiensIn;
    }

}
