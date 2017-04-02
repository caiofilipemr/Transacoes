package com.transacoes.repository;

import java.math.BigDecimal;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.transacoes.model.ContaModel;

@Repository
@Transactional
public interface ContaRepository extends CrudRepository<ContaModel, Long> {
	public static final BigDecimal LIMITE_PADRAO = new BigDecimal(200);
}