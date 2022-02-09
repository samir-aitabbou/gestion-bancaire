package org.lsi.metier;

import java.util.Date;

import org.lsi.dao.CompteRepository;
import org.lsi.dao.EmployeRepository;
import org.lsi.dao.OperationRepository;
import org.lsi.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OperationMetierImpl implements OperationMetier {
	
	@Autowired
	private OperationRepository operationRepository;
	@Autowired
	private CompteRepository compteRepository;
	@Autowired
	private EmployeRepository employeRepository;

	@Override
	@Transactional
	public boolean verser(String code, double montant, Long codeEmp) {
		
		//Compte cp=compteRepository.findOne(code);  // donne un pb
		Compte cp=compteRepository.findById(code).orElse(null);
		Employe e= employeRepository.findById(codeEmp).orElse(null);
		Operation o = new Versment();
		o.setDateOperation(new Date());
		o.setMontant(montant);
		o.setCompte(cp);
		o.setEmploye(e);
		operationRepository.save(o);
		cp.setSolde(cp.getSolde()+montant);
		return true;
	}

	@Override
	@Transactional
	public boolean retirer(String code, double montant, Long codeEmp) {
		
		double AideDecouvert = 0;
		Compte cp=compteRepository.findById(code).orElse(null);
		if(cp instanceof CompteCourant)
			AideDecouvert=((CompteCourant) cp).getDecouvert();

		if(cp.getSolde()+AideDecouvert<montant) throw new RuntimeException("Solde Insuffisant");
		Employe e= employeRepository.findById(codeEmp).orElse(null);
		Operation o = new Retrait();
		o.setDateOperation(new Date());
		o.setMontant(montant);
		o.setCompte(cp);
		o.setEmploye(e);
		operationRepository.save(o);
		cp.setSolde(cp.getSolde()-montant);
		return true;
	}

	@Override
	@Transactional
	public boolean virement(String cpte1, String cpte2, double montant, Long codeEmp) {
		retirer(cpte1, montant, codeEmp);
		verser(cpte2, montant, codeEmp);
		return true;
	}


	@Override
	public Page<Operation> listOperation(String codeC, int page, int size) {

		return operationRepository.listOperation(codeC,new PageRequest(page,size));
	}


//	@Override
//	public PageOperation getOperation(String codeCompte, int page, int size) {
//		
//		Page<Operation> ops= operationRepository.getOperations(codeCompte, PageRequest.of(page, size));
//		
//		// 2 éme Solution
//		//Compte cp = compteRepository.findById(codeCompte).orElse(null);
//		//Page<Operation> ops2 = operationRepository.findByCompte(cp, (Pageable) new PageRequest(page, size));
//		
//		PageOperation pOp = new PageOperation();
//		
//			pOp.setOperations(ops.getContent());
//			pOp.setNombreOperations(ops.getNumberOfElements());
//			pOp.setPage(ops.getNumber());
//			pOp.setTotalpages(ops.getTotalPages());
//			
//		return pOp;
//	}

}
