package com.andev.entity;

import com.andev.IntegrationTestBase;
import com.andev.util.HibernateUtil;
import com.andev.util.TestEntity;
import org.assertj.core.api.Assertions;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;

class UserIT extends IntegrationTestBase {

    private static final SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();

    @Test
    void whenSave_thenSaveCorrect() {
        User givenUser = TestEntity.getUserWithoutId();

        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            session.save(givenUser);
            Assertions.assertThat(givenUser.getId()).isNotNull();
            session.getTransaction().rollback();
        }
    }

    @Test
    void whenFindById_thenReturnEntity() {
        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            User actual = session.get(User.class, TestEntity.getGivenId());
            Assertions.assertThat(actual).isEqualTo(TestEntity.getExpectedUser());
            session.getTransaction().rollback();
        }
    }

    @Test
    void whenFindById_thenNotFound() {
        Integer givenId = Integer.MAX_VALUE;
        User actualUser;

        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            actualUser = session.get(User.class, givenId);
            session.getTransaction().commit();
        }

        Assertions.assertThat(actualUser).isNull();
    }

    @Test
    void whenUpdate_thenOk() {
        Integer givenId = 1;
        User givenUser = User.builder()
                .id(1)
                .login("nikolai")
                .password("123456")
                .build();
        String expectedPassword = "123456";
        User actualUser;

        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            session.update(givenUser);
            session.flush();
            session.clear();
            actualUser = session.get(User.class, givenId);
            session.getTransaction().commit();
        }

        Assertions.assertThat(actualUser.getPassword()).isEqualTo(expectedPassword);
    }

    @Test
    void whenDelete_thenOk() {
        Integer givenId = 1;
        User givenUser = User.builder()
                .id(1)
                .login("ivan")
                .password("123")
                .build();
        User actualUser;

        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            session.delete(givenUser);
            session.flush();
            session.clear();
            actualUser = session.get(User.class, givenId);
            session.getTransaction().commit();
        }

        Assertions.assertThat(actualUser).isNull();
    }
}