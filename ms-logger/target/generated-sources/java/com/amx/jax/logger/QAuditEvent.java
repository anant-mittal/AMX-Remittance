package com.amx.jax.logger;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QAuditEvent is a Querydsl query type for AuditEvent
 */
@Generated("com.querydsl.codegen.EmbeddableSerializer")
public class QAuditEvent extends BeanPath<AuditEvent> {

    private static final long serialVersionUID = 749427553L;

    public static final QAuditEvent auditEvent = new QAuditEvent("auditEvent");

    public final StringPath category = createString("category");

    public final StringPath component = createString("component");

    public final StringPath description = createString("description");

    public final StringPath message = createString("message");

    public final NumberPath<Long> timestamp = createNumber("timestamp", Long.class);

    public final SimplePath<com.amx.utils.EnumType> type = createSimple("type", com.amx.utils.EnumType.class);

    public QAuditEvent(String variable) {
        super(AuditEvent.class, forVariable(variable));
    }

    public QAuditEvent(Path<? extends AuditEvent> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAuditEvent(PathMetadata metadata) {
        super(AuditEvent.class, metadata);
    }

}

