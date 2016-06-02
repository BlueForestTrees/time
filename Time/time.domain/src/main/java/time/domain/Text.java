package time.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Text {

	private List<DatedPhrase> phrases = new ArrayList();

	private Metadata metadata = new Metadata();

	public Metadata getMetadata() {
		return metadata;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

	public String[] getParagraphs() {
		return paragraphs;
	}

	public void setParagraphs(String[] paragraphs) {
		this.paragraphs = paragraphs;
	}

	private String[] paragraphs;

	public StringBuilder getContent() {
		return content;
	}

	public void setContent(StringBuilder content) {
		this.content = content;
	}

	private StringBuilder content;
	private StringBuilder hightlightenContent;

	public List<DatedPhrase> getPhrases() {
		return phrases;
	}

	public void addPhrases(final List<DatedPhrase> phrases){
		this.phrases.addAll(phrases);
	}

    public void appendHightlightContent(final String part){
    	if(hightlightenContent == null){
    		hightlightenContent = new StringBuilder();
    	}
    	hightlightenContent.append(part);
		hightlightenContent.append(" ");
    }
    public String getTextString() {
        return content.toString();
    }

	public String getHightlightTextString() {
		return hightlightenContent.toString();
	}

    public void setText(String text) {
		if(text == null){
			throw new RuntimeException("Text.setText(null)");
		}
        this.content = new StringBuilder(text);
    }
    
    public StringBuilder getText(){
        return this.content;
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
