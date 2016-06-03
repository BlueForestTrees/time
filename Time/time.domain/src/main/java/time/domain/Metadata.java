package time.domain;

public class Metadata {
    public static final String EXT = ".meta";

    private String titre;
    private String auteur;
    private String date;
    private Integer paragraphes;
    private Integer phrases;
    private String url;
    private String identifier;
    private String comments;
    private String filename;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getParagraphes() {
        return paragraphes;
    }

    public void setParagraphes(Integer paragraphes) {
        this.paragraphes = paragraphes;
    }

    public Integer getPhrases() {
        return phrases;
    }

    public void setPhrases(Integer phrases) {
        this.phrases = phrases;
    }

    @Override
    public String toString() {
        return "Metadata{" +
                "titre='" + titre + '\'' +
                ", auteur='" + auteur + '\'' +
                ", date='" + date + '\'' +
                ", paragraphes=" + paragraphes +
                ", phrases=" + phrases +
                ", url='" + url + '\'' +
                ", identifier='" + identifier + '\'' +
                ", comments='" + comments + '\'' +
                ", filename='" + filename + '\'' +
                '}';
    }
}
