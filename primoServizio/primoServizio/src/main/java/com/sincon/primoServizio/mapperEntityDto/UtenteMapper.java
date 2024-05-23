package com.sincon.primoServizio.mapperEntityDto;

import com.sincon.primoServizio.dto.UtenteDto;
import com.sincon.primoServizio.model.Utente;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UtenteMapper {
    UtenteDto utenteToUtenteDto(Utente utente);
    Utente utenteDtoToUtente(UtenteDto utenteDto);
}
