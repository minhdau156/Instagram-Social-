package com.instagram.adapter.out.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import com.instagram.adapter.out.persistence.entity.BaseJpaEntity;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.flyway.enabled=false",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@Import(com.instagram.infrastructure.config.JpaConfig.class)
class BaseJpaEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    @Entity
    @Getter
    @Setter
    public static class DummyEntity extends BaseJpaEntity {
        @Id
        @GeneratedValue
        private Long id;
        private String name;
    }

    @Test
    void shouldPopulateAuditFieldsOnSave() {
        // Arrange
        DummyEntity entity = new DummyEntity();
        entity.setName("test");

        // Act
        DummyEntity savedEntity = entityManager.persistFlushFind(entity);

        // Assert
        assertThat(savedEntity.getCreatedAt()).isNotNull();
        assertThat(savedEntity.getUpdatedAt()).isNotNull();
        assertThat(savedEntity.getId()).isNotNull();
    }

    @Test
    void shouldUpdateUpdatedAtFieldOnModification() throws InterruptedException {
        // Arrange
        DummyEntity entity = new DummyEntity();
        entity.setName("test");

        DummyEntity savedEntity = entityManager.persistFlushFind(entity);
        var initialUpdatedAt = savedEntity.getUpdatedAt();

        // Act
        Thread.sleep(10);
        savedEntity.setName("updated");
        DummyEntity updatedEntity = entityManager.persistFlushFind(savedEntity);

        // Assert
        assertThat(updatedEntity.getUpdatedAt()).isAfter(initialUpdatedAt);
    }
}
