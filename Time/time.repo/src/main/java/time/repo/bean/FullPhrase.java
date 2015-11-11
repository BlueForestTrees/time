package time.repo.bean;

public class FullPhrase extends Phrase {

    public long getDateByTen() {
        return getDate() / 10L;
    }

    public long getDateByTen3() {
        return getDate() / 10000L;
    }

    public long getDateByTen6() {
        return getDate() / 10000000L;
    }

    public long getDateByTen9() {
        return getDate() / 10000000000L;
    }
    
}
