package com.transacoes.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.transacoes.model.PessoaModel;

@Repository
@Transactional
public interface PessoaRepository extends CrudRepository<PessoaModel, Long> { }