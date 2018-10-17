package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import brave.Span;
import brave.Tracer;
import brave.Tracing;

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

		// その他の処理
		Tracer tracer = Tracing.currentTracer();
		Span spanX = tracer.newChild(tracer.currentSpan().context()).name("span-x").start();

		try {
			heavyJob();

		} finally {
			spanX.finish();
		}

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
