package com.amx.service_provider.repository.webservice;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.amx.service_provider.dbmodel.webservice.OwsTransferLog;

@Component
public interface OwsTransferLogRepository extends CrudRepository<OwsTransferLog, Serializable>
{
}
