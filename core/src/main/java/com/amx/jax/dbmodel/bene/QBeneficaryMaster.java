package com.amx.jax.dbmodel.bene;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QBeneficaryMaster is a Querydsl query type for BeneficaryMaster
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QBeneficaryMaster extends EntityPathBase<BeneficaryMaster> {

    private static final long serialVersionUID = 116947417L;

    public static final QBeneficaryMaster beneficaryMaster = new QBeneficaryMaster("beneficaryMaster");

    public final NumberPath<java.math.BigDecimal> age = createNumber("age", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> applicationCountryId = createNumber("applicationCountryId", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> beneficaryMasterSeqId = createNumber("beneficaryMasterSeqId", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> beneficaryStatus = createNumber("beneficaryStatus", java.math.BigDecimal.class);

    public final StringPath beneficaryStatusName = createString("beneficaryStatusName");

    public final StringPath buildingNo = createString("buildingNo");

    public final StringPath cityName = createString("cityName");

    public final StringPath createdBy = createString("createdBy");

    public final DateTimePath<java.util.Date> createdDate = createDateTime("createdDate", java.util.Date.class);

    public final DateTimePath<java.util.Date> dateOfBrith = createDateTime("dateOfBrith", java.util.Date.class);

    public final StringPath districtName = createString("districtName");

    public final StringPath fifthName = createString("fifthName");

    public final StringPath firstName = createString("firstName");

    public final StringPath flatNo = createString("flatNo");

    public final StringPath fourthName = createString("fourthName");

    public final NumberPath<java.math.BigDecimal> fsCityMaster = createNumber("fsCityMaster", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> fsCountryMaster = createNumber("fsCountryMaster", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> fsDistrictMaster = createNumber("fsDistrictMaster", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> fsStateMaster = createNumber("fsStateMaster", java.math.BigDecimal.class);

    public final StringPath isActive = createString("isActive");

    public final StringPath localFifthName = createString("localFifthName");

    public final StringPath localFirstName = createString("localFirstName");

    public final StringPath localFourthName = createString("localFourthName");

    public final StringPath localSecondName = createString("localSecondName");

    public final StringPath localThirdName = createString("localThirdName");

    public final StringPath modifiedBy = createString("modifiedBy");

    public final DateTimePath<java.util.Date> modifiedDate = createDateTime("modifiedDate", java.util.Date.class);

    public final StringPath nationality = createString("nationality");

    public final StringPath noOfRemittance = createString("noOfRemittance");

    public final StringPath occupation = createString("occupation");

    public final StringPath secondName = createString("secondName");

    public final StringPath stateName = createString("stateName");

    public final StringPath streetNo = createString("streetNo");

    public final StringPath thirdName = createString("thirdName");

    public final NumberPath<java.math.BigDecimal> yearOfBrith = createNumber("yearOfBrith", java.math.BigDecimal.class);

    public QBeneficaryMaster(String variable) {
        super(BeneficaryMaster.class, forVariable(variable));
    }

    public QBeneficaryMaster(Path<? extends BeneficaryMaster> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBeneficaryMaster(PathMetadata metadata) {
        super(BeneficaryMaster.class, metadata);
    }

}

