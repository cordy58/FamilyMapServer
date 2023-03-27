package Handlers;

import java.io.*;
import java.net.*;

import Request.RegisterRequest;
import Result.RegisterResult;
import Services.RegisterService;
import com.google.gson.Gson;
import com.sun.net.httpserver.*;

public class UserRegisterHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;

        try {
            if (exchange.getRequestMethod().equalsIgnoreCase("post")) {
                Gson gson = new Gson();
                Reader requestData = new InputStreamReader(exchange.getRequestBody());
                RegisterRequest request = gson.fromJson(requestData, RegisterRequest.class);

                RegisterService registerService = new RegisterService();
                RegisterResult result = registerService.register(request);

                if (result.isSuccess()) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                }

                Writer responseBody = new OutputStreamWriter(exchange.getResponseBody());
                gson.toJson(result, responseBody);
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
