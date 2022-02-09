package org.lsi.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.lsi.dao.EmployeRepository;
import org.lsi.dao.GroupeRepository;
import org.lsi.entities.Client;
import org.lsi.entities.Compte;
import org.lsi.entities.Employe;
import org.lsi.entities.Groupe;
import org.lsi.metier.EmployeMetier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class EmployeRestService {
	
	@Autowired
	private EmployeMetier employeMetier;

	@Autowired
	private EmployeRepository employeRepository;

	@Autowired
	private GroupeRepository groupeRepository;

	@RequestMapping(value="/AddE")
	public String AddE(Model model)
	{
		List<Employe> employes = employeRepository.findAll();
		Employe employe= new Employe();
		List<Groupe> Listegroupes = groupeRepository.findAll();

		model.addAttribute("employes", employes);
		model.addAttribute("employe", employe);
		model.addAttribute("Listegroupes",Listegroupes);
		return"Employe/AddEmploye";
	}

	@RequestMapping(value = "/addEmploye",method = RequestMethod.POST)
	public String addEmploye(Employe employe,@RequestParam(value = "groupes") List<Long> groupes)
	{
		Collection<Groupe> col = new ArrayList<Groupe>();
		if(!groupes.isEmpty())
		{
			for(Long grp:groupes)
			{
				Groupe g = groupeRepository.findById(grp).get();
				col.add(g);
			}
			employe.setGroupes(col);
		}

		employeRepository.save(employe);
		return "Employe/ListeEmployes";
	}

	@RequestMapping(value = "/listEmployes", method = RequestMethod.GET)
	public String listEmployes(Model model, @RequestParam(value = "motC",defaultValue = "") Long motC)
	{

		Employe employe = employeRepository.findOne(motC);
		model.addAttribute("employe",employe);
		return "Employe/ListeEmployes";
	}



	@RequestMapping(value="/employes",method=RequestMethod.POST)
	public Employe saveEmploye(@RequestBody Employe e) {
		return employeMetier.saveEmploye(e);
	}

	@RequestMapping(value="/employes",method=RequestMethod.GET)
	public List<Employe> listEmployes() {
		return employeMetier.listEmployes();
	}

}
