package com.sincon.primoServizio.mapperEntityDto;

import com.sincon.primoServizio.dto.DizionarioDto;
import com.sincon.primoServizio.model.Dizionario;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DizionarioMapper {
    DizionarioMapper INSTANCE = Mappers.getMapper(DizionarioMapper.class);

    DizionarioDto dizionarioEntityToDto(Dizionario dizionario);
    Dizionario dizionarioDtoToEntity(DizionarioDto dizionarioDto);
    void aggiornaDizionarioDto(DizionarioDto dizionarioDto, @MappingTarget DizionarioDto dizionarioEsistenteDto);
    void aggiornaDizionario(Dizionario dizionarioDto, @MappingTarget Dizionario dizionarioEsistenteDto);
}
