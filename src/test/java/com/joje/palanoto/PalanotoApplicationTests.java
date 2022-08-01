package com.joje.palanoto;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PalanotoApplicationTests {

//	@Test
//	void contextLoads() {
//	}

//	@Test
//	void jasypt() {
//
//		String url = "jdbc:log4jdbc:mariadb://000.000.000.1:0000/dbee?characterEncoding=UTF-8&serverTimezone=UTC";
//		String username = "";
//		String password = "";
//
//		System.out.println(jasyptEncoding(url));
//		System.out.println(jasyptEncoding(username));
//		System.out.println(jasyptEncoding(password));
//	}
//
//	public String jasyptEncoding(String value) {
//		String key = "joje_0414_95";
//		StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
//		pbeEnc.setAlgorithm("PBEWithMD5AndDES");
//		pbeEnc.setPassword(key);
//		return pbeEnc.encrypt(value);
//	}

}
