package time.tika;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import time.conf.Resolver;
import time.domain.Text;
import time.tool.string.Strings;
import time.tool.url.UrlTo;
import time.transform.ITextTransformer;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TextFactory {

    private final ITextTransformer textTransformer;
    private ObjectMapper mapper;

    @Inject
    public TextFactory(final ITextTransformer textTransformer){
        this.textTransformer = textTransformer;
        mapper = new ObjectMapper();
    }

    public Text build(final String filepath) throws FileNotFoundException {
        return build(new FileInputStream(new File(filepath)));
    }

    public Text buildFromUrl(final String url, final String storageDir) throws IOException {
        final byte[] pageBytes = UrlTo.byteArray(url);
        final String filename = Strings.forFilename(url);
        Files.write(Paths.get(storageDir, filename), pageBytes);

        final InputStream pageStream = new ByteArrayInputStream(pageBytes);
        final Text text = build(pageStream);
        text.getMetadata().setUrl(url);
        text.getMetadata().setFilename(filename);
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
            throw new RuntimeException("Parsing InputStream", e);
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

    public Text buildFromMetaPath(final String metaPath) throws IOException, InvocationTargetException, IllegalAccessException {
        final time.domain.Metadata metadata = mapper.readValue(new File(metaPath), time.domain.Metadata.class);
        FileInputStream input;
        final String filepath = Resolver.get(Strings.without(metaPath,  time.domain.Metadata.EXT));
        try {
            input = new FileInputStream(filepath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Erreur lecture de " + filepath, e);
        }
        final Text text = build(input);

        BeanUtils.copyProperties(text.getMetadata(), metadata);

        return text;
    }
}
