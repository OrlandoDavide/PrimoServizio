package com.sincon.primoServizio.mapperEntityDto;

import com.sincon.primoServizio.dto.UtenteDto;
import com.sincon.primoServizio.model.Utente;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UtenteMapper {
    UtenteMapper INSTANCE = Mappers.getMapper(UtenteMapper.class);

    UtenteDto utenteEntityToDto(Utente utente);
    Utente utenteDtoToEntity(UtenteDto utenteDto);

    List<UtenteDto> listaEntityToDto(List<Utente> ListaUtenti);
    List<Utente> listaUtentiToEntity(List<Utente> ListaUtenti);
}
