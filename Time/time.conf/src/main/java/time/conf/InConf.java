package time.conf;

/**
 * Les éléments participants à la conf.
 * On trie par la position avant éxecution de la liste des InConf.
 * @author slimane
 *
 */
public interface InConf {
	void appendTo(final Conf conf);
	int position();
}
