package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class CrptApi {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final AtomicInteger requestCount = new AtomicInteger(0);
    private final int requestLimit;
    private final OkHttpClient httpClient = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();


    public CrptApi(TimeUnit timeUnit, int requestLimit) {
        this.requestLimit = requestLimit;

        scheduler.scheduleAtFixedRate(() ->requestCount.set(0), 0, 1, timeUnit);
    }

    public synchronized void createDocument(Document document, String signature) throws IOException, InterruptedException {
        while (requestCount.get() >= requestLimit) {
            wait();
        }

        requestCount.incrementAndGet();
        try {
            String json = objectMapper.writeValueAsString(document);
            RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
            Request request = new Request.Builder()
                    .url("https://ismp.crpt.ru/api/v3/lk/documents/create")
                    .post(body)
                    .addHeader("Signature", signature)
                    .build();
            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }
            }
        } finally {
            requestCount.decrementAndGet();
            notifyAll();
        }
    }

    public static class Document {
        public Description description;
        public String doc_id;
        public String doc_status;
        public String doc_type;
        public boolean importRequest;
        public String owner_inn;
        public String participant_inn;
        public String producer_inn;
        public String production_date;
        public String production_type;
        public Product[] products;
        public String reg_date;
        public String reg_number;


        public static class Description {
            public String participantInn;
        }

        public static class Product{
            public String certificate_document;
            public String certificate_document_date;
            public String certificate_document_number;
            public String owner_inn;
            public String producer_inn;
            public String production_date;
            public String tnved_code;
            public String uit_code;
            public String uitu_code;
        }
    }
}
