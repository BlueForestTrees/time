package time.conf;

import org.apache.commons.lang3.text.StrLookup;
import org.apache.commons.lang3.text.StrSubstitutor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.lang.System.getenv;
import static java.util.Optional.ofNullable;

public class Resolver {

    final static Map<String, String> hardCodedEnv = new HashMap<>();

    final static StrSubstitutor substitutor = new StrSubstitutor(new StrLookup<Object>() {
        @Override
        public String lookup(String key) {
            return ofNullable(getenv(key))
                    .orElseThrow(() -> new RuntimeException("variable d'environnement manquante : " + key));
        }
    });

    public static String get(final String str) {
        return substitutor.replace(str);
    }

}
