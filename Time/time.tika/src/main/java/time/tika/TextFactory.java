package time.tika;

import com.google.inject.Inject;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import time.domain.Text;
import time.tool.url.UrlTo;
import time.transform.CompositeTextTransformer;
import time.transform.ITextTransformer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class TextFactory {

    private final ITextTransformer textTransformer;

    @Inject
    public TextFactory(final ITextTransformer textTransformer){
        this.textTransformer = textTransformer;
    }

    public Text build(final String filename) throws FileNotFoundException {
        final Text text = build(new FileInputStream(filename));
        text.getMetadata().setFile(filename);
        return text;
    }

    public Text buildFromUrl(final String url) throws IOException {
        final Text text = build(UrlTo.inputStream(url));
        text.getMetadata().setUrl(url);
        return text;
    }

    public Text build(final String url, final String title, final String textString){
        final Text text = new Text();
        text.getMetadata().setUrl(url);
        text.getMetadata().setTitre(title);
        text.setText(textString);
        textTransformer.transform(text);
        return text;
    }

    public Text build(final InputStream input){
        final ContentHandler textHandler = new BodyContentHandler(Integer.MAX_VALUE);
        final Metadata tikaMetadata = new Metadata();
        try {
            final AutoDetectParser parser = new AutoDetectParser();
            parser.parse(input, textHandler, tikaMetadata);
            input.close();
        } catch (IOException | SAXException | TikaException e) {
            throw new RuntimeException("Parsing fichier", e);
        }

        final Text text = new Text();
        final time.domain.Metadata metadata = text.getMetadata();

        metadata.setIdentifier(tikaMetadata.get(TikaCoreProperties.IDENTIFIER));
        metadata.setTitre(tikaMetadata.get(TikaCoreProperties.TITLE));
        metadata.setAuteur(tikaMetadata.get(TikaCoreProperties.CREATOR));
        metadata.setDate(tikaMetadata.get(TikaCoreProperties.CREATED));
        metadata.setComments(tikaMetadata.get(TikaCoreProperties.COMMENTS));
        metadata.setUrl(metadata.getTitre());
        text.setText(textHandler.toString());

        textTransformer.transform(text);
        return text;
    }
}
