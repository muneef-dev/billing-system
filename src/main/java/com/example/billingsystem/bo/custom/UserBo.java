package com.example.billingsystem.bo.custom;

import com.example.billingsystem.bo.SuperBo;
import com.example.billingsystem.dto.UserDto;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UserBo extends SuperBo {
    boolean createUser(UserDto userDto) throws SQLException, ClassNotFoundException;
    List<UserDto> searchUser(String id) throws SQLException, ClassNotFoundException;
    boolean deleteUser(String id) throws SQLException, ClassNotFoundException;
    boolean updateUser(UserDto userDto) throws SQLException, ClassNotFoundException;
    List<UserDto> loadAllUsers() throws SQLException, ClassNotFoundException;
    Optional<UserDto> authenticateUser(String usernameOrEmail, String password) throws SQLException, ClassNotFoundException;
    UserDto authenticate(String usernameOrEmail, String password) throws SQLException, ClassNotFoundException;
    UserDto findUserById(String id) throws SQLException, ClassNotFoundException;
}
