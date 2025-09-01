import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;



public class CrptAPI {
    private final TimeUnit timeUnit;
    private final int requestLimit;
    private static HttpClient httpClient;
    private final ReentrantLock lock;
    private final Deque<Instant> requestTimeStamps;
    private final ObjectMapper objectMapper;


    public CrptAPI(TimeUnit timeUnit, int requestLimit) {
        this.timeUnit = timeUnit;
        this.requestLimit = requestLimit;
        httpClient = HttpClient.newHttpClient();
        this.lock = new ReentrantLock(true);
        this.requestTimeStamps = new ArrayDeque<>();
        this.objectMapper=new ObjectMapper();
    }
    //Вложенный класс документа
    class Document{
        public static class Description{
            private String participationInn;
            public String getParticipationInn(){return participationInn;}
            public void setParticipationInn(String s){this.participationInn=s;}
        }
        public static class Products{
            private String certificate_document;
            private String certificate_document_date;
            private String certificate_document_number;
            private String owner_inn;
            private String producer_inn;
            private String production_date;
            private String tnved_code;
            private String uit_code;
            private String uitu_code;
            private CertificateType certificateType;
            public enum CertificateType {
                CONFORMITY_CERTIFICATE("CONFORMITY_CERTIFICATE"),
                CONFORMITY_DECLARATION("CONFORMITY_DECLARATION");

                private final String certificate_documentValue;

                CertificateType(String _certificate_documentValue) {
                    this.certificate_documentValue = _certificate_documentValue;
                }

                public String getCertificate_documentValue() {
                    return certificate_documentValue;
                }

                // Метод для десериализации из строки
                public static CertificateType fromCertificate_documentValue(String value) {
                    for (CertificateType type : values()) {
                        if (type.certificate_documentValue.equals(value)) {
                            return type;
                        }
                    }
                    throw new IllegalArgumentException("Неизвестный тип документа: " + value);
                }
            }
            public String getCertificate_document() {
                return certificate_document;
            }

            public void setCertificate_document(String certificate_document) {
                this.certificate_document = certificate_document;
            }

            public CertificateType getCertificateType(){
                return certificateType;
            }
            public void setCertificateType(CertificateType type){
                this.certificateType=type;
            }

            public String getCertificate_document_date() {
                return certificate_document_date;
            }

            public void setCertificate_document_date(String certificate_document_date) {
                this.certificate_document_date = certificate_document_date;
            }

            public String getCertificate_document_number() {
                return certificate_document_number;
            }

            public void setCertificate_document_number(String certificate_document_number) {
                this.certificate_document_number = certificate_document_number;
            }

            public String getOwner_inn() {
                return owner_inn;
            }

            public void setOwner_inn(String owner_inn) {
                this.owner_inn = owner_inn;
            }

            public String getProducer_inn() {
                return producer_inn;
            }

            public void setProducer_inn(String producer_inn) {
                this.producer_inn = producer_inn;
            }

            public String getProduction_date() {
                return production_date;
            }

            public void setProduction_date(String production_date) {
                this.production_date = production_date;
            }

            public String getTnved_code() {
                return tnved_code;
            }

            public void setTnved_code(String _tnved_code) {
                if (_tnved_code == null || !_tnved_code.matches("^\\d{10}$")) {
                    throw new IllegalArgumentException("Код ТН ВЭД должен состоять ровно из 10 цифр. Получено: " + _tnved_code);
                }
                this.tnved_code = _tnved_code;
            }

            public String getUit_code() {
                return uit_code;
            }

            public void setUit_code(String uit_code) {
                this.uit_code=uit_code;
            }

            public String getUitu_code() {
                return uitu_code;
            }

            public void setUitu_code(String uitu_code) {
                this.uitu_code = uitu_code;
            }


        }
        private Description description;
        private String doc_id;
        private String doc_status;
        private String doc_type;
        private String importRequest;
        private String owner_inn;
        private String produser_inn;
        private String production_date;
        public enum production_dateType{
            OWN_PRODUCTION("OWN_PRODUCTION"),
            PRODUCT_PRODUCTION("PRODUCT_PRODUCTION");

