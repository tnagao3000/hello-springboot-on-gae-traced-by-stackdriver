package com.example.demo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloSpringbootController {

	@RequestMapping("/")
	public String index() {

		heavyJob();
		return "top";
	}

	@RequestMapping("/a")
	public String a() {

		heavyJob();
		heavyJob();

		return "A was done.";
	}

	private void heavyJob() {

		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
