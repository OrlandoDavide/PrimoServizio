package com.sincon.primoServizio.mapperEntityDto;

import com.sincon.primoServizio.dto.OrganigrammaStrutturaDto;
import com.sincon.primoServizio.model.OrganigrammaStruttura;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrgStrutturaMapper {
    OrganigrammaStrutturaDto orgStrutturaToOrgStrutturaDto(OrganigrammaStruttura orgStruttura);
    OrganigrammaStruttura orgStrutturaDtoToOrgStruttura(OrganigrammaStrutturaDto orgStrutturaDto);
}
