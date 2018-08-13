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

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class UserController {

    @Autowired
    private LdapUserService ldapUserService;

    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping(BaseConfiguration.BASE_API_URL + "/users/{username}")
    public @ResponseBody Response getUserInfo(@PathVariable String username) {
        Response response = new Response(200);
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
    public Response getListUsers(@RequestParam(value = "name", required = false) String name) {
        Response response = new Response(200);

        if (Objects.nonNull(name)) {
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
            return response;
        }

        List<UserDTO> listUsers = ldapUserService.getAllUsers();
        if (!listUsers.isEmpty()) {
            response.setData(listUsers);
            response.setMessage(HttpStatus.OK.getReasonPhrase());
            return response;
        } else {
            response.setMessage(ErrorCode.NOT_FIND_USER.getContent());
            response.setStatus(ErrorCode.NOT_FIND_USER.getValue());
            return response;
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(BaseConfiguration.BASE_API_URL + "/users/{username}/roles")
    public @ResponseBody
    List<Map<String, Object>> updateUserRole(@RequestBody Map<String, Object> context) {
        String newRole = (String) context.get("role");
        return null;
    }

}
