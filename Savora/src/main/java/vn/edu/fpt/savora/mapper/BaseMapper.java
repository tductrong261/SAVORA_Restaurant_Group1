package vn.edu.fpt.savora.mapper;

import java.util.List;

/**
 * Base generic mapper interface for MapStruct mappers.
 *
 * @param <E> Entity type
 * @param <D> DTO Response type
 */
public interface BaseMapper<E, D> {

    D toResponse(E entity);

    List<D> toResponseList(List<E> entities);
}
