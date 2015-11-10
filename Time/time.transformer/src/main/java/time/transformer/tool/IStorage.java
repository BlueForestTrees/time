package time.transformer.tool;

import java.io.IOException;

import time.repo.bean.FullPhrase;

public interface IStorage {
    void store(FullPhrase phrase) throws IOException;

    void end() throws IOException;

    void start() throws IOException;
}
