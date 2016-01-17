package time.web.service;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class IndexService {

    private static final Logger LOG = LogManager.getLogger(IndexService.class);

    public String reIndex() {
        LOG.debug("reIndex...");

        LOG.debug("reIndex   done.");
        return "TODO reindex";
    }
}
