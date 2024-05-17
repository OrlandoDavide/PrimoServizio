package com.sincon.primoServizio.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QOrganigrammaStruttura is a Querydsl query type for OrganigrammaStruttura
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrganigrammaStruttura extends EntityPathBase<OrganigrammaStruttura> {

    private static final long serialVersionUID = 1480341225L;

    public static final QOrganigrammaStruttura organigrammaStruttura = new QOrganigrammaStruttura("organigrammaStruttura");

    public final BooleanPath abilitato = createBoolean("abilitato");

    public final NumberPath<Integer> asl = createNumber("asl", Integer.class);

    public final NumberPath<Integer> categoria = createNumber("categoria", Integer.class);

    public final StringPath cfReferente = createString("cfReferente");

    public final StringPath codCap = createString("codCap");

    public final StringPath codice = createString("codice");

    public final NumberPath<Integer> codiceEdotto = createNumber("codiceEdotto", Integer.class);

    public final StringPath codiceHsp11 = createString("codiceHsp11");

    public final StringPath codiceHsp12 = createString("codiceHsp12");

    public final StringPath codiceNSIS = createString("codiceNSIS");

    public final StringPath codiceSpecialitaClinica = createString("codiceSpecialitaClinica");

    public final StringPath codiceSts11 = createString("codiceSts11");

    public final StringPath coditeTs = createString("coditeTs");

    public final StringPath codMinisteriale = createString("codMinisteriale");

    public final StringPath codProvinciale = createString("codProvinciale");

    public final StringPath cognomeReferente = createString("cognomeReferente");

    public final StringPath cognomeTitolare = createString("cognomeTitolare");

    public final StringPath comune = createString("comune");

    public final NumberPath<Integer> createdBy = createNumber("createdBy", Integer.class);

    public final DatePath<java.time.LocalDate> createdDate = createDate("createdDate", java.time.LocalDate.class);

    public final NumberPath<Integer> createdWith = createNumber("createdWith", Integer.class);

    public final DatePath<java.time.LocalDate> dataAllineamento = createDate("dataAllineamento", java.time.LocalDate.class);

    public final DatePath<java.time.LocalDate> dataApertura = createDate("dataApertura", java.time.LocalDate.class);

    public final DatePath<java.time.LocalDate> dataAttivazione = createDate("dataAttivazione", java.time.LocalDate.class);

    public final DatePath<java.time.LocalDate> dataChiusura = createDate("dataChiusura", java.time.LocalDate.class);

    public final NumberPath<Integer> deletedBy = createNumber("deletedBy", Integer.class);

    public final DatePath<java.time.LocalDate> deletedDate = createDate("deletedDate", java.time.LocalDate.class);

    public final NumberPath<Integer> deletedWith = createNumber("deletedWith", Integer.class);

    public final StringPath denominazione = createString("denominazione");

    public final NumberPath<Integer> distretto = createNumber("distretto", Integer.class);

    public final StringPath emailTitolare = createString("emailTitolare");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isAsl = createBoolean("isAsl");

    public final NumberPath<Integer> modifiedBy = createNumber("modifiedBy", Integer.class);

    public final DatePath<java.time.LocalDate> modifiedDate = createDate("modifiedDate", java.time.LocalDate.class);

    public final NumberPath<Integer> modifiedWith = createNumber("modifiedWith", Integer.class);

    public final StringPath nomeReferente = createString("nomeReferente");

    public final StringPath nomeTitolare = createString("nomeTitolare");

    public final NumberPath<Integer> parent = createNumber("parent", Integer.class);

    public final NumberPath<Integer> progCO = createNumber("progCO", Integer.class);

    public final NumberPath<Integer> progDistretto = createNumber("progDistretto", Integer.class);

    public final NumberPath<Integer> progReparto = createNumber("progReparto", Integer.class);

    public final NumberPath<Integer> progServizio = createNumber("progServizio", Integer.class);

    public final NumberPath<Integer> progStabilimento = createNumber("progStabilimento", Integer.class);

    public final NumberPath<Integer> pubblico = createNumber("pubblico", Integer.class);

    public final BooleanPath puntoPrelievo = createBoolean("puntoPrelievo");

    public final BooleanPath reteRegionale = createBoolean("reteRegionale");

    public final BooleanPath screening = createBoolean("screening");

    public final NumberPath<Integer> strutturaOriginale = createNumber("strutturaOriginale", Integer.class);

    public final StringPath telefonoTitolare = createString("telefonoTitolare");

    public final NumberPath<Integer> tipologia = createNumber("tipologia", Integer.class);

    public final NumberPath<Integer> tipologiaEdotto = createNumber("tipologiaEdotto", Integer.class);

    public final NumberPath<Integer> tipologiaGiuridica = createNumber("tipologiaGiuridica", Integer.class);

    public QOrganigrammaStruttura(String variable) {
        super(OrganigrammaStruttura.class, forVariable(variable));
    }

    public QOrganigrammaStruttura(Path<? extends OrganigrammaStruttura> path) {
        super(path.getType(), path.getMetadata());
    }

    public QOrganigrammaStruttura(PathMetadata metadata) {
        super(OrganigrammaStruttura.class, metadata);
    }

}

