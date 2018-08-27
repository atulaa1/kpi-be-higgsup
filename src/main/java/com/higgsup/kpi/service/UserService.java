package com.higgsup.kpi.service;

import com.higgsup.kpi.dto.UserDTO;
import com.higgsup.kpi.entity.KpiUser;

public interface UserService {
    UserDTO getUserDetails(String username);

    UserDTO registerUser(String username);

    UserDTO updateInfoUser(String username, UserDTO user);
}
