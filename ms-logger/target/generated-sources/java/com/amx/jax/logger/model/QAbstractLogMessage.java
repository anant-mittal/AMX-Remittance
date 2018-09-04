package com.amx.jax.logger.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QAbstractLogMessage is a Querydsl query type for AbstractLogMessage
 */
@Generated("com.querydsl.codegen.EmbeddableSerializer")
public class QAbstractLogMessage extends BeanPath<AbstractLogMessage> {

    private static final long serialVersionUID = 977829164L;

    public static final QAbstractLogMessage abstractLogMessage = new QAbstractLogMessage("abstractLogMessage");

    public final com.amx.jax.logger.QAuditEvent _super = new com.amx.jax.logger.QAuditEvent(this);

    //inherited
    public final StringPath category = _super.category;

    //inherited
    public final StringPath component = _super.component;

    //inherited
    public final StringPath description = _super.description;

    public final StringPath id = createString("id");

    public final StringPath loggerName = createString("loggerName");

    public final StringPath message = createString("message");

    public final StringPath moduleName = createString("moduleName");

    public final NumberPath<Float> score = createNumber("score", Float.class);

    public final NumberPath<Long> timestamp = createNumber("timestamp", Long.class);

    public final SimplePath<com.amx.utils.EnumType> type = createSimple("type", com.amx.utils.EnumType.class);

    public QAbstractLogMessage(String variable) {
        super(AbstractLogMessage.class, forVariable(variable));
    }

    public QAbstractLogMessage(Path<? extends AbstractLogMessage> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAbstractLogMessage(PathMetadata metadata) {
        super(AbstractLogMessage.class, metadata);
    }

}

