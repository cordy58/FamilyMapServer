package Handlers;

import java.io.*;
import java.net.*;

import Result.FindPersonResult;
import Services.FindPersonService;
import com.google.gson.Gson;
import com.sun.net.httpserver.*;

public class FetchPersonHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;

        try {
            if (exchange.getRequestMethod().equalsIgnoreCase("get")) {
                Headers requiredHeader = exchange.getRequestHeaders();
                if (requiredHeader.containsKey("Authorization")) {
                    String authToken = requiredHeader.getFirst("Authorization");

                    String[] urlPath = exchange.getRequestURI().toString().split("/");
                    String personID = urlPath[2];

                    FindPersonService findPersonService = new FindPersonService();
                    FindPersonResult result = findPersonService.findPerson(authToken, personID);

                    if (result.isSuccess()) {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    } else {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST,0);
                    }

                    Gson gson = new Gson();
                    Writer responseBody = new OutputStreamWriter(exchange.getResponseBody());
                    gson.toJson(result, responseBody);
                    responseBody.close();

                    success = true;
                }
            }

            if (!success) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                exchange.getResponseBody().close();
            }
        } catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }
}
