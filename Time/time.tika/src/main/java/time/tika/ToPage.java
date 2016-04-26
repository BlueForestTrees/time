package time.tika;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import time.repo.bean.Page;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ToPage {

    public Page from(final String filename) throws FileNotFoundException {
        return from(new FileInputStream(filename));
    }

    public Page from(final InputStream input){
        final ContentHandler textHandler = new BodyContentHandler(Integer.MAX_VALUE);
        final Metadata metadata = new Metadata();
        try {
            final AutoDetectParser parser = new AutoDetectParser();
            parser.parse(input, textHandler, metadata);
            input.close();
        } catch (IOException | SAXException | TikaException e) {
            throw new RuntimeException("Parsing fichier", e);
        }

        final Page page = new Page();

        page.setIdentifier(metadata.get(TikaCoreProperties.IDENTIFIER));
        page.setTitle(metadata.get(TikaCoreProperties.TITLE));
        page.setCreator(metadata.get(TikaCoreProperties.CREATOR));
        page.setCreated(metadata.get(TikaCoreProperties.CREATED));
        page.setLanguage(metadata.get(TikaCoreProperties.LANGUAGE));
        page.setType(metadata.get(TikaCoreProperties.TYPE));
        page.setComments(metadata.get(TikaCoreProperties.COMMENTS));
        page.setText(textHandler.toString());

        return page;
    }
}
