package com.sincon.primoServizio.mapperEntityDto;

import com.sincon.primoServizio.dto.DizionarioDto;
import com.sincon.primoServizio.model.Dizionario;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-05-31T10:51:10+0200",
    comments = "version: 1.6.0.Beta1, compiler: javac, environment: Java 20.0.2.1 (Amazon.com Inc.)"
)
@Component
public class DizionarioMapperImpl implements DizionarioMapper {

    @Override
    public DizionarioDto dizionarioEntityToDto(Dizionario dizionario) {
        if ( dizionario == null ) {
            return null;
        }

        DizionarioDto dizionarioDto = new DizionarioDto();

        dizionarioDto.setId( dizionario.getId() );
        dizionarioDto.setDenominazione( dizionario.getDenominazione() );
        dizionarioDto.setCategoria( dizionario.getCategoria() );
        dizionarioDto.setCodifica( dizionario.getCodifica() );
        dizionarioDto.setCustomFlag( dizionario.getCustomFlag() );
        dizionarioDto.setAttivo( dizionario.isAttivo() );
        dizionarioDto.setMalattia( dizionario.getMalattia() );
        dizionarioDto.setParent( dizionario.getParent() );
        dizionarioDto.setOrdine( dizionario.getOrdine() );

        return dizionarioDto;
    }

    @Override
    public Dizionario dizionarioDtoToEntity(DizionarioDto dizionarioDto) {
        if ( dizionarioDto == null ) {
            return null;
        }

        Dizionario dizionario = new Dizionario();

        dizionario.setId( dizionarioDto.getId() );
        dizionario.setDenominazione( dizionarioDto.getDenominazione() );
        dizionario.setCategoria( dizionarioDto.getCategoria() );
        dizionario.setCodifica( dizionarioDto.getCodifica() );
        dizionario.setCustomFlag( dizionarioDto.getCustomFlag() );
        dizionario.setAttivo( dizionarioDto.isAttivo() );
        dizionario.setMalattia( dizionarioDto.getMalattia() );
        dizionario.setParent( dizionarioDto.getParent() );
        dizionario.setOrdine( dizionarioDto.getOrdine() );

        return dizionario;
    }
}
