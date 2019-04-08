package com.higgsup.kpi.service;

import com.higgsup.kpi.dto.UserDTO;
import com.higgsup.kpi.entity.KpiUser;

import java.util.List;

public interface UserService {
    UserDTO getUserDetails(String username);

    List<UserDTO> getAllEmployeeAndManUsers(String... ignoreProperties);

    UserDTO registerUser(String username);

    UserDTO updateInfoUser(String username, UserDTO user);

    List<UserDTO> getAllEmployee();
}
