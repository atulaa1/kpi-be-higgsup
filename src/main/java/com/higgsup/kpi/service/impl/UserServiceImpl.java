package com.higgsup.kpi.service.impl;

import com.higgsup.kpi.dto.UserDTO;
import com.higgsup.kpi.entity.KpiUser;
import com.higgsup.kpi.glossary.ErrorCode;
import com.higgsup.kpi.glossary.ErrorMessage;
import com.higgsup.kpi.repository.UserRepository;
import com.higgsup.kpi.service.LdapUserService;
import com.higgsup.kpi.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LdapUserService ldapUserService;

    @Override
    public UserDTO getUserDetails(String username) {
        UserDTO user = ldapUserService.getUserDetail(username);
        if (user != null) {
            KpiUser userFromDB = userRepository.findByUserName(username);

            //if has in db then add properties
            if (Objects.nonNull(userFromDB)) {
                BeanUtils.copyProperties(userFromDB, user, "userName",
                        "firstName",
                        "lastName",
                        "fullName",
                        "email");
            }
            return user;
        }
        return null;
    }

    @Transactional
    @Override
    public UserDTO registerUser(String username) {
        UserDTO userDTO = new UserDTO();
        KpiUser user = userRepository.findByUserName(username);
        if (user == null) {
            UserDTO ldapUser = ldapUserService.getUserDetail(username);
            if (ldapUser != null) {
                KpiUser kpiUser = new KpiUser();

                kpiUser.setUserName(ldapUser.getUsername());
                kpiUser.setEmail(ldapUser.getEmail());
                kpiUser.setFirstName(ldapUser.getFirstName());
                kpiUser.setLastName(ldapUser.getLastName());
                kpiUser.setActive(1);

                KpiUser kpiUserCreate = userRepository.save(kpiUser);
                BeanUtils.copyProperties(kpiUserCreate, userDTO);
                userDTO.setUserRole(ldapUser.getUserRole());
            } else {
                userDTO.setErrorCode(ErrorCode.NOT_FIND.getValue());
                userDTO.setMessage(ErrorMessage.NOT_FIND_USER);
            }
        } else {
            userDTO.setErrorCode(ErrorCode.DATA_EXIST.getValue());
            userDTO.setMessage(ErrorMessage.DATA_EXIST_WITH_USER_NAME);
        }
        return userDTO;
    }

    @Transactional
    @Override
    public UserDTO updateInfoUser(String username, UserDTO user) {
        UserDTO userDTO = registerUser(username);
        if (Objects.isNull(userDTO.getErrorCode()) || Objects.equals(userDTO.getErrorCode(), ErrorCode.DATA_EXIST.getValue())) {
            KpiUser kpiUser = userRepository.findByUserName(username);
            if (Objects.nonNull(kpiUser)) {
                BeanUtils.copyProperties(user, kpiUser, "username",
                        "password",
                        "firstName",
                        "lastName",
                        "fullName",
                        "avatar",
                        "email");
                if (Objects.nonNull(user.getAvatar())) {
                    kpiUser.setAvatar(user.getAvatar());
                }
                KpiUser kpiUserRP = userRepository.save(kpiUser);

                BeanUtils.copyProperties(kpiUserRP, user);
                user.setUsername(kpiUserRP.getUserName());

                return user;
            } else {
                userDTO.setErrorCode(ErrorCode.NOT_FIND.getValue());
                userDTO.setMessage(ErrorMessage.NOT_FIND_USER);
                return userDTO;
            }

        } else {
            return userDTO;
        }
    }

}
