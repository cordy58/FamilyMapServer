package Handlers;

import java.io.*;
import java.net.*;

import Result.FillResult;
import Services.FillService;
import com.sun.net.httpserver.*;

import com.google.gson.Gson;

public class FillUserInfoHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;

        try {
            if (exchange.getRequestMethod().equalsIgnoreCase("post")) {
                String[] urlPath = exchange.getRequestURI().toString().split("/");
                String username = urlPath[2];
                int generations = 4;
                if (urlPath[3] != null) {
                    generations = Integer.parseInt(urlPath[3]);
                }

                FillService fill = new FillService();
                FillResult fillResult = fill.fill(username, generations);

                if (fillResult.isSuccess()) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                }

                Gson gson = new Gson();
                Writer responseBody = new OutputStreamWriter(exchange.getResponseBody());
                gson.toJson(fillResult, responseBody);
                responseBody.close();

                success = true;
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
