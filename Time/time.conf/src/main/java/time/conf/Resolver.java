package time.conf;

import org.apache.commons.lang3.text.StrLookup;
import org.apache.commons.lang3.text.StrSubstitutor;

public class Resolver {

    final static StrSubstitutor substitutor = new StrSubstitutor(new StrLookup<Object>() {
        @Override
        public String lookup(String key) {
            final String value = System.getenv(key);
            if(value == null){
                throw new RuntimeException("variable d'environnement manquante : " + key);
            }
            return value;
        }
    });

    public static String get(final String str){
        return substitutor.replace(str);
    }

}
