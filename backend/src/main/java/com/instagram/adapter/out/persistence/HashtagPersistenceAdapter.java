package com.instagram.adapter.out.persistence;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.instagram.adapter.out.persistence.entity.HashtagJpaEntity;
import com.instagram.adapter.out.persistence.repository.HashtagJpaRepository;
import com.instagram.domain.model.Hashtag;
import com.instagram.domain.port.out.HashtagRepository;

@Component
public class HashtagPersistenceAdapter implements HashtagRepository {

    private final HashtagJpaRepository jpaRepository;

    public HashtagPersistenceAdapter(HashtagJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Hashtag> findByName(String name) {
        return jpaRepository.findByName(name).map(this::toDomain);
    }

    @Override
    public Hashtag save(Hashtag hashtag) {
        return toDomain(jpaRepository.save(toEntity(hashtag)));
    }

    @Override
    public Hashtag findOrCreate(String name) {
        Optional<HashtagJpaEntity> e = jpaRepository.findByName(name);
        if (e.isEmpty()) {
            return save(Hashtag.builder().id(null).name(name).postCount(0).build());
        }
        return toDomain(e.get());
    }

    private HashtagJpaEntity toEntity(Hashtag hashtag) {
        return HashtagJpaEntity.builder()
                .id(hashtag.getId())
                .name(hashtag.getName())
                .postCount(hashtag.getPostCount())
                .build();
    }

    private Hashtag toDomain(HashtagJpaEntity e) {
        return Hashtag.builder()
                .id(e.getId())
                .name(e.getName())
                .postCount(e.getPostCount())
                .build();
    }

}
