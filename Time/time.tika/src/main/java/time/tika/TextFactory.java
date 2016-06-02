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
        return build(new FileInputStream(filename));
    }

    public Text buildFromUrl(final String url) throws IOException {
        final Text text = build(UrlTo.inputStream(url));
        text.setUrl(url);
        return text;
    }

    public Text build(final String url, final String title, final String textString){
        final Text text = new Text();
        text.setUrl(url);
        text.setTitle(title);
        text.setText(textString);
        textTransformer.transform(text);
        return text;
    }

    public Text build(final InputStream input){
        final ContentHandler textHandler = new BodyContentHandler(Integer.MAX_VALUE);
        final Metadata metadata = new Metadata();
        try {
            final AutoDetectParser parser = new AutoDetectParser();
            parser.parse(input, textHandler, metadata);
            input.close();
        } catch (IOException | SAXException | TikaException e) {
            throw new RuntimeException("Parsing fichier", e);
        }

        final Text text = new Text();

        text.setIdentifier(metadata.get(TikaCoreProperties.IDENTIFIER));
        text.setTitle(metadata.get(TikaCoreProperties.TITLE));
        text.setCreator(metadata.get(TikaCoreProperties.CREATOR));
        text.setCreated(metadata.get(TikaCoreProperties.CREATED));
        text.setLanguage(metadata.get(TikaCoreProperties.LANGUAGE));
        text.setType(metadata.get(TikaCoreProperties.TYPE));
        text.setComments(metadata.get(TikaCoreProperties.COMMENTS));
        text.setUrl(text.getTitle());
        text.setText(textHandler.toString());

        textTransformer.transform(text);
        return text;
    }
}
