package com.amx.jax.complaince;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ComplainceRepository extends CrudRepository<jaxCbkReport, String> {

	@Query("select c from jaxCbkReport c where c.docNo=?1 AND c.docFyr=?2")
	public jaxCbkReport getCBKReportByDocNoAndDocFyr(BigDecimal docNo,BigDecimal docFyr);
	
	@Query("select c from jaxCbkReport c")
	public List<jaxCbkReport> getDocFyr();

}
