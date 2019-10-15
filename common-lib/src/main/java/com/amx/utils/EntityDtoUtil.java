package com.amx.utils;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;

import com.amx.jax.exception.ApiHttpExceptions.ApiHttpArgException;
import com.amx.jax.exception.ApiHttpExceptions.ApiStatusCodes;

public class EntityDtoUtil {

	public static <E, D> D entityToDto(E entity, D dto) {
		try {
			BeanUtils.copyProperties(dto, entity);
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new ApiHttpArgException(ApiStatusCodes.ENTITY_CONVERSION_EXCEPTION);
		}
		return dto;
	}

	public static <D, E> E dtoToEntity(D dto, E entity) {
		try {
			BeanUtils.copyProperties(entity, dto);
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new ApiHttpArgException(ApiStatusCodes.ENTITY_CONVERSION_EXCEPTION);
		}
		return entity;
	}
}
