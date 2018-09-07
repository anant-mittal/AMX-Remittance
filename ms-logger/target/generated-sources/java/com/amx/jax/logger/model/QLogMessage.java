package com.amx.jax.logger.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QLogMessage is a Querydsl query type for LogMessage
 */
@Generated("com.querydsl.codegen.EmbeddableSerializer")
public class QLogMessage extends BeanPath<LogMessage> {

    private static final long serialVersionUID = 2009606762L;

    public static final QLogMessage logMessage = new QLogMessage("logMessage");

    public final StringPath applicationName = createString("applicationName");

    public final StringPath id = createString("id");

    public final StringPath level = createString("level");

    public final StringPath loggerName = createString("loggerName");

    public final StringPath message = createString("message");

    public final NumberPath<Float> score = createNumber("score", Float.class);

    public final DateTimePath<java.util.Date> time = createDateTime("time", java.util.Date.class);

    public final ListPath<String, StringPath> traceback = this.<String, StringPath>createList("traceback", String.class, StringPath.class, PathInits.DIRECT2);

    public QLogMessage(String variable) {
        super(LogMessage.class, forVariable(variable));
    }

    public QLogMessage(Path<? extends LogMessage> path) {
        super(path.getType(), path.getMetadata());
    }

    public QLogMessage(PathMetadata metadata) {
        super(LogMessage.class, metadata);
    }

}

