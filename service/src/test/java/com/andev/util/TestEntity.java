package com.andev.util;

import com.andev.entity.User;
import com.andev.entity.enums.Role;
import lombok.experimental.UtilityClass;

import javax.persistence.criteria.CriteriaBuilder;

@UtilityClass
public class TestEntity {

    public static User getUserWithoutId (){
        return User.builder()
                .login("Petr")
                .password("333")
                .role(Role.CUSTOMER)
                .build();
    }

    public static User getExpectedUser(){
        return User.builder()
                .id(1)
                .login("ivan")
                .password("123")
                .role(Role.ADMIN)
                .build();
    }

    public static Integer getGivenId(){
        return 1;
    }


}
