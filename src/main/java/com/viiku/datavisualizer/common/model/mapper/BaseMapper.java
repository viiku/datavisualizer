package com.viiku.datavisualizer.common.model.mapper;

/**
 * Interface for mapping between source and target types
 * Mapper class named {@link BaseMapper}
 *
 * @param <S> the source type
 * @param <T> the target type
 */

public interface BaseMapper<S, T> {

    /**
     * Maps a single source to a target type
     */
    T mapToTarget(S source);

    /**
     * Maps to single Target to a Source type
     */
    S mapToSource(T target);
}