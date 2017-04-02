package com.transacoes.controller;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.transacoes.model.ContaModel;
import com.transacoes.model.PessoaModel;
import com.transacoes.repository.ContaRepository;
import com.transacoes.repository.PessoaRepository;

@Controller
public class TransacoesController {
	public static final String VIEW_HOME = "index";
	
	@Autowired
	private PessoaRepository pessoaRepository;

	@RequestMapping("/*")
	public String home() {
		return VIEW_HOME;
	}
	
	@RequestMapping("adicionarPessoa")
	public String adicionarPessoa(PessoaModel pessoaModel) {
		System.out.println(pessoaModel);
		ContaModel contaModel = new ContaModel();
		contaModel.setLimite(ContaRepository.LIMITE_PADRAO);
		contaModel.setSaldo(BigDecimal.ZERO);
		pessoaModel.setConta(contaModel);
		pessoaModel.setDataCadastro(new Date());
		pessoaRepository.save(pessoaModel);
		return VIEW_HOME;
	}
}