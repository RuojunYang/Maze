package ca.cmpt213.as4.model;

import java.util.Objects;

/**
 * https://stackoverflow.com/questions/5303539/didnt-java-once-have-a-pair-class/16089354
 * use self create Pair rather use Pair form javafx
 *
 * @param <K>
 * @param <V>
 */
public class Pair<K, V> {

    private final K key;
    private final V value;

    public static <K, V> Pair<K, V> createPair(K key, V value) {
        return new Pair<K, V>(key, value);
    }

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Pair)) {
            return false;
        }
        Pair<?, ?> p = (Pair<?, ?>) o;
        return Objects.equals(p.key, key) && Objects.equals(p.value, value);
    }
}