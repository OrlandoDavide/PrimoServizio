package com.sincon.primoServizio.mapperEntityDto;

import com.sincon.primoServizio.dto.ComuneDto;
import com.sincon.primoServizio.model.Comune;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-05-23T15:59:21+0200",
    comments = "version: 1.6.0.Beta1, compiler: javac, environment: Java 20.0.2.1 (Amazon.com Inc.)"
)
@Component
public class ComuneMapperImpl implements ComuneMapper {

    @Override
    public ComuneDto comuneToComuneDto(Comune comune) {
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
    public Comune comuneDtoToComune(ComuneDto comuneDto) {
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
}
