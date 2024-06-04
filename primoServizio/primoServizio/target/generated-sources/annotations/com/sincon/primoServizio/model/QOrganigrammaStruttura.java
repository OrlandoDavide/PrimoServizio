package com.sincon.primoServizio.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOrganigrammaStruttura is a Querydsl query type for OrganigrammaStruttura
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrganigrammaStruttura extends EntityPathBase<OrganigrammaStruttura> {

    private static final long serialVersionUID = 1480341225L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOrganigrammaStruttura organigrammaStruttura = new QOrganigrammaStruttura("organigrammaStruttura");

    public final BooleanPath abilitato = createBoolean("abilitato");

    public final QOrganigrammaStruttura asl;

    public final QDizionario categoria;

    public final StringPath cfReferente = createString("cfReferente");

    public final ListPath<OrganigrammaStruttura, QOrganigrammaStruttura> children = this.<OrganigrammaStruttura, QOrganigrammaStruttura>createList("children", OrganigrammaStruttura.class, QOrganigrammaStruttura.class, PathInits.DIRECT2);

    public final StringPath codCap = createString("codCap");

    public final StringPath codice = createString("codice");

    public final NumberPath<Integer> codiceEdotto = createNumber("codiceEdotto", Integer.class);

    public final StringPath codiceHsp11 = createString("codiceHsp11");

    public final StringPath codiceHsp12 = createString("codiceHsp12");

    public final StringPath codiceNSIS = createString("codiceNSIS");

    public final StringPath codiceSpecialitaClinica = createString("codiceSpecialitaClinica");

    public final StringPath codiceSts11 = createString("codiceSts11");

    public final StringPath codiceTs = createString("codiceTs");

    public final StringPath codMinisteriale = createString("codMinisteriale");

    public final StringPath codProvinciale = createString("codProvinciale");

    public final StringPath cognomeReferente = createString("cognomeReferente");

    public final StringPath cognomeTitolare = createString("cognomeTitolare");

    public final QComune comune;

    public final NumberPath<Long> createdBy = createNumber("createdBy", Long.class);

    public final DatePath<java.time.LocalDate> createdDate = createDate("createdDate", java.time.LocalDate.class);

    public final NumberPath<Integer> createdWith = createNumber("createdWith", Integer.class);

    public final DatePath<java.time.LocalDate> dataAllineamento = createDate("dataAllineamento", java.time.LocalDate.class);

    public final DatePath<java.time.LocalDate> dataApertura = createDate("dataApertura", java.time.LocalDate.class);

    public final DatePath<java.time.LocalDate> dataAttivazione = createDate("dataAttivazione", java.time.LocalDate.class);

    public final DatePath<java.time.LocalDate> dataChiusura = createDate("dataChiusura", java.time.LocalDate.class);

    public final NumberPath<Long> deletedBy = createNumber("deletedBy", Long.class);

    public final DatePath<java.time.LocalDate> deletedDate = createDate("deletedDate", java.time.LocalDate.class);

    public final NumberPath<Integer> deletedWith = createNumber("deletedWith", Integer.class);

    public final StringPath denominazione = createString("denominazione");

    public final QOrganigrammaStruttura distretto;

    public final StringPath emailTitolare = createString("emailTitolare");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isAsl = createBoolean("isAsl");

    public final NumberPath<Long> modifiedBy = createNumber("modifiedBy", Long.class);

    public final DatePath<java.time.LocalDate> modifiedDate = createDate("modifiedDate", java.time.LocalDate.class);

    public final NumberPath<Integer> modifiedWith = createNumber("modifiedWith", Integer.class);

    public final StringPath nomeReferente = createString("nomeReferente");

    public final StringPath nomeTitolare = createString("nomeTitolare");

    public final QOrganigrammaStruttura parent;

    public final NumberPath<Integer> progCO = createNumber("progCO", Integer.class);

    public final NumberPath<Integer> progDistretto = createNumber("progDistretto", Integer.class);

    public final NumberPath<Integer> progReparto = createNumber("progReparto", Integer.class);

    public final NumberPath<Integer> progServizio = createNumber("progServizio", Integer.class);

    public final NumberPath<Integer> progStabilimento = createNumber("progStabilimento", Integer.class);

    public final NumberPath<Integer> pubblico = createNumber("pubblico", Integer.class);

    public final BooleanPath puntoPrelievo = createBoolean("puntoPrelievo");

    public final BooleanPath reteRegionale = createBoolean("reteRegionale");

    public final BooleanPath screening = createBoolean("screening");

    public final QOrganigrammaStruttura strutturaOriginale;

    public final StringPath telefonoTitolare = createString("telefonoTitolare");

    public final QDizionario tipologia;

    public final QDizionario tipologiaEdotto;

    public final QDizionario tipologiaGiuridica;

    public QOrganigrammaStruttura(String variable) {
        this(OrganigrammaStruttura.class, forVariable(variable), INITS);
    }

    public QOrganigrammaStruttura(Path<? extends OrganigrammaStruttura> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOrganigrammaStruttura(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOrganigrammaStruttura(PathMetadata metadata, PathInits inits) {
        this(OrganigrammaStruttura.class, metadata, inits);
    }

    public QOrganigrammaStruttura(Class<? extends OrganigrammaStruttura> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.asl = inits.isInitialized("asl") ? new QOrganigrammaStruttura(forProperty("asl"), inits.get("asl")) : null;
        this.categoria = inits.isInitialized("categoria") ? new QDizionario(forProperty("categoria")) : null;
        this.comune = inits.isInitialized("comune") ? new QComune(forProperty("comune")) : null;
        this.distretto = inits.isInitialized("distretto") ? new QOrganigrammaStruttura(forProperty("distretto"), inits.get("distretto")) : null;
        this.parent = inits.isInitialized("parent") ? new QOrganigrammaStruttura(forProperty("parent"), inits.get("parent")) : null;
        this.strutturaOriginale = inits.isInitialized("strutturaOriginale") ? new QOrganigrammaStruttura(forProperty("strutturaOriginale"), inits.get("strutturaOriginale")) : null;
        this.tipologia = inits.isInitialized("tipologia") ? new QDizionario(forProperty("tipologia")) : null;
        this.tipologiaEdotto = inits.isInitialized("tipologiaEdotto") ? new QDizionario(forProperty("tipologiaEdotto")) : null;
        this.tipologiaGiuridica = inits.isInitialized("tipologiaGiuridica") ? new QDizionario(forProperty("tipologiaGiuridica")) : null;
    }

}

