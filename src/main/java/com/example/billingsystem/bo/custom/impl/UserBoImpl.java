package com.example.billingsystem.bo.custom.impl;

import com.example.billingsystem.bo.custom.UserBo;
import com.example.billingsystem.dao.DaoFactory;
import com.example.billingsystem.dao.custom.UserDao;
import com.example.billingsystem.dto.UserDto;
import com.example.billingsystem.entity.User;
import com.example.billingsystem.util.PasswordManager;
import com.example.billingsystem.util.KeyGenerator;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserBoImpl implements UserBo {

    private final UserDao userDao = DaoFactory.getInstance().getDao(DaoFactory.DaoType.USER);

    @Override
    public boolean createUser(UserDto userDto) throws SQLException, ClassNotFoundException {
        // Generate ID if not provided
        if (userDto.getId() == null || userDto.getId().trim().isEmpty()) {
            userDto.setId(KeyGenerator.generateId());
        }

        // Set created_at timestamp if not provided
        if (userDto.getCreatedAt() == null) {
            userDto.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        }

        // Validate role enum values according to schema
        if (userDto.getRole() != null) {
            String role = userDto.getRole().toLowerCase();
            if (!role.equals("admin") && !role.equals("staff")) {
                userDto.setRole("staff"); // Default to staff if invalid role
            }
        } else {
            userDto.setRole("staff"); // Default role from schema
        }

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
                userDto.getId(),
                userDto.getUsername(),
                userDto.getEmail(),
                userDto.getPassword(),
                userDto.getRole(),
                userDto.getCreatedAt(),
                userDto.getLastLogin()
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

    @Override
    public UserDto findUserById(String id) throws SQLException, ClassNotFoundException {
        User user = userDao.findById(id);
        if (user != null) {
            return new UserDto(
                    user.getId(), user.getUsername(), user.getEmail(), user.getPassword(), user.getRole(), user.getCreatedAt(), user.getLastLogin()
            );
        }
        return null;
    }

    @Override
    public UserDto getUserByEmail(String email) throws SQLException, ClassNotFoundException {
        User user = userDao.findByEmail(email);
        if (user != null) {
            return new UserDto(
                    user.getId(), user.getUsername(), user.getEmail(), user.getPassword(), user.getRole(), user.getCreatedAt(), user.getLastLogin()
            );
        }
        return null;
    }

    @Override
    public boolean updatePassword(String email, String newPassword) throws SQLException, ClassNotFoundException {
        String hashedPassword = PasswordManager.encryptPassword(newPassword);
        return userDao.updatePasswordByEmail(email, hashedPassword);
    }

    @Override
    public boolean updatePassword(String userId, String currentPassword, String newPassword) throws SQLException, ClassNotFoundException {
        // First verify the current password
        User user = userDao.findById(userId);
        if (user == null || !PasswordManager.checkPassword(currentPassword, user.getPassword())) {
            return false;
        }

        // Update with new password
        String hashedPassword = PasswordManager.encryptPassword(newPassword);
        return userDao.updatePasswordById(userId, hashedPassword);
    }
}
