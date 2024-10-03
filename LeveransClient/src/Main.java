import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        int choice = 0;
        String loginEmail = "";
        String loginPassword = "";
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome user, make a choice between 1-Login, 2-Create New User");
        choice = scanner.nextInt();
            switch (choice){
                case 1:
                    System.out.println("Please enter your email: ");
                    loginEmail = scanner.next();
                    System.out.println("Please enter your password: ");
                    loginPassword = scanner.next();
                    Login.checkLogin(loginEmail, loginPassword);
                    break;

                case 2:
                    System.out.println("Please enter your credentials.");
                    Login.createUser();
                    break;

            }
    }
}
