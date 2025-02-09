package ru.otus.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.crm.service.DBServiceClientExt;
import ru.otus.crm.service.DBServiceClientExtImpl;
import ru.otus.services.TemplateProcessor;

import java.io.IOException;
import java.util.Collections;

@WebServlet("/clients")
public class ClientsServlet extends HttpServlet {
    private static final String CLIENTS_PAGE_TEMPLATE = "clients.html";
    private static final String PARAM_NAME = "clientName";
    private static final String PARAM_ADDRESS = "clientAddress";
    private static final String PARAM_PHONES = "clientPhones";

    private final transient DBServiceClient dbServiceClient;
    private final transient TemplateProcessor templateProcessor;

    public ClientsServlet(TemplateProcessor templateProcessor, DBServiceClient dbServiceClient) {
        this.templateProcessor = templateProcessor;
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(CLIENTS_PAGE_TEMPLATE, Collections.emptyMap()));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter(PARAM_NAME);
        String street = request.getParameter(PARAM_ADDRESS);
        String phones = request.getParameter(PARAM_PHONES);

        DBServiceClientExt DBServiceClientExt = new DBServiceClientExtImpl(dbServiceClient);
        DBServiceClientExt.saveClient(name, street, phones);
        response.sendRedirect("/allClients");
    }
}
