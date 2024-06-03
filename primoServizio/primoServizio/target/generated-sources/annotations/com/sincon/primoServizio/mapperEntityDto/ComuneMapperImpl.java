package com.sincon.primoServizio.mapperEntityDto;

import com.sincon.primoServizio.dto.ComuneDto;
import com.sincon.primoServizio.model.Comune;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-03T16:20:17+0200",
    comments = "version: 1.6.0.Beta1, compiler: javac, environment: Java 20.0.2.1 (Amazon.com Inc.)"
)
@Component
public class ComuneMapperImpl implements ComuneMapper {

    @Override
    public ComuneDto comuneEntityToDto(Comune comune) {
        if ( comune == null ) {
            return null;
        }

        ComuneDto comuneDto = new ComuneDto();

        comuneDto.setIstat( comune.getIstat() );
        comuneDto.setNome( comune.getNome() );
        comuneDto.setProvincia( comune.getProvincia() );
        comuneDto.setAsl( comune.getAsl() );
        comuneDto.setAttivo( comune.getAttivo() );

        return comuneDto;
    }

    @Override
    public Comune comuneDtoToEntity(ComuneDto comuneDto) {
        if ( comuneDto == null ) {
            return null;
        }

        Comune comune = new Comune();

        comune.setIstat( comuneDto.getIstat() );
        comune.setNome( comuneDto.getNome() );
        comune.setProvincia( comuneDto.getProvincia() );
        comune.setAsl( comuneDto.getAsl() );
        comune.setAttivo( comuneDto.getAttivo() );

        return comune;
    }

    @Override
    public void aggiornaComuneDto(ComuneDto comune, ComuneDto comuneEsistente) {
        if ( comune == null ) {
            return;
        }

        comuneEsistente.setIstat( comune.getIstat() );
        comuneEsistente.setNome( comune.getNome() );
        comuneEsistente.setProvincia( comune.getProvincia() );
        comuneEsistente.setAsl( comune.getAsl() );
        comuneEsistente.setAttivo( comune.getAttivo() );
    }

    @Override
    public void aggiornaComune(Comune comune, Comune comuneEsistente) {
        if ( comune == null ) {
            return;
        }

        comuneEsistente.setIstat( comune.getIstat() );
        comuneEsistente.setNome( comune.getNome() );
        comuneEsistente.setProvincia( comune.getProvincia() );
        comuneEsistente.setAsl( comune.getAsl() );
        comuneEsistente.setAttivo( comune.getAttivo() );
    }
}
