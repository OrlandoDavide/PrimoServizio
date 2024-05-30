package com.sincon.primoServizio.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUtente is a Querydsl query type for Utente
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUtente extends EntityPathBase<Utente> {

    private static final long serialVersionUID = -1609210323L;

    public static final QUtente utente = new QUtente("utente");

    public final BooleanPath attivo = createBoolean("attivo");

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath password = createString("password");

    public QUtente(String variable) {
        super(Utente.class, forVariable(variable));
    }

    public QUtente(Path<? extends Utente> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUtente(PathMetadata metadata) {
        super(Utente.class, metadata);
    }

}

