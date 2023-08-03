package cn.aulang.common.core.concurrent;

public interface ExpiredConcurrentHashMapListener<K, V> {

    default void onAdd(K key, V value) {
    }

    default void onRemoval(K key, V value) {
    }
}