package time.tika;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import time.repo.bean.Text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ToPage {

    public Text from(final String filename) throws FileNotFoundException {
        return from(new FileInputStream(filename));
    }

    public Text from(final InputStream input){
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
        text.setText(textHandler.toString());

        return text;
    }
}
