package com.fiexch.orderbook;

public class RejectOrder {
	
	private String rejectionCode;

	public RejectOrder(String rejectionCode) {
		this.rejectionCode = rejectionCode;
	}
	public String getRejectionCode() {
		return rejectionCode;
	}

	public void setRejectionCode(String rejectionCode) {
		this.rejectionCode = rejectionCode;
	}
}
