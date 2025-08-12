package com.marcosvn3.appclimacomapi;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ClimaApp {
    private static final String API_CHAVE = "SUA_CHAVI_AQUI";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather?q=%s&APPID=%s&units=metric&lang=pt_br";

    public static void main(String[] args) {
        String cidade = "Aracaju";
        buscarCidade(cidade);
    }

    public static void buscarCidade(String cidade) {
        HttpClient client = HttpClient.newHttpClient();
        String url = String.format(BASE_URL, cidade, API_CHAVE); // Corrigido a ordem dos parâmetros

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                processarResposta(response.body());
            } else {
                System.err.println("Erro: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void processarResposta(String JsonResponse) {
        JsonObject json = new JsonParser().parse(JsonResponse).getAsJsonObject();

        String cidade = json.get("name").getAsString();
        JsonObject main = json.getAsJsonObject("main");
        double temp = main.get("temp").getAsDouble();
        double umidade = main.get("humidity").getAsDouble();
        JsonObject clima = json.getAsJsonArray("weather").get(0).getAsJsonObject();
        String descricao = clima.get("description").getAsString();

        System.out.println("\n========Previsao do tempo==================");
        System.out.println("Cidade: " + cidade);
        System.out.println("Temperatura: " + temp + "°C");
        System.out.println("Umidade: "+ umidade+"%");
        System.out.println("Condicões: "+ descricao);
    }
}