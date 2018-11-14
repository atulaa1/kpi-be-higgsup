package com.higgsup.kpi.service.impl;

import com.higgsup.kpi.dto.UserDTO;
import com.higgsup.kpi.entity.KpiLateTimeCheck;
import com.higgsup.kpi.glossary.ErrorCode;
import com.higgsup.kpi.glossary.ErrorMessage;
import com.higgsup.kpi.repository.KpiLateTimeCheckRepo;
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Service
public class LdapUserServiceImpl implements LdapUserService {

    @Autowired
    private Environment environment;

    @Autowired
    private LdapTemplate ldapTemplate;

    @Autowired
    private KpiLateTimeCheckRepo lateTimeCheckRepo;

    @Override
    public UserDTO getUserDetail(String username) {
        LdapQuery query = query().where("objectclass").is("user").and("sAMAccountName").is(username);
        List<UserDTO> queryResult = ldapTemplate.search(query, new UserAttributesMapper());
        if (!queryResult.isEmpty()) {
            return queryResult.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<UserDTO> getAllUsers() {
        LdapQuery query = query().where("objectclass").is("user").and("roleForKpi").isPresent();
        List<UserDTO> result = ldapTemplate.search(query, new UserAttributesMapper());

        List<UserDTO> userDTOList = new ArrayList<>();
        for (UserDTO userDTO : result) {
            if (!userDTO.getUserRole().contains("ROLE_ADMIN")) {
                userDTOList.add(userDTO);
            }
        }

        userDTOList = userDTOList.stream()
                .sorted(Comparator.<UserDTO>comparingInt(u -> u.getUserRole().contains("ROLE_MAN") ? 0 : 1)
                        .thenComparing(UserDTO::getUsername))
                .collect(Collectors.toList());
        return userDTOList;
    }

    @Override
    public List<UserDTO> getAllEmployeeAndManUsers() {
        LdapQuery query = query().where("objectclass").is("user").and("roleForKpi").isPresent()
                .and("roleForKpi").not().like("*admin,*");
        return ldapTemplate.search(query, new UserAttributesMapper());
    }

    public List<UserDTO> getAllEmployee(){
        LdapQuery query = query().where("objectclass").is("user").and("roleForKpi").is("employee");
        return ldapTemplate.search(query, new UserAttributesMapper());
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

        if(roles.contains("ROLE_MAN")){
            lateTimeCheckRepo.deleteLateTimeCheck(username);
        }
        return user;
    }

    @Override
    public List<UserDTO> findUsersByName(String name) {
        LdapQuery query = query().where("objectclass").is("user").and("roleForKpi").isPresent().and("displayName")
                .like("*" + name + "*");
        return ldapTemplate.search(query, new UserAttributesMapper());
    }

}
