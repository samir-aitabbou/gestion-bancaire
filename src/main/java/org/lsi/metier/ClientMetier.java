package org.lsi.metier;

import java.util.List;

import org.lsi.entities.Client;

public interface ClientMetier {

	public Client saveClient(Client c);
	public List<Client> listClient();
	
}


