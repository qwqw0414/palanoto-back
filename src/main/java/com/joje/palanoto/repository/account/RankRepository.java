package com.joje.palanoto.repository.account;

import com.joje.palanoto.model.entity.musicpedia.RankEntity;
import com.joje.palanoto.model.entity.musicpedia.RankKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RankRepository extends JpaRepository<RankEntity, RankKey> {
}