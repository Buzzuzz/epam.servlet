package com.servlet.ejournal.pools;

import java.util.Set;

public interface Pool<T, K> {
    Set<T> getAllowedInstances(K instance);
}
