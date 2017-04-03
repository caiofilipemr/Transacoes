package com.transacoes.controller;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.transacoes.TipoTransacao;
import com.transacoes.model.ContaModel;
import com.transacoes.model.PessoaModel;
import com.transacoes.model.TransacaoModel;
import com.transacoes.repository.ContaRepository;
import com.transacoes.repository.PessoaRepository;

@Controller
public class TransacoesController {
	public static final String VIEW_HOME = "index";

	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private ContaRepository contaRepository;

	@GetMapping("/*")
	public ModelAndView home() {
		ModelAndView modelAndView = new ModelAndView(VIEW_HOME);
		modelAndView.addObject("tipos", TipoTransacao.values());
		modelAndView.addObject("clientes", pessoaRepository.findAll());
		return modelAndView;
	}

	@RequestMapping(value = "adicionarPessoa", method = RequestMethod.POST)
	public ModelAndView adicionarPessoa(PessoaModel pessoaModel) {
		System.out.println(pessoaModel);
		ContaModel contaModel = new ContaModel();
		contaModel.setLimite(ContaRepository.LIMITE_PADRAO);
		contaModel.setSaldo(BigDecimal.ZERO);
		pessoaModel.setConta(contaModel);
		pessoaModel.setDataCadastro(new Date());
		pessoaRepository.save(pessoaModel);
		return new ModelAndView(VIEW_HOME);
	}

	@RequestMapping(value = "realizarTransacao", method = RequestMethod.POST)
	public ModelAndView realizarTransacao(@Valid TransacaoModel transacaoModel,
			BindingResult bindingResult) {
		System.out.println(transacaoModel);
		if (bindingResult.hasErrors()) {
			for (ObjectError e : bindingResult.getAllErrors()) {
				System.out.println(e);
			}
		}
		return new ModelAndView(VIEW_HOME);
	}

	@RequestMapping(value = "atualizarSaldo", method = RequestMethod.POST,
			headers = {"Content-type=application/x-www-form-urlencoded"})
	@ResponseBody
	public String atualizarSaldo(ContaModel contaModel) {
		contaModel = contaRepository.findOne(contaModel.getId());
		return DecimalFormat.getInstance().format(contaModel.getSaldo());
	}
}
