package com.sincon.primoServizio.mapperEntityDto;

import com.sincon.primoServizio.dto.ComuneDto;
import com.sincon.primoServizio.model.Comune;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ComuneMapper {
    ComuneDto comuneToComuneDto(Comune comune);
    Comune comuneDtoToComune(ComuneDto comuneDto);
}
