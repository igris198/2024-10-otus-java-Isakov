package ru.otus.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DBServiceClient;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/allClients")
public class AllClientsServlet extends HttpServlet {
    private final transient DBServiceClient dbServiceClient;

    public AllClientsServlet(DBServiceClient dbServiceClient) {
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        List<Client> clients = dbServiceClient.findAll();

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<head><title>Список клиентов</title></head>");
        out.println("<body>");
        out.println("<h4>Список клиентов</h4>");
        out.println("<table style=\"width: 600px\" border= \"1\">");
        out.println("<thead>");
        out.println("<tr>");
        out.println("<th style=\"width: 100px\">Имя</td>");
        out.println("<th style=\"width: 200px\">Адрес</td>");
        out.println("<th style=\"width: 300px\">Телефоны</td>");
        out.println("</tr>");
        out.println("</thead>");
        out.println("<tbody>");
        for (Client client : clients) {
            String phones = client.getPhones().stream()
                    .map(Phone::getNumber)
                    .collect(Collectors.joining(", "));
            out.println("<tr>");
            out.println("<td style=\"width: 100px\">" + client.getName() + "</td>");
            out.println("<td style=\"width: 200px\">" + client.getAddress().getStreet() + "</td>");
            out.println("<td style=\"width: 300px\">" + phones + "</td>");
            out.println("</tr>");
        }
        out.println("</tbody>");
        out.println("</table>");
        out.println("</body>");
        out.println("</html>");
    }
}
