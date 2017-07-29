package litterature.anagramme;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class Anagramme {

    private String prefix;
    private String suffix;

    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException, IOException, URISyntaxException {

        final String prefix = "SL";
        final String mix = "IMANE I";
        final String suffix = "DA";

        ForkJoinPool.commonPool().submit(() -> {
            System.out.println("mixing " + mix);
            final Anagramme anagramme = Anagramme.with(prefix, mix, suffix);
            while(true){
                anagramme
                        .move()
                        .keepIfNew()
                        .printIfNew();
            }
        }).get(30, TimeUnit.SECONDS);

    }

    public static Anagramme with(String prefix, String base, final String suffix) {
        final Anagramme anagramme = new Anagramme();
        anagramme.prefix = prefix;
        anagramme.base = base.split("");
        anagramme.suffix = suffix;
        anagramme.initialBase = base;
        return anagramme;
    }

    private Set<String> history;
    private String[] base;
    private String initialBase;
    private Random random;
    private int last;
    private boolean lastWasInserted;

    private Anagramme printIfNew() {
        if(lastWasInserted){
            print();
        }
        return this;
    }

    private int mixCount() {
        return history.size();
    }

    private Anagramme keepIfNew() {
        final String e = get();
        lastWasInserted = history.add(e);
        return this;
    }

    private Anagramme() {
        random = new Random();
        last = 99999999;
        history = new TreeSet<>();
        lastWasInserted = false;
    }


    public Anagramme move(){
        return move(random.nextInt(base.length), random.nextInt(base.length));
    }

    public Anagramme move(int oldIndex, int newIndex){
        final String temp = base[newIndex];
        base[newIndex] = base[oldIndex];
        base[oldIndex] = temp;
        return this;
    }

    public Anagramme print(){
        System.out.println(history.size() + " - " + prefix + get() + suffix + "   ");
        return this;
    }

    public String get() {
        final StringBuilder builder = new StringBuilder();
        for(String s : base) {
            builder.append(s);
        }
        return builder.toString();
    }

    //void init(base)
    //String mix()
    //String getNew()(mix puis add jusqu'à nvle insertion, avec limite de temps)
    //inputText, onChange = init,
    //panel flexBox, oninit = empty()


}
