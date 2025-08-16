package com.example.billingsystem.dao.custom;

import com.example.billingsystem.dao.CrudDao;
import com.example.billingsystem.entity.User;

import java.sql.SQLException;
import java.util.List;

public interface UserDao extends CrudDao<User, String> {
    User findByUsername(String username) throws SQLException, ClassNotFoundException;
    List<User> loadAll() throws SQLException, ClassNotFoundException;
    List<User> search(String id) throws SQLException, ClassNotFoundException;
}
