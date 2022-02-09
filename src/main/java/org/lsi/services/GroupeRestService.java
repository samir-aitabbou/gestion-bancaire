package org.lsi.services;

import org.lsi.dao.GroupeRepository;
import org.lsi.entities.Client;
import org.lsi.entities.Groupe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GroupeRestService {
    @Autowired
    org.lsi.dao.GroupeRepository groupeRepository;

    @RequestMapping(value = "/AddG")
    public String AddG()
    {
        return"Groupes/AddGroupe";
    }

    @RequestMapping(value = "/addGroupe", method= RequestMethod.POST)
    public String addGroupe(String nomGroupe)
    {
        groupeRepository.save(new Groupe(nomGroupe));
        return"Groupes/ListeGroupes";
    }

    @RequestMapping("/listGroupes")
    public String aff(Model model,@RequestParam(value = "motC",defaultValue = "") String motC,
                      @RequestParam(name = "page",defaultValue = "0") int page,
                      @RequestParam(name = "size",defaultValue = "5")int size){


        Page<Groupe> listGroupes = groupeRepository.chercher("%"+motC+"%",new PageRequest(page,size));
        model.addAttribute("listGroupes",listGroupes.getContent());
        int[] pages = new int[listGroupes.getTotalPages()];
        model.addAttribute("pages",pages);
        model.addAttribute("size",size);
        model.addAttribute("pageCourante",page);
        model.addAttribute("motC",motC);

        return "Groupes/ListeGroupes";

    }

}
