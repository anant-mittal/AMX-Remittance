package com.amx.jax.complaince;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ComplainceRepository extends CrudRepository<CBK_Report, String> {

	@Query("select c from CBK_Report c where c.docNo=?1 AND c.docFyr=?2")
	public CBK_Report getCBKReportByDocNoAndDocFyr(BigDecimal docNo,BigDecimal docFyr);
	
	@Query("select c from CBK_Report c")
	public List<CBK_Report> getDocFyr();

}
