package fr.limayrac.banque.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import fr.limayrac.banque.model.Client;
import fr.limayrac.banque.model.Compte;
import fr.limayrac.banque.service.*;

import java.util.List;
import java.util.Optional;

@RestController
public class clientsController {

    @Autowired
    private clientService clientService;
    @Autowired
    private compteService compteService;

    @GetMapping("")
    public ModelAndView homePage()
    {
        Client  client = new Client();
        return new ModelAndView("index", "client", client);
    }

    @PostMapping("client/connexion")
    public ModelAndView connexionClient(@ModelAttribute("client") Client client, ModelMap model)
    {
        Integer identif = client.getIdentifiant();
        String mdp = client.getPassword();
        Iterable<Client>  listClients = clientService.getClients();
        ModelAndView connexion = new ModelAndView();
        for (Client client2 : listClients)
        {
            if(client2.getIdentifiant() == identif)
            {
                if(client2.getPassword().equals(mdp))
                {
                    client = client2;
                    List<Compte> comptes = client.getComptes();
                    if(comptes.size()<2)
                    {
                        model.addAttribute("nonTransfert","0");
                    }
                    model.addAttribute("comptes",client.getComptes());
                    connexion.setViewName("detailClient");
                    connexion.addObject(client);
                    break;
                }
                else
                {
                    model.addAttribute("erreur", "Le mots de passe saisie est incorrect");
                    connexion.setViewName("index");
                    connexion.addObject(client);
                }
            }
        }

        if(connexion.getViewName() == null)
        {
            model.addAttribute("erreur", "L'identifiant saisie est incorrect");
            connexion.setViewName("index");
            connexion.addObject(client);
        }
        return connexion;
    }

    @GetMapping("/client/lister")
    public ModelAndView listeClients()
    {
        return new ModelAndView("listeClient", "clients", clientService.getClients());
    }
    
    @GetMapping("/client/lister/{id}")
    public ModelAndView detailClient(@PathVariable("id") final Integer id)
    {
        Optional<Client> client = clientService.getClient(id);
        return new ModelAndView("detailClient", "client", client.orElse(null));
    }

    @GetMapping("/client/creer")
    public ModelAndView creerClient(){
        Client  c = new Client();
        return new ModelAndView("creationClientForm","client",c);
    }

    @PostMapping("/client/creer")
    public ModelAndView create(@ModelAttribute("client") Client client, ModelMap model)
    {
        model.addAttribute("nom",client.getNom());
        model.addAttribute("prenom",client.getPrenom());
        model.addAttribute("identifiant",client.getIdentifiant());
        model.addAttribute("password",client.getPassword());
        model.addAttribute("email",client.getEmail());

        clientService.saveClient(client);
        return new ModelAndView("index");
    }

    @GetMapping("/client/editer/{id}")
    public ModelAndView editerClient(@PathVariable("id") final Integer id){
        Optional<Client> client = clientService.getClient(id);
        return new ModelAndView("editerClient", "client", client.orElse(null));
    }

    @PostMapping("/client/editer")
    public ModelAndView submitEdition(@ModelAttribute("client") Client client, ModelMap model)
    {
        clientService.saveClient(client);
        return listeClients();
    }

    @GetMapping("/client/effacer/{id}")
    public ModelAndView effacerClient(@PathVariable("id") final Integer id)
    {
        Optional<Client> clientById = clientService.getClient(id);
        Client client = clientById.get();
        List<Compte> comptes = client.getComptes();
        for (Compte compte : comptes) {
            compteService.deleteCompte(compte.getId());
        }
        clientService.deleteClient(id);
        return listeClients();
    }
}
