package com.servlet.ejournal.pools;

import java.util.Set;

public interface Pool<T, K> {
    public Set<T> getAllowedInstances(K instance);
}
