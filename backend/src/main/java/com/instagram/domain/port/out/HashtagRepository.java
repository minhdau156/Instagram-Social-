package com.instagram.domain.port.out;

import java.util.Optional;

import com.instagram.domain.model.Hashtag;

public interface HashtagRepository {

    Optional<Hashtag> findByName(String name);

    Hashtag save(Hashtag hashtag);

    Hashtag findOrCreate(String name);
}
