package com.amx.jax.dbmodel.bene;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;

import com.amx.jax.dbmodel.bene.BeneficaryAccount;
import com.querydsl.core.types.Path;


/**
 * QBeneficaryAccount is a Querydsl query type for BeneficaryAccount
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QBeneficaryAccount extends EntityPathBase<BeneficaryAccount> {

    private static final long serialVersionUID = 1607608726L;

    public static final QBeneficaryAccount beneficaryAccount = new QBeneficaryAccount("beneficaryAccount");

    public final StringPath aliasFirstName = createString("aliasFirstName");

    public final StringPath aliasFourthName = createString("aliasFourthName");

    public final StringPath aliasSecondName = createString("aliasSecondName");

    public final StringPath aliasThirdName = createString("aliasThirdName");

    public final StringPath bankAccountNumber = createString("bankAccountNumber");

    public final NumberPath<java.math.BigDecimal> bankAccountTypeId = createNumber("bankAccountTypeId", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> bankBranchCode = createNumber("bankBranchCode", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> bankBranchId = createNumber("bankBranchId", java.math.BigDecimal.class);

    public final StringPath bankCode = createString("bankCode");

    public final NumberPath<java.math.BigDecimal> bankId = createNumber("bankId", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> beneApplicationCountryId = createNumber("beneApplicationCountryId", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> beneficaryAccountSeqId = createNumber("beneficaryAccountSeqId", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> beneficaryCountryId = createNumber("beneficaryCountryId", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> beneficaryMasterId = createNumber("beneficaryMasterId", java.math.BigDecimal.class);

    public final StringPath createdBy = createString("createdBy");

    public final DateTimePath<java.util.Date> createdDate = createDateTime("createdDate", java.util.Date.class);

    public final NumberPath<java.math.BigDecimal> currencyId = createNumber("currencyId", java.math.BigDecimal.class);

    public final StringPath isActive = createString("isActive");

    public final DateTimePath<java.util.Date> lastEmosRemittance = createDateTime("lastEmosRemittance", java.util.Date.class);

    public final DateTimePath<java.util.Date> lastJavaRemittence = createDateTime("lastJavaRemittence", java.util.Date.class);

    public final StringPath modifiedBy = createString("modifiedBy");

    public final DateTimePath<java.util.Date> modifiedDate = createDateTime("modifiedDate", java.util.Date.class);

    public final StringPath recordStaus = createString("recordStaus");

    public final NumberPath<java.math.BigDecimal> serviceGroupId = createNumber("serviceGroupId", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> serviceProviderBranchId = createNumber("serviceProviderBranchId", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> serviceProviderId = createNumber("serviceProviderId", java.math.BigDecimal.class);

    public final StringPath swiftCode = createString("swiftCode");

    public QBeneficaryAccount(String variable) {
        super(BeneficaryAccount.class, forVariable(variable));
    }

    public QBeneficaryAccount(Path<? extends BeneficaryAccount> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBeneficaryAccount(PathMetadata metadata) {
        super(BeneficaryAccount.class, metadata);
    }

}

