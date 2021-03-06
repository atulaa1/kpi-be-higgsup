package com.higgsup.kpi.service;

import java.util.List;

import com.higgsup.kpi.dto.UserDTO;

public interface LdapUserService {
    UserDTO getUserDetail(String username);

    List<UserDTO> getAllUsers();

    List<UserDTO> getAllEmployeeAndManUsers();

    UserDTO updateUserRole(String username, List<String> role);

    List<UserDTO> findUsersByName(String name);

    List<UserDTO> getAllEmployee();
}
