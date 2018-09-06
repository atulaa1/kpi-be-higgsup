package com.higgsup.kpi.service.impl;

import com.higgsup.kpi.dto.UserDTO;
import com.higgsup.kpi.glossary.ErrorCode;
import com.higgsup.kpi.glossary.ErrorMessage;
import com.higgsup.kpi.service.LdapUserService;
import com.higgsup.kpi.util.UserAttributesMapper;
import com.higgsup.kpi.util.UtilsLdap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.stereotype.Service;

import javax.naming.Name;
import java.util.*;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Service
public class LdapUserServiceImpl implements LdapUserService {

    @Autowired
    private Environment env;

    @Autowired
    private LdapTemplate ldapTemplate;

    @Override
    public UserDTO getUserDetail(String username) {
        UserDTO user = new UserDTO();
        LdapQuery query = query().where("objectclass").is("user").and("sAMAccountName").is(username);
        List<UserDTO> queryResult = ldapTemplate.search(query, new UserAttributesMapper());
        if (!queryResult.isEmpty()) {
            user = queryResult.get(0);
            return user;
        } else {
            return null;
        }
    }

    @Override
    public List<UserDTO> getAllUsers() {
        LdapQuery query = query().where("objectclass").is("user").and("roleForKpi").isPresent();
        List<UserDTO> result = ldapTemplate.search(query, new UserAttributesMapper());
        result.sort(Comparator.comparing(UserDTO::getUsername));
        List<UserDTO> userDTOList = new ArrayList<>();

        for (UserDTO userDTO : result){
            if (!userDTO.getUserRole().contains("ROLE_ADMIN") && userDTO.getUserRole().contains("ROLE_MAN")){
                userDTOList.add(userDTO);
            }
        }

        for (UserDTO userDTO : result){
            if (!userDTO.getUserRole().contains("ROLE_ADMIN") && !userDTO.getUserRole().contains("ROLE_MAN")){
                userDTOList.add(userDTO);
            }
        }

        return userDTOList;
    }

    @Override
    public UserDTO updateUserRole(String username, List<String> roles) {
        String rolesJoin = UtilsLdap.convertRole(roles);
        UserDTO user = getUserDetail(username);
        if (Objects.isNull(user)) {
            user = new UserDTO();
            user.setErrorCode(ErrorCode.NOT_FIND.getValue());
            user.setMessage(ErrorMessage.NOT_FIND_USER);
        } else {
            Name dn = UtilsLdap.buildDn(user);
            DirContextOperations context = ldapTemplate.lookupContext(dn);
            context.setAttributeValue("roleForKPI", rolesJoin);
            ldapTemplate.modifyAttributes(context);
        }
        user.setUserRole(roles);
        return user;
    }

    @Override
    public List<UserDTO> findUsersByName(String name) {
        List<UserDTO> result = null;
        LdapQuery query = query().where("objectclass").is("user").and("roleForKpi").isPresent().and("displayName")
                .like("*" + name + "*");
        result = ldapTemplate.search(query, new UserAttributesMapper());
        return result;
    }

}
