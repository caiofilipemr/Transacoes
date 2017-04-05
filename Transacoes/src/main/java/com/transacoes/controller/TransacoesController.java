package com.transacoes.controller;

import java.text.DecimalFormat;
import java.util.Date;
import java.text.SimpleDateFormat;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.beans.propertyeditors.CustomDateEditor;

import com.transacoes.TipoTransacao;
import com.transacoes.model.ContaModel;
import com.transacoes.model.PessoaModel;
import com.transacoes.model.TransacaoModel;
import com.transacoes.service.TransacaoService.SaldoInsuficienteException;
import com.transacoes.service.ContaService;
import com.transacoes.service.PessoaService;
import com.transacoes.service.TransacaoService;

@Controller
public class TransacoesController {
	public static final String VIEW_HOME = "index";

	@Autowired
	private PessoaService pessoaService;

	@Autowired
	private ContaService contaService;

	@Autowired
	private TransacaoService transacaoService;

	@InitBinder
	private void dateBinder(WebDataBinder binder) {
	    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	    CustomDateEditor editor = new CustomDateEditor(dateFormat, true);
	    binder.registerCustomEditor(Date.class, editor);
	}

	private getObjetosView() {
		ModelAndView modelAndView = new ModelAndView(VIEW_HOME);
		modelAndView.addObject("tipos", TipoTransacao.values());
		Iterable<PessoaModel> pessoas = pessoaService.encontrarTodos();
		modelAndView.addObject("clientes", pessoas);
		modelAndView.addObject("saldo", pessoas.iterator().hasNext()
				? DecimalFormat.getInstance().format(pessoas.iterator().next().getConta().getSaldo())
				: "0");
		return modelAndView;
	}

	@GetMapping("/*")
	public ModelAndView home() {
		modelAndView.addObject("pessoaModel", new PessoaModel());
		return modelAndView;
	}

	@RequestMapping(value = "adicionarPessoa", method = RequestMethod.POST)
	public String adicionarPessoa(@Valid PessoaModel pessoaModel,
			BindingResult bindingResult) {
		System.out.println(bindingResult);
		if (bindingResult.hasErrors()) {
			modelAndView.addObject("pessoaModel", pessoaModel);
			return VIEW_HOME;
		}
		System.out.println(pessoaModel);

		pessoaService.incluir(pessoaModel);
		return "redirect:" + VIEW_HOME;
	}

	@RequestMapping(value = "realizarTransacao", method = RequestMethod.POST)
	public String realizarTransacao(@Valid TransacaoModel transacaoModel,
			BindingResult bindingResult) {
		System.out.println(transacaoModel);
		if (bindingResult.hasErrors()) {
			return VIEW_HOME;
		}

		try {
			transacaoService.realizarTransacao(transacaoModel);
		} catch (SaldoInsuficienteException e) {
			System.out.println(e.toString());
		}
		return "redirect:" + VIEW_HOME;
	}

	@RequestMapping(value = "atualizarSaldo", method = RequestMethod.POST,
			headers = {"Content-type=application/x-www-form-urlencoded"})
	@ResponseBody
	public String atualizarSaldo(ContaModel contaModel) {
		contaModel = contaService.consultar(contaModel.getId());
		return DecimalFormat.getInstance().format(contaModel.getSaldo());
	}
}
