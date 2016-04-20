package time.repo.bean;

import java.io.Serializable;
import java.util.List;

public class Page implements Serializable {

    private static final long serialVersionUID = 1591695335410659944L;
    private String url;
    private Integer depth;
    private Integer nbLiensOut;
    private Integer nbLiensIn;
    private String metadata;
    private List<String> liens;
    private StringBuilder content;
    private String title;
    private StringBuilder hightlightenContent;
    
    public void appendHightlightContent(final String part){
    	if(hightlightenContent == null){
    		hightlightenContent = new StringBuilder();
    	}
    	hightlightenContent.append(part);
    }
    public String getTextString() {
        return content.toString();
    }

    public void setText(String text) {
        this.content = new StringBuilder(text);
    }
    
    public StringBuilder getText(){
        return this.content;
    }

    public List<String> getLiens() {
        return liens;
    }

    public void setLiens(List<String> liens) {
        this.liens = liens;
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

	public String getMetadata() {
		return metadata;
	}

	public void setMetadata(String metadata) {
		this.metadata = metadata;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getHightlightenContent() {
		return hightlightenContent.toString();
	}
	
	public void openParagraph() {
		appendHightlightContent("<div class=\"paragraph\">");
	}
	public void closeParagraph() {
		appendHightlightContent("</div>");
	}
	public void startPhrase() {
		appendHightlightContent("<span class=\"phrase\">");
	}
	public void endPhrase() {
		appendHightlightContent("</span>");
	}

}
