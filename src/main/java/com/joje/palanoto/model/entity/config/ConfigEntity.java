package com.joje.palanoto.model.entity.config;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity(name="tb_config")
public class ConfigEntity {

    @Id
    private String id;

    private String value;

}
