package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class HelloSpringbootController3 {

	@Autowired
	RestTemplate restTemplate;

	/**
	 * RestTemplateのテスト
	 */
	@RequestMapping("/c")
	public String c() {

		// RestTemplateを使った呼出
		String c2Result = restTemplate.getForObject("http://localhost:8080/c2", String.class);

		// トレース結果を見た感じではDIされたRestTemplate自体もSpanを作ってるっぽい!?
		// restTemplate.getForObject("https://www.yahoo.co.jp", String.class);

		return c2Result + " : C was done.";
	}

	@RequestMapping("/c2")
	public String c2() {

		heavyJob();
		return "C2 was done.";
	}

	private void heavyJob() {

		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
