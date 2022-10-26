package com.andev.entity;

import com.andev.IntegrationTestBase;
import com.andev.util.HibernateUtil;
import org.assertj.core.api.Assertions;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

class ProductIT extends IntegrationTestBase {

    private static final SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();

    @Test
    void whenSave_thenSaveCorrect() {
        Product givenProduct = Product.builder()
                .name("prod")
                .description("bla")
                .amount(33)
                .price(BigDecimal.ONE)
                .build();
        Product expectedProduct = Product.builder()
                .id(2)
                .name("prod")
                .description("bla")
                .amount(33)
                .price(BigDecimal.ONE)
                .build();
        Integer givenId = 2;
        Product actualProduct;

        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            session.save(givenProduct);
            actualProduct = session.get(Product.class, givenId);
            session.getTransaction().commit();
        }

        Assertions.assertThat(actualProduct).isEqualTo(expectedProduct);
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
        Product actualProduct;

        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            actualProduct = session.get(Product.class, givenId);
            session.getTransaction().commit();
        }

        Assertions.assertThat(actualProduct.getId()).isEqualTo(expectedId);
    }

    @Test
    void whenFindById_thenNotFound() {
        Integer givenId = Integer.MAX_VALUE;
        Product actualProduct;

        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            actualProduct = session.get(Product.class, givenId);
            session.getTransaction().commit();
        }

        Assertions.assertThat(actualProduct).isNull();
    }

    @Test
    void whenUpdate_thenOk() {
        Integer givenId = 1;
        Product givenProduct = Product.builder()
                .id(1)
                .name("new product")
                .build();
        String expectedNameProduct = "new product";
        Product actualProduct;

        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            session.update(givenProduct);
            session.flush();
            session.clear();
            actualProduct = session.get(Product.class, givenId);
            session.getTransaction().commit();
        }

        Assertions.assertThat(actualProduct.getName()).isEqualTo(expectedNameProduct);
    }

    @Test
    void whenDelete_thenOk() {
        Integer givenId = 1;
        Product givenProduct = Product.builder()
                .id(1)
                .build();
        Product actualProduct;

        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            session.delete(givenProduct);
            session.flush();
            session.clear();
            actualProduct = session.get(Product.class, givenId);
            session.getTransaction().commit();
        }

        Assertions.assertThat(actualProduct).isNull();
    }

}