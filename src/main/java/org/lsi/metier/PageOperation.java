package org.lsi.metier;

import java.io.Serializable;

import java.util.List;

import org.lsi.entities.Operation;

public class PageOperation implements Serializable {
	
	private List<Operation> operations;
	private int page;
	private int nombreOperations;
	private int totalOperations;
	private int totalpages;
	
	public List<Operation> getOperations() {
		return operations;
	}
	public void setOperations(java.util.List<Operation> operations) {
		this.operations = operations;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getNombreOperations() {
		return nombreOperations;
	}
	public void setNombreOperations(int nombreOperations) {
		this.nombreOperations = nombreOperations;
	}
	public int getTotalOperations() {
		return totalOperations;
	}
	public void setTotalOperations(int totalOperations) {
		this.totalOperations = totalOperations;
	}
	public int getTotalpages() {
		return totalpages;
	}
	public void setTotalpages(int totalpages) {
		this.totalpages = totalpages;
	}

}
