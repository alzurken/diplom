package ru.mipt.sign.core.exceptions;

import java.math.BigInteger;

public class NeuronNotFound extends Exception {
	private BigInteger id;
	
	public BigInteger getId() {
		return id;
	}

	public NeuronNotFound(BigInteger id) {
		this.id = id;
	}
}
