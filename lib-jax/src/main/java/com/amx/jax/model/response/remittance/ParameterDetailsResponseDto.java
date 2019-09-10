package com.amx.jax.model.response.remittance;

/**
 * @author rabil
 */
import java.util.ArrayList;
import java.util.List;

public class ParameterDetailsResponseDto {

List<ParameterDetailsDto> parameterDetailsDto = new ArrayList<>();

public List<ParameterDetailsDto> getParameterDetailsDto() {
	return parameterDetailsDto;
}

public void setParameterDetailsDto(List<ParameterDetailsDto> parameterDetailsDto) {
	this.parameterDetailsDto = parameterDetailsDto;
}
}
