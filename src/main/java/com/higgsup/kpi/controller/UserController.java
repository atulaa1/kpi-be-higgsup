package com.higgsup.kpi.controller;

import com.higgsup.kpi.configure.BaseConfiguration;
import com.higgsup.kpi.dto.Response;
import com.higgsup.kpi.dto.UserDTO;
import com.higgsup.kpi.glossary.ErrorCode;
import com.higgsup.kpi.service.LdapUserService;
import com.higgsup.kpi.util.UtilsValidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class UserController {

    @Autowired
    private LdapUserService ldapUserService;

    @PreAuthorize("hasAnyRole('EMPLOYEE')")
    @GetMapping(BaseConfiguration.BASE_API_URL + "/user-info")
    public @ResponseBody
    Response getUserInfo() {
        Response response = new Response(200);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        if (username != null && !username.equals("")) {
            if (!UtilsValidate.containRegex(username)) {
                UserDTO user = ldapUserService.getUserDetail(username);
                if (user != null) {
                    response.setData(user);
                } else {
                    response.setMessage(ErrorCode.NOT_FIND_USER.getContent());
                    response.setStatus(ErrorCode.NOT_FIND_USER.getValue());
                }
            }
        }
        return response;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(BaseConfiguration.BASE_API_URL + "/users")
    public @ResponseBody
    Response getListUsers() {
        Response response = new Response(200);
        List<UserDTO> listUsers = ldapUserService.getAllUsers();
        if (!listUsers.isEmpty()) {
            response.setData(listUsers);
            response.setMessage(HttpStatus.OK.getReasonPhrase());
        } else {
            response.setMessage(ErrorCode.NOT_FIND_USER.getContent());
            response.setStatus(ErrorCode.NOT_FIND_USER.getValue());
        }
        return response;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(BaseConfiguration.BASE_API_URL + "/user-role/{username}")
    public @ResponseBody
    List<Map<String, Object>> updateUserRole(@RequestBody Map<String, Object> context) {
        String newRole = (String) context.get("role");
        return null;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(BaseConfiguration.BASE_API_URL + "/search-users/{name}")
    public @ResponseBody
    Response searchUsers(@PathVariable String name) {
        Response response = new Response(200);
        if (name != null && !name.equals("")) {
            if (!UtilsValidate.containRegex(name)) {
                List<UserDTO> listUsersByName = ldapUserService.findUsersByName(name);
                if (!listUsersByName.isEmpty()) {
                    response.setData(listUsersByName);
                    response.setMessage(HttpStatus.OK.getReasonPhrase());
                } else {
                    response.setMessage(ErrorCode.NOT_FIND_USER.getContent());
                    response.setStatus(ErrorCode.NOT_FIND_USER.getValue());
                }
            } else {
                response.setMessage(ErrorCode.PARAMETERS_IS_NOT_VALID.getContent());
                response.setStatus(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
            }
        } else {
            response.setMessage(ErrorCode.PARAMETERS_IS_MISSING.getContent());
            response.setStatus(ErrorCode.PARAMETERS_IS_MISSING.getValue());
        }
        return response;
    }

}
