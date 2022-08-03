package com.joje.palanoto.component;

import com.joje.palanoto.exception.HttpRequestException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component("HttpRequestComponent")
public class HttpRequestComponent {

    public Document requestHtml(String uri) {
        try {
            return Jsoup.connect(uri).get();
        } catch (IOException e) {
            throw new HttpRequestException("http 요청 실패 : [uri]=[" + uri + "]");
        }
    }

}
