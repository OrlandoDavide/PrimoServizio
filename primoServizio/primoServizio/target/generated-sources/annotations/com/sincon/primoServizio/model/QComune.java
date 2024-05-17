package com.sincon.primoServizio.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QComune is a Querydsl query type for Comune
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QComune extends EntityPathBase<Comune> {

    private static final long serialVersionUID = -2128907777L;

    public static final QComune comune = new QComune("comune");

    public final StringPath asl = createString("asl");

    public final StringPath attivo = createString("attivo");

    public final StringPath denominazione = createString("denominazione");

    public final StringPath istat = createString("istat");

    public final StringPath provincia = createString("provincia");

    public QComune(String variable) {
        super(Comune.class, forVariable(variable));
    }

    public QComune(Path<? extends Comune> path) {
        super(path.getType(), path.getMetadata());
    }

    public QComune(PathMetadata metadata) {
        super(Comune.class, metadata);
    }

}

