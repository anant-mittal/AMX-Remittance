package org.sample.assignment.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QApplicationLog is a Querydsl query type for ApplicationLog
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QApplicationLog extends EntityPathBase<ApplicationLog> {

    private static final long serialVersionUID = -145454112L;

    public static final QApplicationLog applicationLog = new QApplicationLog("applicationLog");

    public final QLogMessage _super = new QLogMessage(this);

    public final EnumPath<ApplicationLogType> applicationLogType = createEnum("applicationLogType", ApplicationLogType.class);

    //inherited
    public final StringPath applicationName = _super.applicationName;

    //inherited
    public final StringPath id = _super.id;

    //inherited
    public final StringPath level = _super.level;

    //inherited
    public final StringPath loggerName = _super.loggerName;

    //inherited
    public final StringPath message = _super.message;

    public final StringPath moduleName = createString("moduleName");

    //inherited
    public final NumberPath<Float> score = _super.score;

    public final StringPath threadNo = createString("threadNo");

    //inherited
    public final DateTimePath<java.util.Date> time = _super.time;

    //inherited
    public final ListPath<String, StringPath> traceback = _super.traceback;

    public QApplicationLog(String variable) {
        super(ApplicationLog.class, forVariable(variable));
    }

    public QApplicationLog(Path<? extends ApplicationLog> path) {
        super(path.getType(), path.getMetadata());
    }

    public QApplicationLog(PathMetadata<?> metadata) {
        super(ApplicationLog.class, metadata);
    }

}

