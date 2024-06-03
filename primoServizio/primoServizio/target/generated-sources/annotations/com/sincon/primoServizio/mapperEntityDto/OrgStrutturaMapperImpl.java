package com.sincon.primoServizio.mapperEntityDto;

import com.sincon.primoServizio.dto.ComuneDto;
import com.sincon.primoServizio.dto.DizionarioDto;
import com.sincon.primoServizio.dto.OrganigrammaStrutturaDto;
import com.sincon.primoServizio.model.Comune;
import com.sincon.primoServizio.model.Dizionario;
import com.sincon.primoServizio.model.OrganigrammaStruttura;
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
public class OrgStrutturaMapperImpl implements OrgStrutturaMapper {

    @Override
    public OrganigrammaStrutturaDto orgStrutturaEntityToDto(OrganigrammaStruttura orgStruttura) {
        if ( orgStruttura == null ) {
            return null;
        }

        OrganigrammaStrutturaDto organigrammaStrutturaDto = new OrganigrammaStrutturaDto();

        organigrammaStrutturaDto.setId( orgStruttura.getId() );
        organigrammaStrutturaDto.setDenominazione( orgStruttura.getDenominazione() );
        organigrammaStrutturaDto.setCodice( orgStruttura.getCodice() );
        organigrammaStrutturaDto.setAsl( orgStrutturaEntityToDto( orgStruttura.getAsl() ) );
        organigrammaStrutturaDto.setNomeTitolare( orgStruttura.getNomeTitolare() );
        organigrammaStrutturaDto.setCognomeTitolare( orgStruttura.getCognomeTitolare() );
        organigrammaStrutturaDto.setEmailTitolare( orgStruttura.getEmailTitolare() );
        organigrammaStrutturaDto.setTelefonoTitolare( orgStruttura.getTelefonoTitolare() );
        organigrammaStrutturaDto.setNomeReferente( orgStruttura.getNomeReferente() );
        organigrammaStrutturaDto.setCognomeReferente( orgStruttura.getCognomeReferente() );
        organigrammaStrutturaDto.setCfReferente( orgStruttura.getCfReferente() );
        organigrammaStrutturaDto.setTipologia( dizionarioToDizionarioDto( orgStruttura.getTipologia() ) );
        organigrammaStrutturaDto.setParent( orgStrutturaEntityToDto( orgStruttura.getParent() ) );
        organigrammaStrutturaDto.setStrutturaOriginale( orgStrutturaEntityToDto( orgStruttura.getStrutturaOriginale() ) );
        organigrammaStrutturaDto.setAbilitato( orgStruttura.isAbilitato() );
        organigrammaStrutturaDto.setPubblico( orgStruttura.getPubblico() );
        organigrammaStrutturaDto.setReteRegionale( orgStruttura.isReteRegionale() );
        organigrammaStrutturaDto.setCategoria( dizionarioToDizionarioDto( orgStruttura.getCategoria() ) );
        organigrammaStrutturaDto.setCodiceTs( orgStruttura.getCodiceTs() );
        organigrammaStrutturaDto.setCodCap( orgStruttura.getCodCap() );
        organigrammaStrutturaDto.setCodMinisteriale( orgStruttura.getCodMinisteriale() );
        organigrammaStrutturaDto.setCodProvinciale( orgStruttura.getCodProvinciale() );
        organigrammaStrutturaDto.setCodiceNSIS( orgStruttura.getCodiceNSIS() );
        organigrammaStrutturaDto.setCodiceSts11( orgStruttura.getCodiceSts11() );
        organigrammaStrutturaDto.setCodiceHsp11( orgStruttura.getCodiceHsp11() );
        organigrammaStrutturaDto.setCodiceHsp12( orgStruttura.getCodiceHsp12() );
        organigrammaStrutturaDto.setCodiceEdotto( orgStruttura.getCodiceEdotto() );
        organigrammaStrutturaDto.setComune( comuneToComuneDto( orgStruttura.getComune() ) );
        organigrammaStrutturaDto.setDataApertura( orgStruttura.getDataApertura() );
        organigrammaStrutturaDto.setDataChiusura( orgStruttura.getDataChiusura() );
        organigrammaStrutturaDto.setDataAttivazione( orgStruttura.getDataAttivazione() );
        organigrammaStrutturaDto.setTipologiaGiuridica( dizionarioToDizionarioDto( orgStruttura.getTipologiaGiuridica() ) );
        organigrammaStrutturaDto.setTipologiaEdotto( dizionarioToDizionarioDto( orgStruttura.getTipologiaEdotto() ) );
        organigrammaStrutturaDto.setDistretto( orgStrutturaEntityToDto( orgStruttura.getDistretto() ) );
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
    public List<OrganigrammaStrutturaDto> orgStrutturaEntityToDtoList(List<OrganigrammaStrutturaDto> entities) {
        if ( entities == null ) {
            return null;
        }

        List<OrganigrammaStrutturaDto> list = new ArrayList<OrganigrammaStrutturaDto>( entities.size() );
        for ( OrganigrammaStrutturaDto organigrammaStrutturaDto : entities ) {
            list.add( organigrammaStrutturaDto );
        }

        return list;
    }

    @Override
    public OrganigrammaStruttura orgStrutturaDtoToEntity(OrganigrammaStrutturaDto orgStrutturaDto) {
        if ( orgStrutturaDto == null ) {
            return null;
        }

        OrganigrammaStruttura organigrammaStruttura = new OrganigrammaStruttura();

        organigrammaStruttura.setId( orgStrutturaDto.getId() );
        organigrammaStruttura.setDenominazione( orgStrutturaDto.getDenominazione() );
        organigrammaStruttura.setCodice( orgStrutturaDto.getCodice() );
        organigrammaStruttura.setAsl( orgStrutturaDtoToEntity( orgStrutturaDto.getAsl() ) );
        organigrammaStruttura.setNomeTitolare( orgStrutturaDto.getNomeTitolare() );
        organigrammaStruttura.setCognomeTitolare( orgStrutturaDto.getCognomeTitolare() );
        organigrammaStruttura.setEmailTitolare( orgStrutturaDto.getEmailTitolare() );
        organigrammaStruttura.setTelefonoTitolare( orgStrutturaDto.getTelefonoTitolare() );
        organigrammaStruttura.setNomeReferente( orgStrutturaDto.getNomeReferente() );
        organigrammaStruttura.setCognomeReferente( orgStrutturaDto.getCognomeReferente() );
        organigrammaStruttura.setCfReferente( orgStrutturaDto.getCfReferente() );
        organigrammaStruttura.setTipologia( dizionarioDtoToDizionario( orgStrutturaDto.getTipologia() ) );
        organigrammaStruttura.setParent( orgStrutturaDtoToEntity( orgStrutturaDto.getParent() ) );
        organigrammaStruttura.setChildrens( organigrammaStrutturaDtoListToOrganigrammaStrutturaList( orgStrutturaDto.getChildrens() ) );
        organigrammaStruttura.setStrutturaOriginale( orgStrutturaDtoToEntity( orgStrutturaDto.getStrutturaOriginale() ) );
        organigrammaStruttura.setAbilitato( orgStrutturaDto.isAbilitato() );
        organigrammaStruttura.setPubblico( orgStrutturaDto.getPubblico() );
        organigrammaStruttura.setReteRegionale( orgStrutturaDto.isReteRegionale() );
        organigrammaStruttura.setCategoria( dizionarioDtoToDizionario( orgStrutturaDto.getCategoria() ) );
        organigrammaStruttura.setCodiceTs( orgStrutturaDto.getCodiceTs() );
        organigrammaStruttura.setCodCap( orgStrutturaDto.getCodCap() );
        organigrammaStruttura.setCodMinisteriale( orgStrutturaDto.getCodMinisteriale() );
        organigrammaStruttura.setCodProvinciale( orgStrutturaDto.getCodProvinciale() );
        organigrammaStruttura.setCodiceNSIS( orgStrutturaDto.getCodiceNSIS() );
        organigrammaStruttura.setCodiceSts11( orgStrutturaDto.getCodiceSts11() );
        organigrammaStruttura.setCodiceHsp11( orgStrutturaDto.getCodiceHsp11() );
        organigrammaStruttura.setCodiceHsp12( orgStrutturaDto.getCodiceHsp12() );
        organigrammaStruttura.setCodiceEdotto( orgStrutturaDto.getCodiceEdotto() );
        organigrammaStruttura.setComune( comuneDtoToComune( orgStrutturaDto.getComune() ) );
        organigrammaStruttura.setDataApertura( orgStrutturaDto.getDataApertura() );
        organigrammaStruttura.setDataChiusura( orgStrutturaDto.getDataChiusura() );
        organigrammaStruttura.setDataAttivazione( orgStrutturaDto.getDataAttivazione() );
        organigrammaStruttura.setTipologiaGiuridica( dizionarioDtoToDizionario( orgStrutturaDto.getTipologiaGiuridica() ) );
        organigrammaStruttura.setTipologiaEdotto( dizionarioDtoToDizionario( orgStrutturaDto.getTipologiaEdotto() ) );
        organigrammaStruttura.setDistretto( orgStrutturaDtoToEntity( orgStrutturaDto.getDistretto() ) );
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

    @Override
    public void aggiornaStrutturaDto(OrganigrammaStrutturaDto struttura, OrganigrammaStrutturaDto strutturaEsistente) {
        if ( struttura == null ) {
            return;
        }

        strutturaEsistente.setDenominazione( struttura.getDenominazione() );
        strutturaEsistente.setCodice( struttura.getCodice() );
        if ( struttura.getAsl() != null ) {
            if ( strutturaEsistente.getAsl() == null ) {
                strutturaEsistente.setAsl( new OrganigrammaStrutturaDto() );
            }
            aggiornaStrutturaDto( struttura.getAsl(), strutturaEsistente.getAsl() );
        }
        else {
            strutturaEsistente.setAsl( null );
        }
        strutturaEsistente.setNomeTitolare( struttura.getNomeTitolare() );
        strutturaEsistente.setCognomeTitolare( struttura.getCognomeTitolare() );
        strutturaEsistente.setEmailTitolare( struttura.getEmailTitolare() );
        strutturaEsistente.setTelefonoTitolare( struttura.getTelefonoTitolare() );
        strutturaEsistente.setNomeReferente( struttura.getNomeReferente() );
        strutturaEsistente.setCognomeReferente( struttura.getCognomeReferente() );
        strutturaEsistente.setCfReferente( struttura.getCfReferente() );
        strutturaEsistente.setTipologia( struttura.getTipologia() );
        if ( struttura.getParent() != null ) {
            if ( strutturaEsistente.getParent() == null ) {
                strutturaEsistente.setParent( new OrganigrammaStrutturaDto() );
            }
            aggiornaStrutturaDto( struttura.getParent(), strutturaEsistente.getParent() );
        }
        else {
            strutturaEsistente.setParent( null );
        }
        if ( strutturaEsistente.getChildrens() != null ) {
            List<OrganigrammaStrutturaDto> list = orgStrutturaEntityToDtoList( struttura.getChildrens() );
            if ( list != null ) {
                strutturaEsistente.getChildrens().clear();
                strutturaEsistente.getChildrens().addAll( list );
            }
            else {
                strutturaEsistente.setChildrens( null );
            }
        }
        else {
            List<OrganigrammaStrutturaDto> list = orgStrutturaEntityToDtoList( struttura.getChildrens() );
            if ( list != null ) {
                strutturaEsistente.setChildrens( list );
            }
        }
        if ( struttura.getStrutturaOriginale() != null ) {
            if ( strutturaEsistente.getStrutturaOriginale() == null ) {
                strutturaEsistente.setStrutturaOriginale( new OrganigrammaStrutturaDto() );
            }
            aggiornaStrutturaDto( struttura.getStrutturaOriginale(), strutturaEsistente.getStrutturaOriginale() );
        }
        else {
            strutturaEsistente.setStrutturaOriginale( null );
        }
        strutturaEsistente.setAbilitato( struttura.isAbilitato() );
        strutturaEsistente.setPubblico( struttura.getPubblico() );
        strutturaEsistente.setReteRegionale( struttura.isReteRegionale() );
        strutturaEsistente.setCategoria( struttura.getCategoria() );
        strutturaEsistente.setCodiceTs( struttura.getCodiceTs() );
        strutturaEsistente.setCodCap( struttura.getCodCap() );
        strutturaEsistente.setCodMinisteriale( struttura.getCodMinisteriale() );
        strutturaEsistente.setCodProvinciale( struttura.getCodProvinciale() );
        strutturaEsistente.setCodiceNSIS( struttura.getCodiceNSIS() );
        strutturaEsistente.setCodiceSts11( struttura.getCodiceSts11() );
        strutturaEsistente.setCodiceHsp11( struttura.getCodiceHsp11() );
        strutturaEsistente.setCodiceHsp12( struttura.getCodiceHsp12() );
        strutturaEsistente.setCodiceEdotto( struttura.getCodiceEdotto() );
        strutturaEsistente.setComune( struttura.getComune() );
        strutturaEsistente.setDataApertura( struttura.getDataApertura() );
        strutturaEsistente.setDataChiusura( struttura.getDataChiusura() );
        strutturaEsistente.setDataAttivazione( struttura.getDataAttivazione() );
        strutturaEsistente.setTipologiaGiuridica( struttura.getTipologiaGiuridica() );
        strutturaEsistente.setTipologiaEdotto( struttura.getTipologiaEdotto() );
        if ( struttura.getDistretto() != null ) {
            if ( strutturaEsistente.getDistretto() == null ) {
                strutturaEsistente.setDistretto( new OrganigrammaStrutturaDto() );
            }
            aggiornaStrutturaDto( struttura.getDistretto(), strutturaEsistente.getDistretto() );
        }
        else {
            strutturaEsistente.setDistretto( null );
        }
        strutturaEsistente.setProgDistretto( struttura.getProgDistretto() );
        strutturaEsistente.setProgServizio( struttura.getProgServizio() );
        strutturaEsistente.setProgStabilimento( struttura.getProgStabilimento() );
        strutturaEsistente.setProgReparto( struttura.getProgReparto() );
        strutturaEsistente.setProgCO( struttura.getProgCO() );
        strutturaEsistente.setCreatedBy( struttura.getCreatedBy() );
        strutturaEsistente.setCreatedWith( struttura.getCreatedWith() );
        strutturaEsistente.setCreatedDate( struttura.getCreatedDate() );
        strutturaEsistente.setModifiedBy( struttura.getModifiedBy() );
        strutturaEsistente.setModifiedWith( struttura.getModifiedWith() );
        strutturaEsistente.setModifiedDate( struttura.getModifiedDate() );
        strutturaEsistente.setDeletedBy( struttura.getDeletedBy() );
        strutturaEsistente.setDeletedWith( struttura.getDeletedWith() );
        strutturaEsistente.setDeletedDate( struttura.getDeletedDate() );
        strutturaEsistente.setCodiceSpecialitaClinica( struttura.getCodiceSpecialitaClinica() );
        strutturaEsistente.setDataAllineamento( struttura.getDataAllineamento() );
        strutturaEsistente.setScreening( struttura.isScreening() );
        strutturaEsistente.setPuntoPrelievo( struttura.isPuntoPrelievo() );

        handleChildrens( struttura, strutturaEsistente );
    }

    @Override
    public void aggiornaStruttura(OrganigrammaStruttura struttura, OrganigrammaStruttura strutturaEsistente) {
        if ( struttura == null ) {
            return;
        }

        strutturaEsistente.setDenominazione( struttura.getDenominazione() );
        strutturaEsistente.setCodice( struttura.getCodice() );
        if ( struttura.getAsl() != null ) {
            if ( strutturaEsistente.getAsl() == null ) {
                strutturaEsistente.setAsl( new OrganigrammaStruttura() );
            }
            aggiornaStruttura( struttura.getAsl(), strutturaEsistente.getAsl() );
        }
        else {
            strutturaEsistente.setAsl( null );
        }
        strutturaEsistente.setNomeTitolare( struttura.getNomeTitolare() );
        strutturaEsistente.setCognomeTitolare( struttura.getCognomeTitolare() );
        strutturaEsistente.setEmailTitolare( struttura.getEmailTitolare() );
        strutturaEsistente.setTelefonoTitolare( struttura.getTelefonoTitolare() );
        strutturaEsistente.setNomeReferente( struttura.getNomeReferente() );
        strutturaEsistente.setCognomeReferente( struttura.getCognomeReferente() );
        strutturaEsistente.setCfReferente( struttura.getCfReferente() );
        strutturaEsistente.setTipologia( struttura.getTipologia() );
        if ( struttura.getParent() != null ) {
            if ( strutturaEsistente.getParent() == null ) {
                strutturaEsistente.setParent( new OrganigrammaStruttura() );
            }
            aggiornaStruttura( struttura.getParent(), strutturaEsistente.getParent() );
        }
        else {
            strutturaEsistente.setParent( null );
        }
        if ( strutturaEsistente.getChildrens() != null ) {
            List<OrganigrammaStruttura> list = struttura.getChildrens();
            if ( list != null ) {
                strutturaEsistente.getChildrens().clear();
                strutturaEsistente.getChildrens().addAll( list );
            }
            else {
                strutturaEsistente.setChildrens( null );
            }
        }
        else {
            List<OrganigrammaStruttura> list = struttura.getChildrens();
            if ( list != null ) {
                strutturaEsistente.setChildrens( new ArrayList<OrganigrammaStruttura>( list ) );
            }
        }
        if ( struttura.getStrutturaOriginale() != null ) {
            if ( strutturaEsistente.getStrutturaOriginale() == null ) {
                strutturaEsistente.setStrutturaOriginale( new OrganigrammaStruttura() );
            }
            aggiornaStruttura( struttura.getStrutturaOriginale(), strutturaEsistente.getStrutturaOriginale() );
        }
        else {
            strutturaEsistente.setStrutturaOriginale( null );
        }
        strutturaEsistente.setAbilitato( struttura.isAbilitato() );
        strutturaEsistente.setPubblico( struttura.getPubblico() );
        strutturaEsistente.setReteRegionale( struttura.isReteRegionale() );
        strutturaEsistente.setCategoria( struttura.getCategoria() );
        strutturaEsistente.setCodiceTs( struttura.getCodiceTs() );
        strutturaEsistente.setCodCap( struttura.getCodCap() );
        strutturaEsistente.setCodMinisteriale( struttura.getCodMinisteriale() );
        strutturaEsistente.setCodProvinciale( struttura.getCodProvinciale() );
        strutturaEsistente.setCodiceNSIS( struttura.getCodiceNSIS() );
        strutturaEsistente.setCodiceSts11( struttura.getCodiceSts11() );
        strutturaEsistente.setCodiceHsp11( struttura.getCodiceHsp11() );
        strutturaEsistente.setCodiceHsp12( struttura.getCodiceHsp12() );
        strutturaEsistente.setCodiceEdotto( struttura.getCodiceEdotto() );
        strutturaEsistente.setComune( struttura.getComune() );
        strutturaEsistente.setDataApertura( struttura.getDataApertura() );
        strutturaEsistente.setDataChiusura( struttura.getDataChiusura() );
        strutturaEsistente.setDataAttivazione( struttura.getDataAttivazione() );
        strutturaEsistente.setTipologiaGiuridica( struttura.getTipologiaGiuridica() );
        strutturaEsistente.setTipologiaEdotto( struttura.getTipologiaEdotto() );
        if ( struttura.getDistretto() != null ) {
            if ( strutturaEsistente.getDistretto() == null ) {
                strutturaEsistente.setDistretto( new OrganigrammaStruttura() );
            }
            aggiornaStruttura( struttura.getDistretto(), strutturaEsistente.getDistretto() );
        }
        else {
            strutturaEsistente.setDistretto( null );
        }
        strutturaEsistente.setProgDistretto( struttura.getProgDistretto() );
        strutturaEsistente.setProgServizio( struttura.getProgServizio() );
        strutturaEsistente.setProgStabilimento( struttura.getProgStabilimento() );
        strutturaEsistente.setProgReparto( struttura.getProgReparto() );
        strutturaEsistente.setProgCO( struttura.getProgCO() );
        strutturaEsistente.setCreatedBy( struttura.getCreatedBy() );
        strutturaEsistente.setCreatedWith( struttura.getCreatedWith() );
        strutturaEsistente.setCreatedDate( struttura.getCreatedDate() );
        strutturaEsistente.setModifiedBy( struttura.getModifiedBy() );
        strutturaEsistente.setModifiedWith( struttura.getModifiedWith() );
        strutturaEsistente.setModifiedDate( struttura.getModifiedDate() );
        strutturaEsistente.setDeletedBy( struttura.getDeletedBy() );
        strutturaEsistente.setDeletedWith( struttura.getDeletedWith() );
        strutturaEsistente.setDeletedDate( struttura.getDeletedDate() );
        strutturaEsistente.setCodiceSpecialitaClinica( struttura.getCodiceSpecialitaClinica() );
        strutturaEsistente.setDataAllineamento( struttura.getDataAllineamento() );
        strutturaEsistente.setScreening( struttura.isScreening() );
        strutturaEsistente.setPuntoPrelievo( struttura.isPuntoPrelievo() );
    }

    protected DizionarioDto dizionarioToDizionarioDto(Dizionario dizionario) {
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

    protected ComuneDto comuneToComuneDto(Comune comune) {
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

    protected Dizionario dizionarioDtoToDizionario(DizionarioDto dizionarioDto) {
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

    protected List<OrganigrammaStruttura> organigrammaStrutturaDtoListToOrganigrammaStrutturaList(List<OrganigrammaStrutturaDto> list) {
        if ( list == null ) {
            return null;
        }

        List<OrganigrammaStruttura> list1 = new ArrayList<OrganigrammaStruttura>( list.size() );
        for ( OrganigrammaStrutturaDto organigrammaStrutturaDto : list ) {
            list1.add( orgStrutturaDtoToEntity( organigrammaStrutturaDto ) );
        }

        return list1;
    }

    protected Comune comuneDtoToComune(ComuneDto comuneDto) {
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
