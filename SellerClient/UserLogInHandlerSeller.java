import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class UserLogInHandlerSeller {

    public static User getUserInfo(iSeller server, Scanner scanner) throws IOException {
        try {
            while (true) {
                System.out.print("\nPlease, enter \"L\" to LOG IN or \"S\" to SIGN UP: ");
                String line = scanner.nextLine().toUpperCase();
                if (line.equals("L")) {
                    return logIn(server, scanner);
                } else if (line.equals("S")) {
                    return signUp(server, scanner);
                }
            }
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    private static User signUp(iSeller server, Scanner scanner) throws IOException, NoSuchElementException {
        while (true) {
            System.out.print("\nEnter Username: ");
            String username = scanner.nextLine();
            if (username.toUpperCase().equals("BACK")) {
                return getUserInfo(server, scanner);
            }
            if (server.doesUsernameExist(username)) {
                System.out.println("The entered username is already taken. Please try again.");
                return signUp(server, scanner);
            }
            System.out.print("Enter Email: ");
            String email = scanner.nextLine();
            System.out.print("Enter Password: ");
            String password = scanner.nextLine();
            User user = server.signUp(username, email, password);
            if (user == null) {
                System.out.print("The given username is already taken.");
            } else {
                return user;
            }
        }
    }

    private static User logIn(iSeller server, Scanner scanner) throws IOException, NoSuchElementException {
        while (true) {
            System.out.print("\nEnter Username: ");
            String username = scanner.nextLine();
            if (username.equals("BACK")) {
                return getUserInfo(server, scanner);
            }
            if (!server.doesUsernameExist(username)) {
                System.out.println("The entered username doesn't exists. Please try again.");
                logIn(server, scanner);
            }
            System.out.print("\nEnter Password: ");
            String password = scanner.nextLine();

            User user = server.logIn(username, password);
            if (user == null) {
                System.out.println("The given details aren't valid.");
            } else {
                return user;
            }
        }
    }
}