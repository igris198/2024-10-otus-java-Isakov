package ru.otus.controllers;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.crm.model.Client;
import ru.otus.crm.service.DBServiceClient;

@Controller
public class AllClientsController {
    private final DBServiceClient dbServiceClient;

    public AllClientsController(DBServiceClient dbServiceClient) {
        this.dbServiceClient = dbServiceClient;
    }

    @GetMapping("/allClients")
    public String clientsListView(Model model) {
        List<Client> clients = dbServiceClient.findAll();
        model.addAttribute("clients", clients);
        return "clientsList";
    }
}
