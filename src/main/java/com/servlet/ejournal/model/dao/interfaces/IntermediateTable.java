package com.servlet.ejournal.model.dao.interfaces;

import java.sql.Connection;
import java.util.Optional;

public interface IntermediateTable<T> {
    Optional<T> get(Connection con, long entityId, long secondId);
}
