package ru.otus.cachehw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {
    private static final Logger log = LoggerFactory.getLogger(MyCache.class);

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
        listeners.forEach(listener -> {
            try {
                listener.notify(key, value, action);
            } catch (Exception e) {
                log.error("Error while calling method listener.notify({}, {}, {}): {}", key, value, action, e.getMessage());
            }
        });
    }
}
