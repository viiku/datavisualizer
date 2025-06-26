package com.viiku.datavisualizer.common.model.mapper;

/**
 * Interface for mapping between source and target types
 * Mapper class named {@link BaseMapper}
 *
 * @param <E> the source type
 * @param <T> the target type
 */

public interface BaseMapper<E, T> {

    /**
     * Maps to target type from a entity type
     */
    T mapToTarget(E entity);

    /**
     * Maps to single entity to a Source type
     */
    E mapToEntity(T target);
}