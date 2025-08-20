package com.example.billingsystem.bo.custom.impl;

import com.example.billingsystem.bo.custom.UserBo;
import com.example.billingsystem.dao.DaoFactory;
import com.example.billingsystem.dao.custom.UserDao;
import com.example.billingsystem.dto.UserDto;
import com.example.billingsystem.entity.User;
import com.example.billingsystem.util.PasswordManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserBoImpl implements UserBo {

    private final UserDao userDao = DaoFactory.getInstance().getDao(DaoFactory.DaoType.USER);

    @Override
    public boolean createUser(UserDto userDto) throws SQLException, ClassNotFoundException {
        Logger.getLogger(UserBoImpl.class.getName()).log(Level.INFO, "UserBo Executing SQL with ID: " + userDto.getId());

        // Check if email already exists
        if (userDao.existsByEmail(userDto.getEmail())) {
            Logger.getLogger(UserBoImpl.class.getName()).log(Level.WARNING, "Email already exists: " + userDto.getEmail());
            return false;
        }

        // Check if username already exists (only if username is provided)
        if (userDto.getUsername() != null && !userDto.getUsername().trim().isEmpty() &&
            userDao.existsByUsername(userDto.getUsername())) {
            Logger.getLogger(UserBoImpl.class.getName()).log(Level.WARNING, "Username already exists: " + userDto.getUsername());
            return false;
        }

        return userDao.create(new User(
                userDto.getId(), userDto.getUsername(), userDto.getEmail(), userDto.getPassword(), userDto.getRole(), userDto.getCreatedAt(), userDto.getLastLogin()
        ));
    }

    @Override
    public List<UserDto> searchUser(String id) throws SQLException, ClassNotFoundException {
        List<UserDto> userDtoList = new ArrayList<>();
        for (User user: userDao.search(id)) {
            userDtoList.add(new UserDto(
                    user.getId(), user.getUsername(), user.getEmail(), user.getPassword(), user.getRole(), user.getCreatedAt(), user.getLastLogin()
            ));
        }
        return userDtoList;
    }

    @Override
    public boolean deleteUser(String id) throws SQLException, ClassNotFoundException {
        return userDao.delete(id);
    }

    @Override
    public boolean updateUser(UserDto userDto) throws SQLException, ClassNotFoundException {
        return userDao.update(new User(
                userDto.getId(), userDto.getUsername(), userDto.getEmail(), userDto.getPassword(), userDto.getRole(), userDto.getCreatedAt(), userDto.getLastLogin()
        ));
    }

    @Override
    public List<UserDto> loadAllUsers() throws SQLException, ClassNotFoundException {
        List<UserDto> userDtoList = new ArrayList<>();
        for (User user : userDao.loadAll()) {
            userDtoList.add(new UserDto(
                    user.getId(), user.getUsername(), user.getEmail(), user.getPassword(), user.getRole(), user.getCreatedAt(), user.getLastLogin()
            ));
        }
        return userDtoList;
    }

    @Override
    public Optional<UserDto> authenticateUser(String usernameOrEmail, String password) throws SQLException, ClassNotFoundException {
        User user = userDao.findByUsernameOrEmail(usernameOrEmail);
        if (user != null && PasswordManager.checkPassword(password, user.getPassword())) {
            UserDto userDto = new UserDto(
                    user.getId(), user.getUsername(), user.getEmail(), user.getPassword(), user.getRole(), user.getCreatedAt(), user.getLastLogin()
            );
            return Optional.of(userDto);
        }
        return Optional.empty();
    }

    @Override
    public UserDto authenticate(String usernameOrEmail, String password) throws SQLException, ClassNotFoundException {
        User user = userDao.findByUsernameOrEmail(usernameOrEmail);
        if (user != null && PasswordManager.checkPassword(password, user.getPassword())) {
            UserDto userDTO = new UserDto();
            userDTO.setId(user.getId());
            userDTO.setUsername(user.getUsername());
            userDTO.setEmail(user.getEmail());
            userDTO.setRole(user.getRole());
            userDTO.setCreatedAt(user.getCreatedAt());
            userDTO.setLastLogin(user.getLastLogin());
            return userDTO;
        }
        return null;
    }
}
