package com.andev.entity;

import com.andev.IntegrationTestBase;
import com.andev.util.HibernateUtil;
import org.assertj.core.api.Assertions;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

class UserInfoIT extends IntegrationTestBase {

    private static final SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();

    @Test
    void whenSave_thenSaveCorrect() {
        UserInfo givenUserInfo = UserInfo.builder()
                .firstName("Ivan")
                .lastName("Ivanov")
                .email("ivan@gmail.com")
                .phone("7777777")
                .build();
        UserInfo expectedUserInfo = UserInfo.builder()
                .id(2)
                .firstName("Ivan")
                .lastName("Ivanov")
                .email("ivan@gmail.com")
                .phone("7777777")
                .build();
        Integer givenId = 2;
        UserInfo actualUserInfo;

        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            session.save(givenUserInfo);
            session.flush();
            session.clear();
            actualUserInfo = session.get(UserInfo.class, givenId);
            Assertions.assertThat(actualUserInfo).isEqualTo(expectedUserInfo);
            session.getTransaction().rollback();
        }
    }

    @Test
    void whenSave_thenTrowException() {
        Throwable thrown = catchThrowable(() -> {
            try (Session session = sessionFactory.openSession()) {
                session.getTransaction().begin();
                session.save(null);
                session.getTransaction().commit();
            }
        });

        Assertions.assertThat(thrown).isInstanceOf(RuntimeException.class);
    }

    @Test
    void whenFindById_thenReturnEntity() {
        Integer givenId = 1;
        Integer expectedId = 1;
        UserInfo actualUserInfo;

        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            actualUserInfo = session.get(UserInfo.class, givenId);
            session.getTransaction().commit();
        }

        Assertions.assertThat(actualUserInfo.getId()).isEqualTo(expectedId);
    }

    @Test
    void whenFindById_thenNotFound() {
        Integer givenId = Integer.MAX_VALUE;
        UserInfo actualUserInfo;

        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            actualUserInfo = session.get(UserInfo.class, givenId);
            session.getTransaction().commit();
        }

        Assertions.assertThat(actualUserInfo).isNull();
    }

    @Test
    void whenUpdate_thenOk() {
        Integer givenId = 1;
        UserInfo givenUserInfo = UserInfo.builder()
                .id(1)
                .firstName("Ivan")
                .lastName("Petrov")
                .email("ivan@gmail.com")
                .phone("7777777")
                .build();
        String expectedLastName = "Petrov";
        UserInfo actualUserInfo;

        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            session.update(givenUserInfo);
            session.flush();
            session.clear();
            actualUserInfo = session.get(UserInfo.class, givenId);
            session.getTransaction().commit();
        }

        Assertions.assertThat(actualUserInfo.getLastName()).isEqualTo(expectedLastName);
    }

    @Test
    void whenDelete_thenOk() {
        Integer givenId = 1;
        UserInfo givenUserInfo = UserInfo.builder()
                .id(1)
                .firstName("Ivan")
                .lastName("Ivanov")
                .email("ivan@gmail.com")
                .phone("7777777")
                .build();
        UserInfo actualUserInfo;

        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            session.delete(givenUserInfo);
            session.flush();
            session.clear();
            actualUserInfo = session.get(UserInfo.class, givenId);
            session.getTransaction().commit();
        }

        Assertions.assertThat(actualUserInfo).isNull();
    }

}