package time.repo.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Text implements Serializable {

    private static final long serialVersionUID = 1591695335410659944L;
    private String url;
    private String metadata;
	private String identifier;
	private String title;
	private String creator;
	private String created;
	private String language;
	private String type;
	private String comments;
	private List<DatedPhrase> phrases = new ArrayList();
	private StringBuilder content;
	private StringBuilder hightlightenContent;

	public List<DatedPhrase> getPhrases() {
		return phrases;
	}

	public void addPhrases(final List<DatedPhrase> phrases){
		this.phrases.addAll(phrases);
	}

	public String getIdentifier() {
		return identifier;
	}

	public String getTitle() {
		return title;
	}

	public String getCreator() {
		return creator;
	}

	public String getCreated() {
		return created;
	}

	public String getLanguage() {
		return language;
	}

	public String getType() {
		return type;
	}

	public String getComments() {
		return comments;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

    public void appendHightlightContent(final String part){
    	if(hightlightenContent == null){
    		hightlightenContent = new StringBuilder();
    	}
    	hightlightenContent.append(part);
    }
    public String getTextString() {
        return content.toString();
    }

	public String getHightlightTextString() {
		return hightlightenContent.toString();
	}

    public void setText(String text) {
        this.content = new StringBuilder(text);
    }
    
    public StringBuilder getText(){
        return this.content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

	public String getMetadata() {
		return metadata;
	}

	public void setMetadata(String metadata) {
		this.metadata = metadata;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public long nbDatedPhrasesCount() {
		return phrases.size();
	}
}
