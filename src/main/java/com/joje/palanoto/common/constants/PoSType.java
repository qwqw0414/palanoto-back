package com.joje.palanoto.common.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PoSType {

    NN("명사"),
    NNG("일반명사"),
    NNP("고유명사"),
    NNB("의존명사"),
    NP("대명사"),
    NR("수사"),
    VV("동사");

    private final String title;
}
