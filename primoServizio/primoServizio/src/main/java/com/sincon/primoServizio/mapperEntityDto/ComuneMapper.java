package com.sincon.primoServizio.mapperEntityDto;

import com.sincon.primoServizio.dto.ComuneDto;
import com.sincon.primoServizio.model.Comune;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ComuneMapper {
    ComuneMapper INSTANCE = Mappers.getMapper(ComuneMapper.class);

    ComuneDto comuneEntityToDto(Comune comune);
    Comune comuneDtoToEntity(ComuneDto comuneDto);
    void aggiornaComuneDto(ComuneDto comune, @MappingTarget ComuneDto comuneEsistente);
    void aggiornaComune(Comune comune, @MappingTarget Comune comuneEsistente);
}
