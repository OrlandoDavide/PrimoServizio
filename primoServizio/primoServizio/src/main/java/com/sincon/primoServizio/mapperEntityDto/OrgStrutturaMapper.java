package com.sincon.primoServizio.mapperEntityDto;

import com.sincon.primoServizio.dto.OrganigrammaStrutturaDto;
import com.sincon.primoServizio.model.OrganigrammaStruttura;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrgStrutturaMapper {
    OrgStrutturaMapper INSTANCE = Mappers.getMapper(OrgStrutturaMapper.class);

    @Mapping(target = "childrens", ignore = true)
    OrganigrammaStrutturaDto orgStrutturaEntityToDto(OrganigrammaStruttura orgStruttura);

    List<OrganigrammaStrutturaDto> orgStrutturaEntityToDtoList(List<OrganigrammaStrutturaDto> entities);

    @AfterMapping
    default void handleChildrens(OrganigrammaStrutturaDto entity, @MappingTarget OrganigrammaStrutturaDto dto) {
        if (entity.getChildrens() != null) {
            if(dto.getChildrens() != null) {
                dto.getChildrens().clear();
            }
            dto.setChildrens(orgStrutturaEntityToDtoList(entity.getChildrens()));
        }
    }

    OrganigrammaStruttura orgStrutturaDtoToEntity(OrganigrammaStrutturaDto orgStrutturaDto);

    @Mapping(target = "id", ignore = true)
    void aggiornaStrutturaDto(OrganigrammaStrutturaDto struttura, @MappingTarget OrganigrammaStrutturaDto strutturaEsistente);

    @Mapping(target = "id", ignore = true)
    void aggiornaStruttura(OrganigrammaStruttura struttura, @MappingTarget OrganigrammaStruttura strutturaEsistente);

}
