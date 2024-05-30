package com.sincon.primoServizio.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QDizionario is a Querydsl query type for Dizionario
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDizionario extends EntityPathBase<Dizionario> {

    private static final long serialVersionUID = 179908382L;

    public static final QDizionario dizionario = new QDizionario("dizionario");

    public final BooleanPath attivo = createBoolean("attivo");

    public final StringPath categoria = createString("categoria");

    public final StringPath codifica = createString("codifica");

    public final StringPath customFlag = createString("customFlag");

    public final StringPath denominazione = createString("denominazione");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> malattia = createNumber("malattia", Integer.class);

    public final NumberPath<Integer> ordine = createNumber("ordine", Integer.class);

    public final NumberPath<Integer> parent = createNumber("parent", Integer.class);

    public QDizionario(String variable) {
        super(Dizionario.class, forVariable(variable));
    }

    public QDizionario(Path<? extends Dizionario> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDizionario(PathMetadata metadata) {
        super(Dizionario.class, metadata);
    }

}

