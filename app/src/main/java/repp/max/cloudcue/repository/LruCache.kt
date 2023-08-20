package repp.max.cloudcue.repository

import repp.max.cloudcue.models.CityWeather

private const val capacity = 5

class LruCache<K, V> : LinkedHashMap<K, V>(capacity, 0.75f, true) {
    override fun removeEldestEntry(eldest: MutableMap.MutableEntry<K, V>?): Boolean {
        return size > capacity
    }
}