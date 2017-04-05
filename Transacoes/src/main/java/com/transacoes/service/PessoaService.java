package com.transacoes.service;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.transacoes.model.ContaModel;
import com.transacoes.model.PessoaModel;
import com.transacoes.repository.ContaRepository;
import com.transacoes.repository.PessoaRepository;

@Service
public class PessoaService {
	@Autowired
	private PessoaRepository pessoaRepository;

	public Iterable<PessoaModel> encontrarTodos() {
		return pessoaRepository.findAll();
	}

	public void incluir(PessoaModel pessoaModel) {
		ContaModel contaModel = new ContaModel();
		contaModel.setLimite(ContaRepository.LIMITE_PADRAO);
		contaModel.setSaldo(BigDecimal.ZERO);
		pessoaModel.setConta(contaModel);
		pessoaModel.setDataCadastro(new Date());
		pessoaRepository.save(pessoaModel);
	}
}