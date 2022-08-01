package com.joje.palanoto.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultVo {

    Map<String, Object> data = new HashMap<>();

    public void put(String key, Object value) {
        log.debug("[{}]=[{}]", key, value);
        data.put(key, value);
    }

}
