package org.sample.assignment.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QCustomerLog is a Querydsl query type for CustomerLog
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QCustomerLog extends EntityPathBase<CustomerLog> {

    private static final long serialVersionUID = 1176955386L;

    public static final QCustomerLog customerLog = new QCustomerLog("customerLog");

    public final QLogMessage _super = new QLogMessage(this);

    //inherited
    public final StringPath applicationName = _super.applicationName;

    public final StringPath currentPage = createString("currentPage");

    public final StringPath customerId = createString("customerId");

    public final EnumPath<CustomerLogType> customerLogType = createEnum("customerLogType", CustomerLogType.class);

    //inherited
    public final StringPath id = _super.id;

    //inherited
    public final StringPath level = _super.level;

    //inherited
    public final StringPath loggerName = _super.loggerName;

    //inherited
    public final StringPath message = _super.message;

    public final StringPath productId = createString("productId");

    //inherited
    public final NumberPath<Float> score = _super.score;

    public final StringPath searchTerm = createString("searchTerm");

    //inherited
    public final DateTimePath<java.util.Date> time = _super.time;

    //inherited
    public final ListPath<String, StringPath> traceback = _super.traceback;

    public QCustomerLog(String variable) {
        super(CustomerLog.class, forVariable(variable));
    }

    public QCustomerLog(Path<? extends CustomerLog> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCustomerLog(PathMetadata<?> metadata) {
        super(CustomerLog.class, metadata);
    }

}

