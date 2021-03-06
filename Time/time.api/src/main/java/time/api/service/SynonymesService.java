package time.api.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

@Component
public class SynonymesService {

    final RestTemplate t = new RestTemplate();
    final ResponseExtractor<List<String>> responseExtractor = new ResponseExtractor<List<String>>() {
        @Override
        public List<String> extractData(ClientHttpResponse httpResponse) throws IOException {
            final String response = StreamUtils.copyToString(httpResponse.getBody(), Charset.forName("UTF-8"));
            final List<String> synonyms = new ArrayList<>();
            int fromIndex = 0;
            int i;
            int i2;

            for (int nb = 0; nb <= 3; nb++) {
                i = response.indexOf("><a href=\"/des/synonymes", fromIndex);
                i2 = response.indexOf("\"", i + 15);
                synonyms.add(response.substring(i + 25, i2));
                fromIndex = i2;
            }
            synonyms.remove(0);
            return synonyms;
        }
    };
    
    public List<String> get(String term) throws URISyntaxException {
        final URI url = new URI("http://www.crisco.unicaen.fr/des/synonymes/" + term);
        return t.execute(url, HttpMethod.GET, null, responseExtractor);
    }
}
