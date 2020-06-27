package pl.coderslab.entity;

import pl.coderslab.utils.DbUtil;

import java.sql.*;

public class UserDao {
    private static final String CREATE_USER_QUERY = "INSERT INTO users (username, email, password) VALUES (?, ?, ? );";
    private static final String READ_USER_QUERY = "SELECT * FROM users WHERE columnName = ?;";
    private static final String UPDATE_USER_QUERY = "UPDATE users SET ? = ? WHERE ? = ?;";
    private static final String DELETE_USER_QUERY = "DELETE FROM users WHERE id = userId";

    public static void main(String[] args) throws SQLException {
//        User user1 = new User();
//        user1.setEmail("tikiriki999@blabla.com");
//        user1.setPassword("lalala");
//        user1.setUserName("pl.costam");
//        System.out.println(create(user1).getId());
        User checkUser = read("tikiriki@blabla.com");
        System.out.println(checkUser.getId());
        System.out.println(checkUser.getUserName());
        System.out.println(checkUser.getEmail());
        System.out.println(checkUser.getPassword());
//        System.out.println(read(12));
    }

    public static User create(User user) throws SQLException {
        try (Connection dbConnect = DbUtil.getConnection()) {
            PreparedStatement statement = null;
            statement = dbConnect.prepareStatement(CREATE_USER_QUERY, statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                user.setId(rs.getInt(1));
            }
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static User read(int userId) throws SQLException {
        try (Connection dbConnect = DbUtil.getConnection()) {
            PreparedStatement statement = dbConnect.prepareStatement(READ_USER_QUERY.replace("columnName", "id"));
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User toReturnUser = new User(resultSet.getString("username"), resultSet.getString("email"), resultSet.getString("password"));
                toReturnUser.setId(resultSet.getInt("id"));
                return toReturnUser;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

        public static User read(String email) throws SQLException {
        try (Connection dbConnect = DbUtil.getConnection()) {
            PreparedStatement statement = dbConnect.prepareStatement(READ_USER_QUERY.replace("columnName", "email"));
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User toReturnUser = new User(resultSet.getString("username"), resultSet.getString("email"), resultSet.getString("password"));
                toReturnUser.setId(resultSet.getInt("id"));
                return toReturnUser;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
