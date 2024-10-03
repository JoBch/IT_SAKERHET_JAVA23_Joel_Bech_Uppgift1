import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Resources {

    private static final String SERVER_URL = "http://localhost:8080/user";

    public static void deleteUserFromDb(String email) {
        HttpClient httpClient = HttpClient.newHttpClient();

        String jsonBody = "{\"email\":\"" + email + "\"}";

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_URL + "/deleteuser"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response: " + response.body() + " | Status Code: " + response.statusCode());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void displayUsersFromDB(String email) {
        HttpClient httpClient = HttpClient.newHttpClient();

        String jsonBody = "{\"email\":\"" + email + "\"}";

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_URL + "/displayuser"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response: " + response.body() + " | Status Code: " + response.statusCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void sendUserDataToServer(String name, String password, String email, String phone, String age, String newsletter) {
        HttpClient httpClient = HttpClient.newHttpClient();
        String hashedPassword = hashPassword(password);

        String jsonBody = String.format(
                "{\"name\":\"%s\", \"password\":\"%s\", \"email\":\"%s\", \"phone\":\"%s\", \"age\":\"%s\", \"newsletter\":%s}",
                name, hashedPassword, email, phone, age, newsletter);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_URL + "/register"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response: " + response.body() + " | Status Code: " + response.statusCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

}
