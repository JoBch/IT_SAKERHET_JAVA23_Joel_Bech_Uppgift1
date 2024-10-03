import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Login {

    static int choice = 0;
    static String loginEmail = "";
    static String loginPassword = "";
    static Scanner scanner = new Scanner(System.in);
    private static final String SERVER_URL = "http://localhost:8080/user";

    public static void checkLogin(String email, String password) {
        loginEmail = email;
        loginPassword = Resources.hashPassword(password);
        HttpClient httpClient = HttpClient.newHttpClient();
        System.out.println(loginEmail);
        System.out.println(loginPassword);

        String jsonBody = "{\"email\":\"" + loginEmail + "\", \"password\":\"" + loginPassword + "\"}";

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_URL + "/checklogin"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            //Convert response body to Boolean
            boolean loginSuccessful = Boolean.parseBoolean(response.body());

            if (loginSuccessful) {
                loginSuccessful();
            } else {
                System.out.println("Invalid credentials.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createUser() {
        System.out.print("Enter Name: ");
        String name = scanner.next();

        System.out.print("Enter E-mail: ");
        String email = scanner.next();

        System.out.print("Enter age: ");
        String age = scanner.next();

        System.out.print("Enter PhoneNumber: ");
        String phone = scanner.next();

        System.out.print("Do you want our newsletter Y/N: ");
        String newsletter = scanner.next();
        newsletter = (newsletter.equalsIgnoreCase("y")) ? "true" : "false";

        System.out.print("Enter Password: ");
        String userPassword = scanner.next();

        //Send the collected data to the server
        Resources.sendUserDataToServer(name, userPassword, email, phone, age, newsletter);
        loginSuccessful();
    }

    public static void loginSuccessful(){
        while (true) {
            System.out.println("Welcome, make a choice between 1-Show, 2-Delete, 3-Quit");
            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    Resources.displayUsersFromDB(loginEmail);
                    break;

                case 2:
                    System.out.println("This will delete your info, are you sure? Press Y/N");
                    String confirmation = scanner.next();
                    confirmation = (confirmation.equalsIgnoreCase("y")) ? "true" : "false";
                    if (confirmation.equals("true")) {
                        Resources.deleteUserFromDb(loginEmail);
                        break;
                    }
                    else break;

                case 3:
                    System.exit(0);
            }
        }
    }

}
