package time.web.service;

import org.apache.lucene.search.FieldDoc;
import org.springframework.stereotype.Service;
import time.web.bean.Last;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by slimane on 20/01/17.
 */
@Service
public class CacheService {

    private Map<String, Object> cache = new ConcurrentHashMap<>();

    public Last pop(final String lastKey) {
        return lastKey != null ? (Last) cache.remove(lastKey) : null;
    }

    public void save(final String newLastKey, final FieldDoc lastScoreDoc, final int newLastIndex) {
        cache.put(newLastKey, new Last(lastScoreDoc, newLastIndex));
    }
}
