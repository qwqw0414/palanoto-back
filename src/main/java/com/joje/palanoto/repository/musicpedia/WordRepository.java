package com.joje.palanoto.repository.musicpedia;

import com.joje.palanoto.model.entity.musicpedia.WordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordRepository extends JpaRepository<WordEntity, String> {
}