package com.sincon.primoServizio.mapperEntityDto;

import com.sincon.primoServizio.dto.OrganigrammaStrutturaDto;
import com.sincon.primoServizio.model.OrganigrammaStruttura;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-05-17T12:20:12+0200",
    comments = "version: 1.6.0.Beta1, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
@Component
public class EntityMapper$OrganigrammaStrutturaMapperImpl implements EntityMapper.OrganigrammaStrutturaMapper {

    @Override
    public OrganigrammaStrutturaDto orgStrutturaToOrgStrutturaDto(OrganigrammaStruttura orgStruttura) {
        if ( orgStruttura == null ) {
            return null;
        }

        OrganigrammaStrutturaDto organigrammaStrutturaDto = new OrganigrammaStrutturaDto();

        organigrammaStrutturaDto.setId( orgStruttura.getId() );
        organigrammaStrutturaDto.setDenominazione( orgStruttura.getDenominazione() );
        organigrammaStrutturaDto.setCodice( orgStruttura.getCodice() );
        organigrammaStrutturaDto.setAsl( orgStruttura.getAsl() );
        organigrammaStrutturaDto.setNomeTitolare( orgStruttura.getNomeTitolare() );
        organigrammaStrutturaDto.setCognomeTitolare( orgStruttura.getCognomeTitolare() );
        organigrammaStrutturaDto.setEmailTitolare( orgStruttura.getEmailTitolare() );
        organigrammaStrutturaDto.setTelefonoTitolare( orgStruttura.getTelefonoTitolare() );
        organigrammaStrutturaDto.setNomeReferente( orgStruttura.getNomeReferente() );
        organigrammaStrutturaDto.setCognomeReferente( orgStruttura.getCognomeReferente() );
        organigrammaStrutturaDto.setCfReferente( orgStruttura.getCfReferente() );
        organigrammaStrutturaDto.setTipologia( orgStruttura.getTipologia() );
        organigrammaStrutturaDto.setParent( orgStruttura.getParent() );
        organigrammaStrutturaDto.setStrutturaOriginale( orgStruttura.getStrutturaOriginale() );
        organigrammaStrutturaDto.setAbilitato( orgStruttura.isAbilitato() );
        organigrammaStrutturaDto.setPubblico( orgStruttura.getPubblico() );
        organigrammaStrutturaDto.setReteRegionale( orgStruttura.isReteRegionale() );
        organigrammaStrutturaDto.setCategoria( orgStruttura.getCategoria() );
        organigrammaStrutturaDto.setCodCap( orgStruttura.getCodCap() );
        organigrammaStrutturaDto.setCodMinisteriale( orgStruttura.getCodMinisteriale() );
        organigrammaStrutturaDto.setCodiceNSIS( orgStruttura.getCodiceNSIS() );
        organigrammaStrutturaDto.setCodiceSts11( orgStruttura.getCodiceSts11() );
        organigrammaStrutturaDto.setCodiceHsp12( orgStruttura.getCodiceHsp12() );
        organigrammaStrutturaDto.setCodiceEdotto( orgStruttura.getCodiceEdotto() );
        organigrammaStrutturaDto.setComune( orgStruttura.getComune() );
        organigrammaStrutturaDto.setDataApertura( orgStruttura.getDataApertura() );
        organigrammaStrutturaDto.setDataChiusura( orgStruttura.getDataChiusura() );
        organigrammaStrutturaDto.setDataAttivazione( orgStruttura.getDataAttivazione() );
        organigrammaStrutturaDto.setTipologiaGiuridica( orgStruttura.getTipologiaGiuridica() );
        organigrammaStrutturaDto.setTipologiaEdotto( orgStruttura.getTipologiaEdotto() );
        organigrammaStrutturaDto.setDistretto( orgStruttura.getDistretto() );
        organigrammaStrutturaDto.setProgDistretto( orgStruttura.getProgDistretto() );
        organigrammaStrutturaDto.setProgServizio( orgStruttura.getProgServizio() );
        organigrammaStrutturaDto.setProgStabilimento( orgStruttura.getProgStabilimento() );
        organigrammaStrutturaDto.setProgReparto( orgStruttura.getProgReparto() );
        organigrammaStrutturaDto.setProgCO( orgStruttura.getProgCO() );
        organigrammaStrutturaDto.setCreatedBy( orgStruttura.getCreatedBy() );
        organigrammaStrutturaDto.setCreatedWith( orgStruttura.getCreatedWith() );
        organigrammaStrutturaDto.setCreatedDate( orgStruttura.getCreatedDate() );
        organigrammaStrutturaDto.setModifiedBy( orgStruttura.getModifiedBy() );
        organigrammaStrutturaDto.setModifiedWith( orgStruttura.getModifiedWith() );
        organigrammaStrutturaDto.setModifiedDate( orgStruttura.getModifiedDate() );
        organigrammaStrutturaDto.setDeletedBy( orgStruttura.getDeletedBy() );
        organigrammaStrutturaDto.setDeletedWith( orgStruttura.getDeletedWith() );
        organigrammaStrutturaDto.setDeletedDate( orgStruttura.getDeletedDate() );
        organigrammaStrutturaDto.setCodiceSpecialitaClinica( orgStruttura.getCodiceSpecialitaClinica() );
        organigrammaStrutturaDto.setDataAllineamento( orgStruttura.getDataAllineamento() );
        organigrammaStrutturaDto.setScreening( orgStruttura.isScreening() );
        organigrammaStrutturaDto.setPuntoPrelievo( orgStruttura.isPuntoPrelievo() );

        return organigrammaStrutturaDto;
    }

