package com.joje.palanoto.repository.musicpedia;

import com.joje.palanoto.model.entity.musicpedia.ArtistEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<ArtistEntity, String> {
}