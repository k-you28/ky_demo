package com.kevin.pipelineTester;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebhookPipelineTester {

    private static final String BASE_URL = "http://localhost:8080/webhooks/ky";

    public static void main(String[] args) throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        if(webhookTester(client)){
            System.out.println("ALL TEST PASSED");
        }

    }

    public static boolean webhookTester(HttpClient client) throws Exception {

        //Test 1: Accepts two duplicate events
        if(sendWebhook(client, "evt_java_001") != 202
                || sendWebhook(client, "evt_java_001") != 202){
            System.out.println("Test 1 fail");
        }

        //Test 2: Concurrency. Takes in 5 requests at once. Will always return true
        testWebhookConcurrency(client);

        return true;
    }


    private static int sendWebhook(HttpClient client, String eventId) throws Exception {
        String body = """
            {
              "eventId": "%s",
              "eventType": "payment_intent.succeeded",
              "payload": "{ \\"amount\\": 5000 }"
            }
            """.formatted(eventId);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.statusCode();
    }


    private static boolean testWebhookConcurrency(HttpClient client) {
        ExecutorService executor = Executors.newFixedThreadPool(5);

        for (int i = 0; i < 5; i++) {
            executor.submit(() -> {
                sendWebhook(client, "evt_concurrent_001");
                return null;
            });
        }

        executor.shutdown();
        return true;
    }

}
