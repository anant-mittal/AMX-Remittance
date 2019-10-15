package com.amx.jax.dbmodel.fx;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFxOrderTransactionModel is a Querydsl query type for FxOrderTransactionModel
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFxOrderTransactionModel extends EntityPathBase<FxOrderTransactionModel> {

    private static final long serialVersionUID = -174996054L;

    public static final QFxOrderTransactionModel fxOrderTransactionModel = new QFxOrderTransactionModel("fxOrderTransactionModel");

    public final StringPath branchDesc = createString("branchDesc");

    public final NumberPath<java.math.BigDecimal> collectionDocumentCode = createNumber("collectionDocumentCode", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> collectionDocumentFinYear = createNumber("collectionDocumentFinYear", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> collectionDocumentNo = createNumber("collectionDocumentNo", java.math.BigDecimal.class);

    public final StringPath createdDate = createString("createdDate");

    public final DateTimePath<java.util.Date> createdDateAlt = createDateTime("createdDateAlt", java.util.Date.class);

    public final StringPath currencyQuoteName = createString("currencyQuoteName");

    public final NumberPath<java.math.BigDecimal> customerId = createNumber("customerId", java.math.BigDecimal.class);

    public final StringPath customerName = createString("customerName");

    public final NumberPath<java.math.BigDecimal> customerReference = createNumber("customerReference", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> deliveryCharges = createNumber("deliveryCharges", java.math.BigDecimal.class);

    public final StringPath deliveryDate = createString("deliveryDate");

    public final NumberPath<java.math.BigDecimal> deliveryDetSeqId = createNumber("deliveryDetSeqId", java.math.BigDecimal.class);

    public final StringPath deliveryTime = createString("deliveryTime");

    public final NumberPath<java.math.BigDecimal> documentCode = createNumber("documentCode", java.math.BigDecimal.class);

    public final StringPath documentDate = createString("documentDate");

    public final NumberPath<java.math.BigDecimal> documentFinanceYear = createNumber("documentFinanceYear", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> documentNumber = createNumber("documentNumber", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> driverEmployeeId = createNumber("driverEmployeeId", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> employeeId = createNumber("employeeId", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> exchangeRate = createNumber("exchangeRate", java.math.BigDecimal.class);

    public final StringPath foreignCurrencyCode = createString("foreignCurrencyCode");

    public final NumberPath<java.math.BigDecimal> foreignTransactionAmount = createNumber("foreignTransactionAmount", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> idno = createNumber("idno", java.math.BigDecimal.class);

    public final StringPath inventoryId = createString("inventoryId");

    public final StringPath isActive = createString("isActive");

    public final NumberPath<java.math.BigDecimal> localCurrencyId = createNumber("localCurrencyId", java.math.BigDecimal.class);

    public final StringPath localCurrQuoteName = createString("localCurrQuoteName");

    public final NumberPath<java.math.BigDecimal> localTrnxAmount = createNumber("localTrnxAmount", java.math.BigDecimal.class);

    public final StringPath orderStatus = createString("orderStatus");

    public final StringPath orderStatusCode = createString("orderStatusCode");

    public final StringPath otpTokenCustomer = createString("otpTokenCustomer");

    public final StringPath otpTokenPrefix = createString("otpTokenPrefix");

    public final NumberPath<java.math.BigDecimal> pagDetSeqId = createNumber("pagDetSeqId", java.math.BigDecimal.class);

    public final StringPath purposeOfTrnx = createString("purposeOfTrnx");

    public final StringPath sourceOfIncomeDesc = createString("sourceOfIncomeDesc");

    public final NumberPath<java.math.BigDecimal> sourceOfIncomeId = createNumber("sourceOfIncomeId", java.math.BigDecimal.class);

    public final StringPath transactionReferenceNo = createString("transactionReferenceNo");

    public final StringPath transactionTypeDesc = createString("transactionTypeDesc");

    public final StringPath travelCountryName = createString("travelCountryName");

    public final StringPath travelDateRange = createString("travelDateRange");

    public QFxOrderTransactionModel(String variable) {
        super(FxOrderTransactionModel.class, forVariable(variable));
    }

    public QFxOrderTransactionModel(Path<? extends FxOrderTransactionModel> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFxOrderTransactionModel(PathMetadata metadata) {
        super(FxOrderTransactionModel.class, metadata);
    }

}

