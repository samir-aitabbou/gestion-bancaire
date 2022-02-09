package org.lsi.services;

import java.util.List;

import org.lsi.dao.ClientRepository;
import org.lsi.dao.CompteRepository;
import org.lsi.entities.Client;
import org.lsi.entities.Compte;
import org.lsi.metier.ClientMetier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class ClientRestService {

	@Autowired
	private ClientRepository clientRepository;
	@Autowired
	private CompteRepository compteRepository;
	@Autowired
	private ClientMetier clientMetier;

	@RequestMapping("/AddC")
	public String aff(Model model){
		model.addAttribute("client",new Client());
		return "Clients/AddClient";
	}

	@RequestMapping(value="/addClient",method=RequestMethod.POST)
	public String saveClient(@Valid Client c, BindingResult bindingResult)
	{
		 if(bindingResult.hasErrors()) return "Clients/AddClient";
		clientMetier.saveClient(c);
		//return "Clients/AddSucces";
		return "Clients/ListClients";
	}

	@RequestMapping("/listClients")
	public String aff(Model model,@RequestParam(value = "motC",defaultValue = "") String motC,
					  @RequestParam(name = "page",defaultValue = "0") int page,
					  @RequestParam(name = "size",defaultValue = "5")int size){


		Page<Client> clients = clientRepository.chercher("%"+motC+"%",new PageRequest(page,size));
		model.addAttribute("listClients",clients.getContent());
		int[] pages = new int[clients.getTotalPages()];
		model.addAttribute("pages",pages);
		model.addAttribute("size",size);
		model.addAttribute("pageCourante",page);
		model.addAttribute("motC",motC);

		return "Clients/listClients";

	}

	@RequestMapping("/compteClient")
	public String affCompteCl(Model model,@RequestParam(value = "id") Long id,
							  @RequestParam(value = "motC",defaultValue = "") String motC,
							  @RequestParam(name = "page",defaultValue = "0") int page,
							  @RequestParam(name = "size",defaultValue = "5")int size){

		Page<Client> clients = clientRepository.chercher("%"+motC+"%",new PageRequest(page,size));
		model.addAttribute("listClients",clients.getContent());
		int[] pages = new int[clients.getTotalPages()];
		model.addAttribute("pages",pages);
		model.addAttribute("size",size);
		model.addAttribute("pageCourante",page);
		model.addAttribute("motC",motC);

		//Page<Compte> compte = compteRepository.findC(clientRepository.findOne(id),new PageRequest(page,size));
		Page<Compte> compte = compteRepository.findC(clientRepository.findOne(id),new PageRequest(page,size));
		model.addAttribute("LCC",compte.getContent());
		int[] pages2 = new int[compte.getTotalPages()];
		model.addAttribute("size2",size);
		model.addAttribute("pageCourante2",page);


		return "Clients/listClients";
	}

	@RequestMapping(value = "/deleteC",method = RequestMethod.GET)
	public String delete(Long id,String motC,int page,int size){
		clientRepository.deleteById(id);
		return "redirect:/listClients?motC="+motC+"&page="+page+"&size="+size;
	}


	@RequestMapping(value = "/editC",method = RequestMethod.GET)
	public String editP(Model model, Long id,String motC,int page,int size){
		Client p = clientRepository.findOne(id);
		model.addAttribute("client",p);
		model.addAttribute("size",size);
		model.addAttribute("pageCourante",page);
		model.addAttribute("motC",motC);
		return "Clients/EditClient";
	}

	@RequestMapping(value = "/SaveEdit",method = RequestMethod.POST)
	public String saveE(Model model, @Valid Client cl, BindingResult bindingResult,String m,int p,int s){
		if(bindingResult.hasErrors()) return "Clients/AddClient";
		clientRepository.save(cl);
		model.addAttribute("client",cl);
		return "redirect:/listClients?motC="+m+"&page="+p+"&size="+s;
		//return "Clients/clients.html";
	}


	@RequestMapping("/clients")
	public String clients()
	{
		return "redirect:/listClients";
		//return "Clients/listClients";
	}
}


