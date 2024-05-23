package com.sincon.primoServizio.mapperEntityDto;

import com.sincon.primoServizio.dto.DizionarioDto;
import com.sincon.primoServizio.model.Dizionario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DizionarioMapper {

    DizionarioDto dizionarioToDizionarioDto(Dizionario dizionario);
    Dizionario dizionarioDtoToDizionario(DizionarioDto dizionarioDto);
}
