package com._xibrahim.cards.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.List;

@Component
public class ApiMapper {

    private final ModelMapper modelMapper;

    public ApiMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public <S, T> T transformToDto(S data, Class<T> dto) {
        return modelMapper.map(data, (Type) dto);
    }

    public <S, T> T transformFromDto(S dto, Class<T> data) {
        return modelMapper.map(dto, data);
    }

    public <S, T> List<T> transformToDto(List<S> data, Class<T> dto) {
        return modelMapper.map(data, (Type) dto);
    }

    public <S, T> List<T> transformFromDto(List<S> dto, Class<T> data) {
        return modelMapper.map(dto, (Type) data);
    }
}
