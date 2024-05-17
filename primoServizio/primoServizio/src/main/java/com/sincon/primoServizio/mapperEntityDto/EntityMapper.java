package com.sincon.primoServizio.mapperEntityDto;

import com.sincon.primoServizio.dto.ComuneDto;
import com.sincon.primoServizio.dto.DizionarioDto;
import com.sincon.primoServizio.dto.OrganigrammaStrutturaDto;
import com.sincon.primoServizio.dto.UtenteDto;
import com.sincon.primoServizio.model.Comune;
import com.sincon.primoServizio.model.Dizionario;
import com.sincon.primoServizio.model.OrganigrammaStruttura;
import com.sincon.primoServizio.model.Utente;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

public class EntityMapper {

    @Mapper(componentModel = "spring")
    public interface UtenteMapper {
        UtenteMapper MAPPER = Mappers.getMapper(UtenteMapper.class);

        UtenteDto utenteToUtenteDto(Utente utente);
        Utente utenteDtoToUtente(UtenteDto utenteDto);
    }

    @Mapper(componentModel = "spring")
    public interface DizionarioMapper {
        DizionarioMapper MAPPER = Mappers.getMapper(DizionarioMapper.class);

        DizionarioDto dizionarioToDizionarioDto(Dizionario dizionario);
        Dizionario dizionarioDtoToDizionario(DizionarioDto dizionarioDto);
    }

    @Mapper(componentModel = "spring")
    public interface ComuneMapper {
        ComuneMapper MAPPER = Mappers.getMapper(ComuneMapper.class);

        ComuneDto comuneToComuneDto(Comune comune);
        Comune comuneDtoToComune(ComuneDto comuneDto);
    }

    @Mapper(componentModel = "spring")
    public interface OrganigrammaStrutturaMapper {
        OrganigrammaStrutturaMapper MAPPER = Mappers.getMapper(OrganigrammaStrutturaMapper.class);

        OrganigrammaStrutturaDto orgStrutturaToOrgStrutturaDto(OrganigrammaStruttura orgStruttura);
        OrganigrammaStruttura orgStrutturaDtoToOrgStruttura(OrganigrammaStrutturaDto orgStrutturaDto);
    }
}
