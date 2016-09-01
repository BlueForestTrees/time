package litterature.anagramme;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Dictionnaire {

    private final List<String> words;

    public Dictionnaire(final String path) throws IOException, URISyntaxException {
        final URL resource = this.getClass().getClassLoader().getResource(path);
        final URI uri = resource.toURI();
        final Path path1 = Paths.get(uri);
        words = Files.readAllLines(path1);
    }

    public boolean exist(final String value){
        return words.contains(value.toLowerCase());
    }
}
