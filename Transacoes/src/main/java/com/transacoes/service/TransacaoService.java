package com.transacoes.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.transacoes.TipoTransacao;
import com.transacoes.model.TransacaoModel;
import com.transacoes.repository.TransacaoRepository;

@Service
public class TransacaoService {
	public static final BigDecimal TARIFA_TRANSF = new BigDecimal(1.75);
	public static final BigDecimal TARIFA_SAQUE = new BigDecimal(3.9);
	
	@Autowired
	private TransacaoRepository transacaoRepository;

	public void realizarTransacao(TransacaoModel transacaoModel) throws SaldoInsuficienteException {
		if (transacaoModel.getData() == null) {
			transacaoModel.setData(new Date());
		}

		BigDecimal saldo;
		if (TipoTransacao.TRANSFERENCIA.equals(transacaoModel.getTipo())) { // É uma transferência
			transacaoModel.setTarifa(TransacaoService.TARIFA_TRANSF);
			saldo = transacaoModel.getContaOrigem().getSaldo();
			saldo = saldo.subtract(transacaoModel.getValor()).subtract(transacaoModel.getTarifa());

			if (saldo.compareTo(transacaoModel.getContaOrigem().getLimite().negate()) < 0) { // Saldo insuficiente
				throw new TransacaoService.SaldoInsuficienteException("Saldo insuficiente para realizar a transação!",
						transacaoModel);
			}
			transacaoModel.getContaOrigem().setSaldo(saldo);
			saldo = transacaoModel.getContaDestino().getSaldo();
			saldo = saldo.add(transacaoModel.getValor());
			transacaoModel.getContaDestino().setSaldo(saldo);
		} else {
			transacaoModel.setContaDestino(null);
			if (TipoTransacao.DEPOSITO.equals(transacaoModel.getTipo())) {
				transacaoModel.setTarifa(BigDecimal.ZERO);
				saldo = transacaoModel.getContaOrigem().getSaldo();
				saldo = saldo.add(transacaoModel.getValor());
				transacaoModel.getContaOrigem().setSaldo(saldo);
			} else if (TipoTransacao.SAQUE.equals(transacaoModel.getTipo())) {
				transacaoModel.setTarifa(calcularTarifaSaque(transacaoModel));
				saldo = transacaoModel.getContaOrigem().getSaldo();
				saldo = saldo.subtract(transacaoModel.getValor().add(transacaoModel.getTarifa()));

				if (saldo.compareTo(transacaoModel.getContaOrigem().getLimite().negate()) < 0) { // Saldo insuficiente
					throw new TransacaoService.SaldoInsuficienteException("Saldo insuficiente para realizar a transação!",
							transacaoModel);
				}

				transacaoModel.getContaOrigem().setSaldo(saldo);
			}
		}
		transacaoRepository.save(transacaoModel);
	}
	
	private BigDecimal calcularTarifaSaque(TransacaoModel transacaoModel) {
		Date data = transacaoModel.getData();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(data);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		Date inicio = calendar.getTime();
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getMaximum(Calendar.DAY_OF_MONTH));
		Date fim = calendar.getTime();
		System.out.println("Inicio: " + inicio + " - Fim: " + fim);

		List<TransacaoModel> lista = transacaoRepository.findByDataBetweenAndTipoAndContaOrigem(
				inicio, fim, TipoTransacao.SAQUE, transacaoModel.getContaOrigem());
		return lista.size() >= 3 ? TransacaoService.TARIFA_SAQUE : BigDecimal.ZERO;
	}
	
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