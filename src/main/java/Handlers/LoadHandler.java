package Handlers;

import java.io.*;
import java.net.*;

import Request.LoadRequest;
import Result.LoadResult;
import Services.LoadService;
import com.google.gson.Gson;
import com.sun.net.httpserver.*;

public class LoadHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;

        try {
            if (exchange.getRequestMethod().equalsIgnoreCase("post")) {
                Gson gson = new Gson();
                Reader requestData = new InputStreamReader(exchange.getRequestBody());
                LoadRequest request = gson.fromJson(requestData, LoadRequest.class);

                LoadService loadService = new LoadService();
                LoadResult loadResult = loadService.load(request);

                if (loadResult.isSuccess()) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                }

                Writer responseBody = new OutputStreamWriter(exchange.getResponseBody());
                gson.toJson(loadResult, responseBody);
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
