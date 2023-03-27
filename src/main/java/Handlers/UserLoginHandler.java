package Handlers;

import java.io.*;
import java.net.*;

import Request.LoginRequest;
import com.sun.net.httpserver.*;

import Result.LoginResult;
import Services.LoginService;

import com.google.gson.Gson;

public class UserLoginHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;

        try {
            if (exchange.getRequestMethod().equalsIgnoreCase("post")) {
                Gson gson = new Gson();
                Reader requestData = new InputStreamReader(exchange.getRequestBody());
                LoginRequest request = gson.fromJson(requestData, LoginRequest.class);

                LoginService loginService = new LoginService();
                LoginResult loginResult = loginService.login(request);

                if (loginResult.isSuccess()) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                }

                Writer responseBody = new OutputStreamWriter(exchange.getResponseBody());
                gson.toJson(loginResult, responseBody);
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
