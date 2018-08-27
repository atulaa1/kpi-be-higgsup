package com.higgsup.kpi.controller;

import com.higgsup.kpi.configure.BaseConfiguration;
import com.higgsup.kpi.dto.Response;
import com.higgsup.kpi.dto.UserDTO;
import com.higgsup.kpi.glossary.ErrorCode;
import com.higgsup.kpi.glossary.ErrorMessage;
import com.higgsup.kpi.service.LdapUserService;
import com.higgsup.kpi.service.UserService;
import com.higgsup.kpi.util.UtilsValidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
public class UserController {

    @Autowired
    private LdapUserService ldapUserService;
    @Autowired
    private UserService userService;

    @PreAuthorize("hasAnyRole('EMPLOYEE')")
    @GetMapping(BaseConfiguration.BASE_API_URL + "/users/{username}")
    public @ResponseBody
    Response getUserInfo(@PathVariable String username) {
        Response response = new Response(HttpStatus.OK.value());
        if (username != null && !username.equals("")) {
            if (!UtilsValidate.containRegex(username)) {
                UserDTO user = userService.getUserDetails(username);
                if (user != null) {
                    response.setData(user);
                } else {
                    response.setMessage(ErrorMessage.NOT_FIND_USER);
                    response.setStatus(ErrorCode.NOT_FIND.getValue());
                }
            }
        }
        return response;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(BaseConfiguration.BASE_API_URL + "/users")
    public Response getListUsers(@RequestParam(value = "name", required = false) String name) {
        Response response = new Response(HttpStatus.OK.value());

        // search by name
        if (Objects.nonNull(name)) {
            if (!UtilsValidate.containRegex(name)) {
                List<UserDTO> listUsersByName = ldapUserService.findUsersByName(name);
                if (!listUsersByName.isEmpty()) {
                    response.setData(listUsersByName);
                    response.setMessage(HttpStatus.OK.getReasonPhrase());
                } else {
                    response.setMessage(ErrorMessage.NOT_FIND_USER);
                    response.setStatus(ErrorCode.NOT_FIND.getValue());
                }
            } else {
                response.setMessage(ErrorMessage.PARAMETERS_NAME_IS_NOT_VALID);
                response.setStatus(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
            }
            return response;
        } else {

            //get all user
            List<UserDTO> listUsers = ldapUserService.getAllUsers();
            if (!listUsers.isEmpty()) {
                response.setData(listUsers);
                response.setMessage(HttpStatus.OK.getReasonPhrase());
            } else {
                response.setMessage(ErrorMessage.NOT_FIND_USER);
                response.setStatus(ErrorCode.NOT_FIND.getValue());
            }
            return response;
        }

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(BaseConfiguration.BASE_API_URL + "/users/{username}/roles")
    public Response updateUserRole(@PathVariable String username, @RequestBody List<String> roles) {
        Response response = new Response(HttpStatus.OK.value());
        if (!CollectionUtils.isEmpty(roles)) {
            UserDTO userDTO = ldapUserService.updateUserRole(username, roles);
            if (Objects.nonNull(userDTO.getErrorCode())) {
                response.setStatus(userDTO.getErrorCode());
                response.setMessage(userDTO.getMessage());
            } else {
                response.setData(userDTO);
            }
        } else {
            response.setStatus(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
            response.setMessage(ErrorMessage.PARAMETERS_ROLES_IS_EMPTY);
        }
        return response;
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @PutMapping(BaseConfiguration.BASE_API_URL + "/users/{username}")
    public Response updateInfo(@PathVariable String username, @RequestBody UserDTO userDTO) {
        Response response = new Response(HttpStatus.OK.value());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //check if token of user
        if (authentication.getPrincipal().equals(username)) {
            UserDTO userDTOUpdate = userService.updateInfoUser(username, userDTO);
            if (Objects.nonNull(userDTOUpdate.getErrorCode())) {
                response.setMessage(userDTOUpdate.getMessage());
                response.setStatus(userDTOUpdate.getErrorCode());
            }
        } else {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setMessage(HttpStatus.FORBIDDEN.getReasonPhrase());
        }
        return response;
    }
}
