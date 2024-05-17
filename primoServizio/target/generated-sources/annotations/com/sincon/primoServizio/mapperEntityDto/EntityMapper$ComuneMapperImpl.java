package com.sincon.primoServizio.mapperEntityDto;

import com.sincon.primoServizio.dto.ComuneDto;
import com.sincon.primoServizio.model.Comune;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-05-17T12:20:11+0200",
    comments = "version: 1.6.0.Beta1, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
@Component
public class EntityMapper$ComuneMapperImpl implements EntityMapper.ComuneMapper {

    @Override
    public ComuneDto comuneToComuneDto(Comune comune) {
        if ( comune == null ) {
            return null;
        }

        ComuneDto comuneDto = new ComuneDto();

        comuneDto.setIstat( comune.getIstat() );
        comuneDto.setDenominazione( comune.getDenominazione() );
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
        comune.setDenominazione( comuneDto.getDenominazione() );
        comune.setProvincia( comuneDto.getProvincia() );
        comune.setAsl( comuneDto.getAsl() );
        comune.setAttivo( comuneDto.getAttivo() );

        return comune;
    }
}
