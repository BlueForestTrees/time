package time.transformer.tool;

import java.io.IOException;

import time.repo.bean.Phrase;

public interface IStorage {
    void store(Phrase phrase) throws IOException;

    void end() throws IOException;

    void start() throws IOException;
}
