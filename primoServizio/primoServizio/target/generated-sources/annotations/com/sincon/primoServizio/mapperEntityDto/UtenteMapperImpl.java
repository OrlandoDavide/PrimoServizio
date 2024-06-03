package com.sincon.primoServizio.mapperEntityDto;

import com.sincon.primoServizio.dto.UtenteDto;
import com.sincon.primoServizio.model.Utente;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-03T16:20:17+0200",
    comments = "version: 1.6.0.Beta1, compiler: javac, environment: Java 20.0.2.1 (Amazon.com Inc.)"
)
@Component
public class UtenteMapperImpl implements UtenteMapper {

    @Override
    public UtenteDto utenteEntityToDto(Utente utente) {
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
    public Utente utenteDtoToEntity(UtenteDto utenteDto) {
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

    @Override
    public List<UtenteDto> listaEntityToDto(List<Utente> ListaUtenti) {
        if ( ListaUtenti == null ) {
            return null;
        }

        List<UtenteDto> list = new ArrayList<UtenteDto>( ListaUtenti.size() );
        for ( Utente utente : ListaUtenti ) {
            list.add( utenteEntityToDto( utente ) );
        }

        return list;
    }

    @Override
    public List<Utente> listaUtentiToEntity(List<Utente> ListaUtenti) {
        if ( ListaUtenti == null ) {
            return null;
        }

        List<Utente> list = new ArrayList<Utente>( ListaUtenti.size() );
        for ( Utente utente : ListaUtenti ) {
            list.add( utente );
        }

        return list;
    }

    @Override
    public void aggiornaUtenteDto(UtenteDto utente, UtenteDto utenteEsistente) {
        if ( utente == null ) {
            return;
        }

        utenteEsistente.setId( utente.getId() );
        utenteEsistente.setEmail( utente.getEmail() );
        utenteEsistente.setPassword( utente.getPassword() );
        utenteEsistente.setAttivo( utente.isAttivo() );
    }

    @Override
    public void aggiornaUtente(Utente utente, Utente utenteEsistente) {
        if ( utente == null ) {
            return;
        }

        utenteEsistente.setId( utente.getId() );
        utenteEsistente.setEmail( utente.getEmail() );
        utenteEsistente.setPassword( utente.getPassword() );
        utenteEsistente.setAttivo( utente.isAttivo() );
    }
}
