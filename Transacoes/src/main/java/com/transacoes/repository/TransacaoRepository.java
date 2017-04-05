package com.transacoes.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TemporalType;

import com.transacoes.TipoTransacao;

import com.transacoes.model.TransacaoModel;
import com.transacoes.model.ContaModel;

@Repository
@Transactional
public interface TransacaoRepository extends CrudRepository<TransacaoModel, Long> {
	List<TransacaoModel> findByDataBetweenAndTipoAndContaOrigem(@Temporal(TemporalType.DATE) Date inicio,
			@Temporal(TemporalType.DATE) Date fim, TipoTransacao tipo, ContaModel contaOrigem);
}
