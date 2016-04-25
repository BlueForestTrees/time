package time.tika;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;

public class Page {
    private String identifier;
    private String title;
    private String creator;
    private String created;
    private String language;
    private String type;
    private String comments;
    private String content;

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

    public String getContent() {
        return content;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public void setContent(String content) {
        this.content = content;
    }
}