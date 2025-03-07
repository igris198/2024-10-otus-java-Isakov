package ru.otus.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.crm.model.Client;
import ru.otus.crm.service.DBServiceClientExt;

@Controller
public class ClientsController {
    private final DBServiceClientExt dbServiceClientExt;

    public ClientsController(DBServiceClientExt dbServiceClientExt) {
        this.dbServiceClientExt = dbServiceClientExt;
    }

    @GetMapping("/clients")
    public String clientCreateView(Model model) {
        model.addAttribute("client", new Client());
        return "clients";
    }

    @PostMapping("/clients")
    public RedirectView clientSave(
            @RequestParam String clientName, @RequestParam String clientAddress, @RequestParam String clientPhones) {
        dbServiceClientExt.saveClient(clientName, clientAddress, clientPhones);
        return new RedirectView("/allClients", true);
    }
}
