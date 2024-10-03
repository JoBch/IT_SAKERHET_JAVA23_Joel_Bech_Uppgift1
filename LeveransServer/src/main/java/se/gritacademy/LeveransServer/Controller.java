package se.gritacademy.LeveransServer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@RestController
@RequestMapping("/user")
public class Controller {

    @PostMapping("/deleteuser")
    public String deleteUser(@RequestBody UserDTO user) {
        String sql = "DELETE FROM userdata WHERE email = ?";

        try {
            Connection connection = Database.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, user.getEmail());
            ps.executeUpdate();

            return "User successfully deleted!";
        } catch (SQLException e) {
            System.out.println("Error saving user: " + e.getMessage());
            return "Failed to delete user";
        }
    }

    @PostMapping("/register")
    public String saveToDB(@RequestBody UserDTO userDTO) {
        String sql = "INSERT INTO userdata (name, age, email, password, newsletter, phone) VALUES (?,?,?,?,?,?)";

        try {
            Connection connection = Database.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, userDTO.getName());
            ps.setString(2, userDTO.getAge());
            ps.setString(3, userDTO.getEmail());
            ps.setString(4, userDTO.getPassword());
            ps.setString(5, userDTO.getNewsletter());
            ps.setString(6, userDTO.getPhone());
            ps.executeUpdate();

            return "User successfully registered!";
        } catch (SQLException e) {
            System.out.println("Error saving user: " + e.getMessage());
            return "Failed to register user";
        }
    }

    @PostMapping("/checklogin")
    public ResponseEntity<Boolean> checkLogin(@RequestBody UserDTO userDTO) {
        String email = userDTO.getEmail();
        String passwordFromClient = userDTO.getPassword();
        System.out.println(passwordFromClient);

        String sql = "SELECT password FROM userdata WHERE email = ?";

        try {
            Connection connection = Database.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");

                //Compare the hashed password from the client with the one in the database
                if (passwordFromClient.equals(storedPassword)) {
                    return ResponseEntity.ok(true);
                } else {
                    return ResponseEntity.ok(false);
                }
            } else {
                return ResponseEntity.ok(false);
            }

        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false); // Database error
        }
    }



    @PostMapping("/displayuser")
    public String displayUserFromDB(@RequestBody UserDTO userDTO) {
        String sql = "SELECT * FROM userdata WHERE email = ?";
        StringBuilder result = new StringBuilder();

        try {
            Connection connection = Database.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, userDTO.getEmail());
            ResultSet rs = ps.executeQuery();

            // Fetch the results and append them to the result string
            if (rs.next()) {
                result.append("ID: ").append(rs.getString("id")).append("\n")
                        .append("Name: ").append(rs.getString("name")).append("\n")
                        .append("Age: ").append(rs.getString("age")).append("\n")
                        .append("Email: ").append(rs.getString("email")).append("\n")
                        .append("Phone: ").append(rs.getString("phone")).append("\n")
                        .append("Newsletter Subscription: ").append(rs.getString("newsletter")).append("\n")
                        .append("--------------------------------------\n");
            } else {
                return "No user found with the given email.";
            }

            return result.toString();

        } catch (SQLException e) {
            System.out.println("Error fetching user: " + e.getMessage());
            return "Failed to retrieve user: " + e.getMessage();
        }
    }

}
