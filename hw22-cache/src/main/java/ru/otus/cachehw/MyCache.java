package ru.otus.cachehw;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V>, HwListener<K, V> {
    private final Map<K, V> weakCache = new WeakHashMap<>();
    private final List<HwListener<K, V>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        weakCache.put(key, value);
        notify(key, value, "put");
    }

    @Override
    public void remove(K key) {
        weakCache.remove(key);
        notify(key, null, "remove");
    }

    @Override
    public V get(K key) {
        V result = weakCache.get(key);
        notify(key, result, "get");
        return result;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }

    public void notify(K key, V value, String action) {
        listeners.forEach(listener -> listener.notify(key, value, action));
    }
}
