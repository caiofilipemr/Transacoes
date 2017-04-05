package com.transacoes.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Digits;

@Entity
public class ContaModel {
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(precision=15, scale=2)
	@Digits(integer=15, fraction=2)
	private BigDecimal saldo;
	
	@Column(precision=15, scale=2)
	@Digits(integer=15, fraction=2)
	private BigDecimal limite;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public BigDecimal getSaldo() {
		return saldo;
	}
	
	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}
	
	public BigDecimal getLimite() {
		return limite;
	}
	
	public void setLimite(BigDecimal limite) {
		this.limite = limite;
	}

	@Override
	public String toString() {
		return "ContaModel [id=" + id + ", saldo=" + saldo + ", limite=" + limite + "]";
	}
}