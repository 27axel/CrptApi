package com.example;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class CrptApiTest {
    public static void main(String[] args) {
        CrptApi api = new CrptApi(TimeUnit.MINUTES, 5);

        CrptApi.Document document = new CrptApi.Document();
        document.doc_id = "test_doc_id";
        document.doc_status = "test_status";
        document.doc_type = "LP_INTRODUCE_GOODS";
        document.importRequest = true;
        document.owner_inn = "1234567890";
        document.participant_inn = "0987654321";
        document.producer_inn = "1122334455";
        document.production_date = "2023-01-23";
        document.production_type = "test_type";
        document.reg_date = "2023-01-23";
        document.reg_number = "test_reg_number";

        CrptApi.Document.Description description = new CrptApi.Document.Description();
        description.participantInn = "1234567890";
        document.description = description;

        CrptApi.Document.Product product = new CrptApi.Document.Product();
        product.certificate_document = "test_certificate_document";
        product.certificate_document_date = "2023-01-23";
        product.certificate_document_number = "test_certificate_document_number";
        product.owner_inn = "1234567890";
        product.producer_inn = "1122334455";
        product.production_date = "2023-01-23";
        product.tnved_code = "test_tnved_code";
        product.uit_code = "test_uit_code";
        product.uitu_code = "test_uitu_code";

        document.products = new CrptApi.Document.Product[] { product };

        String signature = "test_signature";

        try {
            api.createDocument(document, signature);
            System.out.println("Запрос " + (1) + " успешно выполнен.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("Ошибка при выполнении запроса " + (1));
        }
    }
}
