package pl.coderslab.entity;

import org.mindrot.jbcrypt.BCrypt;
import pl.coderslab.utils.DbUtil;

import java.sql.*;
import java.util.Arrays;


public class UserDao {
    private static final String CREATE_USER_QUERY = "INSERT INTO users (username, email, password) VALUES (?, ?, ? );";
    private static final String READ_USER_QUERY = "SELECT * FROM users WHERE columnName = ?;";
    private static final String UPDATE_USER_QUERY = "UPDATE users SET username = ?, email = ?, password = ? WHERE id = ?;";
    private static final String DELETE_USER_QUERY = "DELETE FROM users WHERE id = ?";

    public static void main(String[] args) {
//        ---- example user in workshop2.sql dump ----
//        username: "test_user"
//        email: "example@example.com"
//        password "test"
    }

    public User create(User user) {
        try (Connection dbConnect = DbUtil.getConnection()) {
            PreparedStatement statement = null;
            statement = dbConnect.prepareStatement(CREATE_USER_QUERY, statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getEmail());
            statement.setString(3, BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
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

    public User read(int userId) {
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

    public User read(String email) {
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

    public void update(User user) {
        final String query = "SELECT password FROM users WHERE id = ?;";
        try (Connection dbConnect = DbUtil.getConnection()) {
            PreparedStatement statement = dbConnect.prepareStatement(UPDATE_USER_QUERY);
            PreparedStatement checkPswrdStatement = dbConnect.prepareStatement(query);

            checkPswrdStatement.setInt(1, user.getId());
            ResultSet resultSet = checkPswrdStatement.executeQuery();
            resultSet.next();

            final String passwordInDb = resultSet.getString(1);

            statement.setString(1, user.getUserName());
            statement.setString(2, user.getEmail());

            // check if password in DB == password in given User object
            if (!passwordInDb.equals(user.getPassword())) {
                statement.setString(3, BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
            } else {
                statement.setString(3, user.getPassword());
            }

            statement.setInt(4, user.getId());
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int userId) {
        try (Connection dbConnect = DbUtil.getConnection()) {
            PreparedStatement statement = dbConnect.prepareStatement(DELETE_USER_QUERY);
            statement.setInt(1, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User[] findAll() {
        String query = "SELECT * FROM users";
        User[] usersArr = new User[0];
        try (Connection dbConnect = DbUtil.getConnection()) {
            PreparedStatement statement = dbConnect.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User newUser = new User();
                newUser.setId(resultSet.getInt("id"));
                newUser.setUserName(resultSet.getString("username"));
                newUser.setEmail(resultSet.getString("email"));
                newUser.setPassword(resultSet.getString("password"));
//                alt create of User
//                User newUser2 = new User(
//                        resultSet.getString("username"),
//                        resultSet.getString("email"),
//                        resultSet.getString("password")
//                );
//                newUser2.setId(resultSet.getInt("id"));
                usersArr = addToArray(newUser, usersArr);
            }
            return usersArr;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private User[] addToArray(User newUser, User[] usersArr) {
        User[] tempArr = Arrays.copyOf(usersArr, usersArr.length + 1);
        tempArr[tempArr.length - 1] = newUser;
        return tempArr;
    }


}
