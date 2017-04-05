package com.transacoes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.transacoes.model.ContaModel;
import com.transacoes.repository.ContaRepository;

@Service
public class ContaService {
	@Autowired
	private ContaRepository contaRepository;

	public ContaModel consultar(Long id) {
		return contaRepository.findOne(id);
	}
}