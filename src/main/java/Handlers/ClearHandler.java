package Handlers;

import java.io.*;
import java.net.*;

import Result.ClearResult;
import Services.ClearService;

import com.google.gson.Gson;
import com.sun.net.httpserver.*;

public class ClearHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;

        try {
            if (exchange.getRequestMethod().equalsIgnoreCase("post")) {
                ClearService clearService = new ClearService();
                ClearResult clearResult = clearService.clear();

                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                Writer responseBody = new OutputStreamWriter(exchange.getResponseBody());
                Gson gson = new Gson();
                gson.toJson(clearResult, responseBody);
                responseBody.close();

                success = true;
            }

            if (!success) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                exchange.getResponseBody().close();
            }

        } catch (IOException e){
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }
}
