package com.transacoes.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TemporalType;

import com.transacoes.model.TransacaoModel;

@Repository
@Transactional
public interface TransacaoRepository extends CrudRepository<TransacaoModel, Long> {
	public static final BigDecimal TARIFA_TRANSF = new BigDecimal(1.75);
	public static final BigDecimal TARIFA_SAQUE = new BigDecimal(3.9);
	
	List<TransacaoModel> findByDataBetween(@Temporal(TemporalType.DATE) Date inicio, 
			@Temporal(TemporalType.DATE) Date fim);
	
	public class SaldoInsuficienteException extends Exception {
		private static final long serialVersionUID = 1L;
		private final TransacaoModel transacaoModel;

		public SaldoInsuficienteException(String msg) {
			super(msg);
			this.transacaoModel = null;
		}
		
		public SaldoInsuficienteException(String msg, TransacaoModel transacaoModel) {
			super(msg);
			this.transacaoModel = transacaoModel;
		}

		public TransacaoModel getTransacaoModel() {
			return transacaoModel;
		}
	}
}