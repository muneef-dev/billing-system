package com.example.billingsystem.dao.custom.impl;

import com.example.billingsystem.dao.custom.UserDao;
import com.example.billingsystem.entity.User;
import com.example.billingsystem.dao.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class UserDaoImpl implements UserDao {
    @Override
    public boolean create(User user) throws SQLException, ClassNotFoundException {
        Logger.getLogger(UserDaoImpl.class.getName()).log(Level.INFO, "UserDao Executing SQL with ID: " + user.getId());
        return CrudUtil.execute("INSERT INTO users (id, username, email, password, user_role, created_at, last_login) VALUES (?,?,?,?,?,?,?)",
                user.getId(), user.getUsername(), user.getEmail(), user.getPassword(), user.getRole(), user.getCreatedAt(), user.getLastLogin());
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM users WHERE id=?",id);
    }

    @Override
    public boolean update(User user) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE users SET username=?, email=?, password=?, user_role=?, created_at=?, last_login=? WHERE id=?",
                user.getUsername(), user.getEmail(), user.getPassword(), user.getRole(), user.getCreatedAt(), user.getLastLogin(), user.getId());
    }

    @Override
    public User findByUsername(String username) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM users WHERE username=?", username);
        if (resultSet.next()) {
            return new User(
                    resultSet.getString("id"),
                    resultSet.getString("username"),
                    resultSet.getString("email"),
                    resultSet.getString("password"),
                    resultSet.getString("user_role"),
                    resultSet.getTimestamp("created_at"),
                    resultSet.getTimestamp("last_login")
            );
        }
        return null;
    }

    @Override
    public User findByEmail(String email) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM users WHERE email=?", email);
        if (resultSet.next()) {
            return new User(
                    resultSet.getString("id"),
                    resultSet.getString("username"),
                    resultSet.getString("email"),
                    resultSet.getString("password"),
                    resultSet.getString("user_role"),
                    resultSet.getTimestamp("created_at"),
                    resultSet.getTimestamp("last_login")
            );
        }
        return null;
    }

    @Override
    public User findByUsernameOrEmail(String usernameOrEmail) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM users WHERE username=? OR email=?", usernameOrEmail, usernameOrEmail);
        if (resultSet.next()) {
            return new User(
                    resultSet.getString("id"),
                    resultSet.getString("username"),
                    resultSet.getString("email"),
                    resultSet.getString("password"),
                    resultSet.getString("user_role"),
                    resultSet.getTimestamp("created_at"),
                    resultSet.getTimestamp("last_login")
            );
        }
        return null;
    }

    @Override
    public User findById(String id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM users WHERE id=?", id);
        if (resultSet.next()) {
            return new User(
                    resultSet.getString("id"),
                    resultSet.getString("username"),
                    resultSet.getString("email"),
                    resultSet.getString("password"),
                    resultSet.getString("user_role"),
                    resultSet.getTimestamp("created_at"),
                    resultSet.getTimestamp("last_login")
            );
        }
        return null;
    }

    @Override
    public boolean existsByEmail(String email) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT COUNT(*) FROM users WHERE email=?", email);
        if (resultSet.next()) {
            return resultSet.getInt(1) > 0;
        }
        return false;
    }

    @Override
    public boolean existsByUsername(String username) throws SQLException, ClassNotFoundException {
        if (username == null || username.trim().isEmpty()) {
            return false; // Username is optional, so empty/null usernames don't count as duplicates
        }
        ResultSet resultSet = CrudUtil.execute("SELECT COUNT(*) FROM users WHERE username=?", username);
        if (resultSet.next()) {
            return resultSet.getInt(1) > 0;
        }
        return false;
    }

    @Override
    public List<User> loadAll() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM users");
        List<User> userList = new ArrayList<>();
        while (resultSet.next()) {
            userList.add(new User(
                    resultSet.getString("id"),
                    resultSet.getString("username"),
                    resultSet.getString("email"),
                    resultSet.getString("password"),
                    resultSet.getString("user_role"),
                    resultSet.getTimestamp("created_at"),
                    resultSet.getTimestamp("last_login")
            ));
        }
        return userList;
    }

    @Override
    public List<User> search(String id) throws SQLException, ClassNotFoundException {
        id = "%" + id + "%";
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM users WHERE id LIKE ? OR username LIKE ? OR email LIKE ?", id, id, id);
        List<User> userList = new ArrayList<>();
        while (resultSet.next()) {
            userList.add(new User(
                    resultSet.getString("id"),
                    resultSet.getString("username"),
                    resultSet.getString("email"),
                    resultSet.getString("password"),
                    resultSet.getString("user_role"),
                    resultSet.getTimestamp("created_at"),
                    resultSet.getTimestamp("last_login")
            ));
        }
        return userList;
    }
}
