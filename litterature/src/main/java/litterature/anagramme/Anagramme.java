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
    private Dictionnaire dico;

    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException, IOException, URISyntaxException {

        final String prefix = "";
        final String mix = "julien";
        final String suffix = "";

        ForkJoinPool.commonPool().submit(() -> {
            System.out.println("mixing " + mix);
            final Anagramme anagramme = Anagramme.with(prefix, mix, suffix, new Dictionnaire("liste_francais.txt"));
            while(true){
                anagramme.move().keepIfNew().printIfNew();
            }
        }).get(30, TimeUnit.SECONDS);

    }

    public static Anagramme with(String prefix, String base, final String suffix, final Dictionnaire dictionnaire) {
        final Anagramme anagramme = new Anagramme();
        anagramme.prefix = prefix;
        anagramme.base = base.split("");
        anagramme.suffix = suffix;
        anagramme.initialBase = base;
        anagramme.dico = dictionnaire;
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
        if(dico.exist(e)) {
            lastWasInserted = history.add(e);
        }else{
            lastWasInserted = false;
        }
        return this;
    }

    private Anagramme() {
        random = new Random();
        last = 99999999;
        history = new TreeSet<>();
        lastWasInserted = false;
    }


    private int randomIndex() {
        int newInt;
        do{
            newInt = random.nextInt(base.length);
        }while(newInt == last);

        last = newInt;
        return newInt;
    }

    public Anagramme move(){
        return move(randomIndex(), randomIndex());
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
        if(builder.toString().equals(initialBase)){
            builder.append("   # # # # # # # # # # # # # # trouvé!!!!!!");
        }
        return builder.toString();
    }

}
