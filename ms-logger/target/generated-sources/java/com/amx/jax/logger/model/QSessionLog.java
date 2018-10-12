package com.amx.jax.logger.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QSessionLog is a Querydsl query type for SessionLog
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSessionLog extends EntityPathBase<SessionLog> {

    private static final long serialVersionUID = 443630357L;

    public static final QSessionLog sessionLog = new QSessionLog("sessionLog");

    public final QAbstractLogMessage _super = new QAbstractLogMessage(this);

    //inherited
    public final StringPath category = _super.category;

    //inherited
    public final StringPath component = _super.component;

    //inherited
    public final StringPath description = _super.description;

    //inherited
    public final StringPath id = _super.id;

    //inherited
    public final StringPath loggerName = _super.loggerName;

    //inherited
    public final StringPath message = _super.message;

    //inherited
    public final StringPath moduleName = _super.moduleName;

    //inherited
    public final NumberPath<Float> score = _super.score;

    //inherited
    public final NumberPath<Long> timestamp = _super.timestamp;

    //inherited
    public final SimplePath<com.amx.utils.EnumType> type = _super.type;

    public final StringPath userId = createString("userId");

    public final StringPath userType = createString("userType");

    public QSessionLog(String variable) {
        super(SessionLog.class, forVariable(variable));
    }

    public QSessionLog(Path<? extends SessionLog> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSessionLog(PathMetadata metadata) {
        super(SessionLog.class, metadata);
    }

}

