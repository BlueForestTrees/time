package time.web.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class IndexService {

    private static final Logger LOG = LoggerFactory.getLogger(IndexService.class);

    public String reIndex() {
        LOG.debug("reIndex...");

        LOG.debug("reIndex   done.");
        return "TODO reindex";
    }
}
