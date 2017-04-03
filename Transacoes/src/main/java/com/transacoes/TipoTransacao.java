package com.transacoes;

public enum TipoTransacao {
	SAQUE(0, "Saque"),
	DEPOSITO(1, "Depósito"),
	TRANSFERENCIA(2, "Transferência");
	
	private String descricao;
	private int index;

	TipoTransacao(int index, String descricao) {
		this.index = index;
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}