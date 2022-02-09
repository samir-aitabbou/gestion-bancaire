package org.lsi.services;

import javax.websocket.server.PathParam;

import org.hibernate.engine.jdbc.Size;
import org.lsi.dao.ClientRepository;
import org.lsi.dao.CompteRepository;
import org.lsi.entities.*;
import org.lsi.metier.CompteMetier;
import org.lsi.metier.OperationMetier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
public class CompteRestService {
	
	@Autowired
	private CompteMetier compteMetier;

	@Autowired
	private ClientRepository ClientRepository;

	@Autowired
	private CompteRepository CompteRepository;

	@Autowired
	private OperationMetier OperationMetier;

	@RequestMapping("/AddCompte")
		public String aff(Model model){
			List<Client> c = ClientRepository.findAll();
			model.addAttribute("cl",c);
			return "Comptes/AddCompte";
		}

	@RequestMapping(value = "/SaveCompte", method=RequestMethod.GET)
	public String AjoutCompte(Model model, @RequestParam(value = "typeC") String type,
										   @RequestParam(value = "iden") String id,
										   @RequestParam(value = "cl") Long client,
										   @RequestParam(value = "D") Double d,
										   @RequestParam(value = "T") Double t,
										   @RequestParam(value = "sold") Double sold)
	{
		List<Compte> cl2 = CompteRepository.findAll();
		for (Compte c:cl2) {
			if (c.getCodeCompte().equals(id))
			{
				List<Client> cls = ClientRepository.findAll();
				model.addAttribute("cl",cls);
				model.addAttribute("iden",id);
				model.addAttribute("msg","Ce compte est déjà existe");
				return "Comptes/AddCompte";
			}
		}
		if (type.equals("CC")){
			CompteRepository.save(new CompteCourant(id,new Date(),sold,d,ClientRepository.findOne(client)));

		}else if (type.equals("CE")){
			CompteRepository.save(new CompteEpargne(id,new Date(),sold,t,ClientRepository.findOne(client)));
		};

		return "Comptes/ListComptes";
	}



	@RequestMapping("/listComptes")
	public String aff(Model model2,
					  @RequestParam(value = "motC",defaultValue = "") String motC,
					  @RequestParam(name = "page",defaultValue = "0") int page,
					  @RequestParam(name = "size",defaultValue = "5")int size)
	{

		Page<Compte> cpts = CompteRepository.search("%"+motC+"%",new PageRequest(page,size));
		Page<CompteCourant> cpts2 = CompteRepository.CC("%"+motC+"%",new PageRequest(page,size));
		Page<CompteEpargne> cpts3 = CompteRepository.CE("%"+motC+"%",new PageRequest(page,size));
		model2.addAttribute("listCpt",cpts.getContent());
		model2.addAttribute("listCC",cpts2.getContent());
		model2.addAttribute("listCE",cpts3.getContent());
		int[] pages22 = new int[cpts.getTotalPages()];
		model2.addAttribute("pages2",pages22);
		model2.addAttribute("size",size);
		model2.addAttribute("pageCourante",page);
		model2.addAttribute("motC",motC);

		return "Comptes/ListComptes";
	}

	@RequestMapping(value = "/deleteCC",method = RequestMethod.GET)
	public String deleteCC(String id,String motC,int page,int size)
	{
		//CompteRepository.deleteById("id");
		Compte cpt = CompteRepository.findOne(id);
		CompteRepository.update(id);
		return "redirect:/listComptes?motC="+motC+"&page="+page+"&size="+size;

	}

	@RequestMapping(value="/editCC", method = RequestMethod.GET)
	public String editC(Model model, String id,String motC,int page,int size)
	{
		List<Client> c = ClientRepository.findAll();
		model.addAttribute("cl",c);
		Compte cp = CompteRepository.findOne(id);
		model.addAttribute("cpt",cp);
		model.addAttribute("size",size);
		model.addAttribute("pageCourante",page);
		model.addAttribute("motC",motC);
		return "Comptes/EditCompte";
	}

	@RequestMapping(value = "/SaveEdit",method = RequestMethod.GET)
	public String SaveEdit(Model model,@RequestParam(value = "typeC") String type,
						@RequestParam(value = "iden1") String id1,
						@RequestParam(value = "iden") String id,
						@RequestParam(value = "cl") Long client,
						@RequestParam(value = "D") Double d,
						@RequestParam(value = "T") Double t,
						@RequestParam(value = "sold") Double sold)
	{
		//model.addAttribute("motC",id1);
		CompteRepository.deleteById(id1);
		if (type.equals("CC")){
			CompteRepository.save(new CompteCourant(id,new Date(),sold,d,ClientRepository.findOne(client)));
		}else if (type.equals("CE")){
			CompteRepository.save(new CompteEpargne(id,new Date(),sold,t,ClientRepository.findOne(client)));
		};

		//return "redirect:/listComptes";
		//return "redirect:/listComptes?motC= "+id+" ";
		return "Comptes/ListComptes";
	}

	@RequestMapping("/operations")
	public String inidex(){
		return "Comptes/comptes";
	}


	@RequestMapping("/consulterC")
	public String consulter(Model model, String codeC, @RequestParam(name = "page",defaultValue = "0") int page,
							@RequestParam(name = "size",defaultValue = "5")int size)
	{
		try {

			Compte cp = compteMetier.consulterCompte(codeC);
			Page<Operation> pageOperation = OperationMetier.listOperation(codeC,page,size);
			int[] pages = new int[pageOperation.getTotalPages()];
			model.addAttribute("pages",pages);
			model.addAttribute("listOperations",pageOperation.getContent());
			model.addAttribute("compte",cp);
			model.addAttribute("pageCourante",page);
			model.addAttribute("codeC",codeC);
		}catch (Exception e){
			model.addAttribute("exception",e);
		}

		return "Comptes/comptes";
	}

   // !! ici j'ai travaillé par l'emploiyé 1 il faut changer dans comptes.html pour passer l'employer en argument
	@RequestMapping(value = "/saveOperation",method = RequestMethod.POST)
	public String saveOperation(Model model,String codeC,String codeC2,String typeO,double montant){
		try {
			if (typeO.equals("Versement")){
				OperationMetier.verser(codeC,montant, 1L);
			}else if (typeO.equals("Retrait")){
				OperationMetier.retirer(codeC,montant, 1L);
			}else{
				OperationMetier.virement(codeC,codeC2,montant,1L);
			}
		}catch (Exception e){
			model.addAttribute("exception",e);
			return "redirect:/consulterC?codeC="+codeC+"&exception="+e.getMessage();
		}

		return "redirect:/consulterC?codeC="+codeC;
	}




	@RequestMapping("/comptes")
	public String inidex(Model model){
		return "redirect:/listComptes";
	}



	//@RequestMapping(value="/comptes", method=RequestMethod.POST)
	//public Compte saveCompte(@RequestBody Compte cp ) {
	//	return compteMetier.saveCompte(cp);
	//}

	//@RequestMapping(value="/comptes/{code}", method=RequestMethod.GET)
	//public Compte getCompte(@PathVariable String code) {
	//	return compteMetier.getCompte(code);
	//}


}