            private String productionType;

            production_dateType(String type){
                this.productionType=type;
            }
            public String getType(){
                return productionType;
            }
            // Метод для десериализации из строки
            public static production_dateType fromCertificate_documentValue(String value) {
                for (Document.production_dateType type : values()) {
                    if (type.productionType.equals(value)) {
                        return type;
                    }
                }
                throw new IllegalArgumentException("Неизвестный тип документа: " + value);
            }
        }
        private String production_type;
        private Products[] productsArray;
        private String reg_Date;
        private String reg_number;

        public Description getDescription() {
            return description;
        }

        public void setDescription(Description description) {
            this.description = description;
        }

        public String getDoc_id() {
            return doc_id;
        }

        public void setDoc_id(String doc_id) {
            this.doc_id = doc_id;
        }

        public String getDoc_status() {
            return doc_status;
        }

        public void setDoc_status(String doc_status) {
            this.doc_status = doc_status;
        }

        public String getDoc_type() {
            return doc_type;
        }

        public void setDoc_type(String doc_type) {
            this.doc_type = doc_type;
        }

        public String getImportRequest() {
            return importRequest;
        }

        public void setImportRequest(String importRequest) {
            this.importRequest = importRequest;
        }

        public String getProduser_inn() {
            return produser_inn;
        }

        public void setProduser_inn(String produser_inn) {
            this.produser_inn = produser_inn;
        }

        public String getOwner_inn() {
            return owner_inn;
        }

        public void setOwner_inn(String owner_inn) {
            this.owner_inn = owner_inn;
        }

        public String getProduction_date() {
            return production_date;
        }

        public void setProduction_date(String production_date) {
            this.production_date = production_date;
        }

        private Document.production_dateType production_dateType;

        public production_dateType getProduction_dateType(){
            return  production_dateType;
        }

        public void setProduction_dateType(production_dateType type){
            this.production_dateType=type;
        }

        public Products[] getProducts(){
            return productsArray;
        }
        public void setProductsArray(Products[] product){
            this.productsArray=product;
        }

        public String getReg_Date() {
            return reg_Date;
        }

        public void setReg_Date(String reg_Date) {
            this.reg_Date = reg_Date;
        }

        public String getReg_number() {
            return reg_number;
        }

        public void setReg_number(String reg_number) {
            this.reg_number = reg_number;
        }

        public String getProduction_type() {
            return production_type;
        }

        public void setProduction_type(String production_type) {
            this.production_type = production_type;
        }
    }
    //___Нужно реализовать ЕДИНСТВЕННЫЙ метод создания документа, но при этом ничего не говорится про метод для обращения к API__
    //___Метод, созданный ниже нарушает принцип единственной ответственности___
    public void CreateDockument(Document document, String signature) {
        lock.lock();
        try {
            waiting();
            performRequest(document, signature);
            recordRequest();
        } finally {
            lock.unlock();
        }
    }
    private void waiting() {
        Instant now = Instant.now();
        Instant thresHold = now.minus(Duration.ofMillis(timeUnit.toMillis(1)));
        //
        while (!requestTimeStamps.isEmpty() && requestTimeStamps.peek().isBefore(thresHold)) {
            requestTimeStamps.poll();
        }

        if (requestTimeStamps.size() >= requestLimit) {
            Instant oldest = requestTimeStamps.peek();
            long sleepTime = Duration.between(thresHold, oldest).toMillis();
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Interrupted while waiting for rate limit", e);
                }
            }
            waiting();
        }
    }
    //Добавляем в очередь время вызова запроса
    private void recordRequest() {
        requestTimeStamps.add(Instant.now());
    }
    //___Как формируктся сигнатура?___
    private void performRequest(Document document, String signature) {
        try {
            String requestBody = objectMapper.writeValueAsString(document);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://ismp.crpt.ru/api/v3/lk/documents/create"))
                    .header("Content-Type", "application/json")
                    .header("Signature", signature)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .timeout(Duration.ofSeconds(30))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 400) {
                throw new RuntimeException("API request failed with status: " + response.statusCode());
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize document", e);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to send request to API", e);
        }
    }
}
