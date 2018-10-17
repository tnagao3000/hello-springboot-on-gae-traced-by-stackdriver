package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import brave.Span;
import brave.Tracer;

@RestController
public class HelloSpringbootController2 {

	@Autowired
	Tracer tracer;

	/**
	 * ネストしたSpanとannotateとtagのテスト
	 */
	@RequestMapping("/b")
	public String b() {

		// 全体(spanAll)
		Span spanAll = tracer.newChild(tracer.currentSpan().context()).name("span-overall").start();

		try {

			// 前半(spanFirst)
			spanAll.annotate("annotate first-half");
			Span spanFirst = tracer.newChild(spanAll.context()).name("span-first-half").start();

			try {
				heavyJob();
			} finally {
				spanFirst.finish();
			}

			// 後半(spanSecond)
			spanAll.annotate("annotate second-half");
			Span spanSecond = tracer.newChild(spanAll.context()).name("span-second-half").start();

			try {
				spanSecond.tag("second-key-a", "val-a");
				spanSecond.tag("second-key-b", "val-b");

				heavyJob();

			} finally {
				spanSecond.finish();
			}

		} finally {
			spanAll.finish();
		}

		return "B was done.";
	}

	private void heavyJob() {

		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
