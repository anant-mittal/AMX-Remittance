alter table PIPS_MSTR add (DERIVED_SELL_RATE number,DERIVED_RATE number,AVGRATE_FOR_DERIVED_RATE number);
 
alter table upload_pips add (DERIVED_SELL_RATE number,DERIVED_RATE number,AVGRATE_FOR_DERIVED_RATE number);

alter table PIPS_MSTR_LOG add (DERIVED_SELL_RATE number,DERIVED_RATE number,AVGRATE_FOR_DERIVED_RATE number);

alter table EX_PIPS_MASTER add (DERIVED_SELL_RATE number,DERIVED_RATE number,AVGRATE_FOR_DERIVED_RATE number);