    @Override
    public OrganigrammaStruttura orgStrutturaDtoToOrgStruttura(OrganigrammaStrutturaDto orgStrutturaDto) {
        if ( orgStrutturaDto == null ) {
            return null;
        }

        OrganigrammaStruttura organigrammaStruttura = new OrganigrammaStruttura();

        organigrammaStruttura.setId( orgStrutturaDto.getId() );
        organigrammaStruttura.setDenominazione( orgStrutturaDto.getDenominazione() );
        organigrammaStruttura.setCodice( orgStrutturaDto.getCodice() );
        organigrammaStruttura.setAsl( orgStrutturaDto.getAsl() );
        organigrammaStruttura.setNomeTitolare( orgStrutturaDto.getNomeTitolare() );
        organigrammaStruttura.setCognomeTitolare( orgStrutturaDto.getCognomeTitolare() );
        organigrammaStruttura.setEmailTitolare( orgStrutturaDto.getEmailTitolare() );
        organigrammaStruttura.setTelefonoTitolare( orgStrutturaDto.getTelefonoTitolare() );
        organigrammaStruttura.setNomeReferente( orgStrutturaDto.getNomeReferente() );
        organigrammaStruttura.setCognomeReferente( orgStrutturaDto.getCognomeReferente() );
        organigrammaStruttura.setCfReferente( orgStrutturaDto.getCfReferente() );
        organigrammaStruttura.setTipologia( orgStrutturaDto.getTipologia() );
        organigrammaStruttura.setParent( orgStrutturaDto.getParent() );
        organigrammaStruttura.setStrutturaOriginale( orgStrutturaDto.getStrutturaOriginale() );
        organigrammaStruttura.setAbilitato( orgStrutturaDto.isAbilitato() );
        organigrammaStruttura.setPubblico( orgStrutturaDto.getPubblico() );
        organigrammaStruttura.setReteRegionale( orgStrutturaDto.isReteRegionale() );
        organigrammaStruttura.setCategoria( orgStrutturaDto.getCategoria() );
        organigrammaStruttura.setCodCap( orgStrutturaDto.getCodCap() );
        organigrammaStruttura.setCodMinisteriale( orgStrutturaDto.getCodMinisteriale() );
        organigrammaStruttura.setCodiceNSIS( orgStrutturaDto.getCodiceNSIS() );
        organigrammaStruttura.setCodiceSts11( orgStrutturaDto.getCodiceSts11() );
        organigrammaStruttura.setCodiceHsp12( orgStrutturaDto.getCodiceHsp12() );
        organigrammaStruttura.setCodiceEdotto( orgStrutturaDto.getCodiceEdotto() );
        organigrammaStruttura.setComune( orgStrutturaDto.getComune() );
        organigrammaStruttura.setDataApertura( orgStrutturaDto.getDataApertura() );
        organigrammaStruttura.setDataChiusura( orgStrutturaDto.getDataChiusura() );
        organigrammaStruttura.setDataAttivazione( orgStrutturaDto.getDataAttivazione() );
        organigrammaStruttura.setTipologiaGiuridica( orgStrutturaDto.getTipologiaGiuridica() );
        organigrammaStruttura.setTipologiaEdotto( orgStrutturaDto.getTipologiaEdotto() );
        organigrammaStruttura.setDistretto( orgStrutturaDto.getDistretto() );
        organigrammaStruttura.setProgDistretto( orgStrutturaDto.getProgDistretto() );
        organigrammaStruttura.setProgServizio( orgStrutturaDto.getProgServizio() );
        organigrammaStruttura.setProgStabilimento( orgStrutturaDto.getProgStabilimento() );
        organigrammaStruttura.setProgReparto( orgStrutturaDto.getProgReparto() );
        organigrammaStruttura.setProgCO( orgStrutturaDto.getProgCO() );
        organigrammaStruttura.setCreatedBy( orgStrutturaDto.getCreatedBy() );
        organigrammaStruttura.setCreatedWith( orgStrutturaDto.getCreatedWith() );
        organigrammaStruttura.setCreatedDate( orgStrutturaDto.getCreatedDate() );
        organigrammaStruttura.setModifiedBy( orgStrutturaDto.getModifiedBy() );
        organigrammaStruttura.setModifiedWith( orgStrutturaDto.getModifiedWith() );
        organigrammaStruttura.setModifiedDate( orgStrutturaDto.getModifiedDate() );
        organigrammaStruttura.setDeletedBy( orgStrutturaDto.getDeletedBy() );
        organigrammaStruttura.setDeletedWith( orgStrutturaDto.getDeletedWith() );
        organigrammaStruttura.setDeletedDate( orgStrutturaDto.getDeletedDate() );
        organigrammaStruttura.setCodiceSpecialitaClinica( orgStrutturaDto.getCodiceSpecialitaClinica() );
        organigrammaStruttura.setDataAllineamento( orgStrutturaDto.getDataAllineamento() );
        organigrammaStruttura.setScreening( orgStrutturaDto.isScreening() );
        organigrammaStruttura.setPuntoPrelievo( orgStrutturaDto.isPuntoPrelievo() );

        return organigrammaStruttura;
    }
}
