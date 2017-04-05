package com.transacoes.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Digits;

import com.transacoes.TipoTransacao;

@Entity
public class TransacaoModel {
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(precision=15, scale=2)
	@Digits(integer=15, fraction=2)
	private BigDecimal valor;
	
	@Column(precision=15, scale=2)
	@Digits(integer=15, fraction=2)
	private BigDecimal tarifa;
	
	@Enumerated(EnumType.STRING)
	private TipoTransacao tipo;
	
	@ManyToOne(cascade = {CascadeType.ALL})
	private ContaModel contaOrigem;
	
	@ManyToOne(cascade = {CascadeType.ALL})
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

	public BigDecimal getTarifa() {
		return tarifa;
	}

	public void setTarifa(BigDecimal tarifa) {
		this.tarifa = tarifa;
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
		return "TransacaoModel [id=" + id + ", valor=" + valor + ", tarifa=" + tarifa
				+ ", tipo=" + tipo + ", contaOrigem=" + contaOrigem 
				+ ", contaDestino=" + contaDestino + ", data=" + data + "]";
	}
}