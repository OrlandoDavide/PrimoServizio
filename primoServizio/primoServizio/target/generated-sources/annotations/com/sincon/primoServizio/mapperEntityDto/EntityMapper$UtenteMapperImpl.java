package com.sincon.primoServizio.mapperEntityDto;

import com.sincon.primoServizio.dto.UtenteDto;
import com.sincon.primoServizio.model.Utente;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-05-22T16:17:19+0200",
    comments = "version: 1.6.0.Beta1, compiler: javac, environment: Java 20.0.2.1 (Amazon.com Inc.)"
)
@Component
public class EntityMapper$UtenteMapperImpl implements EntityMapper.UtenteMapper {

    @Override
    public UtenteDto utenteToUtenteDto(Utente utente) {
        if ( utente == null ) {
            return null;
        }

        UtenteDto utenteDto = new UtenteDto();

        if ( utente.getId() != null ) {
            utenteDto.setId( utente.getId() );
        }
        utenteDto.setEmail( utente.getEmail() );
        utenteDto.setPassword( utente.getPassword() );
        utenteDto.setAttivo( utente.isAttivo() );

        return utenteDto;
    }

    @Override
    public Utente utenteDtoToUtente(UtenteDto utenteDto) {
        if ( utenteDto == null ) {
            return null;
        }

        Utente utente = new Utente();

        utente.setId( utenteDto.getId() );
        utente.setEmail( utenteDto.getEmail() );
        utente.setPassword( utenteDto.getPassword() );
        utente.setAttivo( utenteDto.isAttivo() );

        return utente;
    }
}
