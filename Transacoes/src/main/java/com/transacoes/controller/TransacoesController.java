package com.transacoes.controller;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
import com.transacoes.repository.ContaRepository;
import com.transacoes.repository.PessoaRepository;
import com.transacoes.repository.TransacaoRepository;
import com.transacoes.repository.TransacaoRepository.SaldoInsuficienteException;

@Controller
public class TransacoesController {
	public static final String VIEW_HOME = "index";

	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private ContaRepository contaRepository;

	@Autowired
	private TransacaoRepository transacaoRepository;

	@InitBinder
	private void dateBinder(WebDataBinder binder) {
	    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	    CustomDateEditor editor = new CustomDateEditor(dateFormat, true);
	    binder.registerCustomEditor(Date.class, editor);
	}

	@GetMapping("/*")
	public ModelAndView home() {
		ModelAndView modelAndView = new ModelAndView(VIEW_HOME);
		modelAndView.addObject("tipos", TipoTransacao.values());
		modelAndView.addObject("clientes", pessoaRepository.findAll());
		return modelAndView;
	}

	@RequestMapping(value = "adicionarPessoa", method = RequestMethod.POST)
	public String adicionarPessoa(@Valid PessoaModel pessoaModel,
			BindingResult bindingResult) {
		System.out.println(bindingResult);
		if (bindingResult.hasErrors()) {
			return "redirect:" + VIEW_HOME;
		}
		System.out.println(pessoaModel);
		ContaModel contaModel = new ContaModel();
		contaModel.setLimite(ContaRepository.LIMITE_PADRAO);
		contaModel.setSaldo(BigDecimal.ZERO);
		pessoaModel.setConta(contaModel);
		pessoaModel.setDataCadastro(new Date());
		pessoaRepository.save(pessoaModel);
		return "redirect:" + VIEW_HOME;
	}

	@RequestMapping(value = "realizarTransacao", method = RequestMethod.POST)
	public String realizarTransacao(@Valid TransacaoModel transacaoModel,
			BindingResult bindingResult) {
		System.out.println(transacaoModel);
		if (bindingResult.hasErrors()) {
			for (ObjectError e : bindingResult.getAllErrors()) {
				System.out.println(e);
			}
		}

		try {
			salvarTransacao(transacaoModel);
		} catch (SaldoInsuficienteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "redirect:" + VIEW_HOME;
	}

	@RequestMapping(value = "atualizarSaldo", method = RequestMethod.POST,
			headers = {"Content-type=application/x-www-form-urlencoded"})
	@ResponseBody
	public String atualizarSaldo(ContaModel contaModel) {
		contaModel = contaRepository.findOne(contaModel.getId());
		return DecimalFormat.getInstance().format(contaModel.getSaldo());
	}

	private void salvarTransacao(TransacaoModel transacaoModel) throws SaldoInsuficienteException {
		if (transacaoModel.getData() == null) {
			transacaoModel.setData(new Date());
		}

		BigDecimal saldo;
		if (TipoTransacao.TRANSFERENCIA.equals(transacaoModel.getTipo())) { // É uma transferência
			transacaoModel.setTarifa(TransacaoRepository.TARIFA_TRANSF);
			saldo = transacaoModel.getContaOrigem().getSaldo();
			saldo = saldo.subtract(transacaoModel.getValor()).subtract(transacaoModel.getTarifa());

			if (saldo.compareTo(transacaoModel.getContaOrigem().getLimite()) < 0) { // Saldo insuficiente
				throw new TransacaoRepository.SaldoInsuficienteException("Saldo insuficiente para realizar a transação!",
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
			}
		}
		transacaoRepository.save(transacaoModel);
	}

	private BigDecimal calcularTarifaSaque(TransacaoModel transacaoModel) {
		System.out.println("Calculando a tarfica do saque!");
		Date data = transacaoModel.getData();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(data);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		Date inicio = calendar.getTime();
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getMaximum(Calendar.DAY_OF_MONTH));
		Date fim = calendar.getTime();
		System.out.println("Inicio: " + inicio + " - Fim: " + fim);

		List<TransacaoModel> lista = transacaoRepository.findByDataBetween(inicio, fim);
		System.out.println(lista);
		return lista.size() > 3 ? TransacaoRepository.TARIFA_SAQUE : BigDecimal.ZERO;
	}
}
