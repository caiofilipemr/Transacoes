package com.transacoes.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.transacoes.TipoTransacao;

@Entity
public class TransacaoModel {
	@Id
	@GeneratedValue
	private Long id;
	private BigDecimal valor;
	
	@Enumerated(EnumType.STRING)
	private TipoTransacao tipo;
	
	@ManyToOne
	private ContaModel contaOrigem;
	
	@ManyToOne
	private ContaModel contaDestino;
	
	@Temporal(TemporalType.DATE)
	private Date data;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	
	public TipoTransacao getTipo() {
		return tipo;
	}

	public void setTipo(TipoTransacao tipo) {
		this.tipo = tipo;
	}

	public ContaModel getContaOrigem() {
		return contaOrigem;
	}

	public void setContaOrigem(ContaModel contaOrigem) {
		this.contaOrigem = contaOrigem;
	}

	public ContaModel getContaDestino() {
		return contaDestino;
	}

	public void setContaDestino(ContaModel contaDestino) {
		this.contaDestino = contaDestino;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "TransacaoModel [id=" + id + ", valor=" + valor + ", tipo=" + tipo 
				+ ", contaOrigem=" + contaOrigem + ", contaDestino="
				+ contaDestino + ", data=" + data + "]";
	}
}