package com.sincon.primoServizio.mapperEntityDto;

import com.sincon.primoServizio.dto.OrganigrammaStrutturaDto;
import com.sincon.primoServizio.model.OrganigrammaStruttura;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrgStrutturaMapper {
    OrgStrutturaMapper INSTANCE = Mappers.getMapper(OrgStrutturaMapper.class);

    OrganigrammaStrutturaDto orgStrutturaEntityToDto(OrganigrammaStruttura orgStruttura);
    OrganigrammaStruttura orgStrutturaDtoToEntity(OrganigrammaStrutturaDto orgStrutturaDto);
}
