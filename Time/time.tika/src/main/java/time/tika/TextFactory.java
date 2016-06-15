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
import java.util.Optional;

public class TextFactory {

    private final ITextTransformer textTransformer;
    private ObjectMapper mapper;

    @Inject
    public TextFactory(final ITextTransformer textTransformer){
        this.textTransformer = textTransformer;
        mapper = new ObjectMapper();
    }

    public Text fromFile(final File file) throws FileNotFoundException {
        final Text text = fromInputStream(new FileInputStream(file));
        text.getMetadata().setType(time.domain.Metadata.Type.FILE);
        return text;
    }

    public Text fromFilepath(final String filepath) throws FileNotFoundException {
        return fromFile(new File(filepath));
    }

    public Text fromUrl(final String url, final String storageDir) throws IOException {
        final byte[] pageBytes = UrlTo.byteArray(url);
        final String filename = Strings.forFilename(url);

        if(storageDir != null) {
            Files.write(Paths.get(storageDir, filename), pageBytes);
        }

        final InputStream pageStream = new ByteArrayInputStream(pageBytes);
        final Text text = fromInputStream(pageStream);
        text.getMetadata().setUrl(url);
        text.getMetadata().setFilename(filename);
        text.getMetadata().setType(time.domain.Metadata.Type.WEB_PAGE);
        return text;
    }

    public Text fromString(final String url, final String title, final String textString, time.domain.Metadata.Type type){
        final Text text = new Text();
        text.getMetadata().setUrl(url);
        text.getMetadata().setTitre(title);
        text.getMetadata().setType(type);
        text.setText(textString);
        textTransformer.transform(text);
        return text;
    }

    public Text fromInputStream(final InputStream input){
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
        metadata.setAuteur(tikaMetadata.get(TikaCoreProperties.CREATOR));
        metadata.setDate(tikaMetadata.get(TikaCoreProperties.CREATED));
        metadata.setComments(tikaMetadata.get(TikaCoreProperties.COMMENTS));
        metadata.setTitre(Optional.ofNullable(tikaMetadata.get(TikaCoreProperties.TITLE)).orElse("Sans-Titre"));
        metadata.setUrl(metadata.getTitre());
        text.setText(textHandler.toString());

        textTransformer.transform(text);
        return text;
    }

    public Text fromMetaPath(final String metaPath) throws IOException, InvocationTargetException, IllegalAccessException {
        return fromMetafile(new File(metaPath));
    }

    public Text fromMetafile(final File metafile) throws IOException, InvocationTargetException, IllegalAccessException {
        final time.domain.Metadata metadata = mapper.readValue(metafile, time.domain.Metadata.class);
        FileInputStream input;
        final String filepath = Resolver.get(Strings.without(metafile.getPath(),  time.domain.Metadata.EXT));
        try {
            input = new FileInputStream(filepath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Erreur lecture de " + filepath, e);
        }
        final Text text = fromInputStream(input);

        BeanUtils.copyProperties(text.getMetadata(), metadata);

        return text;
    }

    public Text fromFileMaybeMeta(File file) throws IllegalAccessException, IOException, InvocationTargetException {
        final File metafile = Paths.get(file.getAbsolutePath()+ time.domain.Metadata.EXT).toFile();
        if(metafile.exists()){
            return fromMetafile(metafile);
        }else {
            return fromFile(file);
        }
    }
}
