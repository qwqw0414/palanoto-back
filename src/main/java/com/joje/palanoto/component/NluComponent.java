package com.joje.palanoto.component;

import com.joje.palanoto.common.constants.PoSType;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component("NluComponent")
public class NluComponent {

    private final Komoran komoran = new Komoran(DEFAULT_MODEL.LIGHT);

    public List<String> get(String str, PoSType type) {
        List<String> result = new ArrayList<>();

        KomoranResult komoranResult = komoran.analyze(str);
        List<Token> tokens = komoranResult.getTokenList();

        for(Token token : tokens){
            if(token.getPos().startsWith(type.toString())) {
                if(!"에".equals(token.getMorph())){
                    log.debug("[token]=[{}]", token);
                    result.add(token.getMorph());
                }
            }
        }
        return result;
    }

    public List<String> get(List<String> list, PoSType type) {
        List<String> result = new ArrayList<>();
        if(list != null){
            for(String str : list) {
                result.addAll(this.get(str, type));
            }
        }
        return result;
    }
}
