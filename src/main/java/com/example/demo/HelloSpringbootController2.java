package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import brave.Span;
import brave.Tracer;
import brave.Tracer.SpanInScope;

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
		Span spanAll = tracer.nextSpan().name("span-overall").start();

		try (SpanInScope wsAll = tracer.withSpanInScope(spanAll)) {

			// 前半(spanFirst)
			spanAll.annotate("annotate first-half");
			Span spanFirst = tracer.nextSpan().name("span-first-half").start();

			try (SpanInScope wsFirst = tracer.withSpanInScope(spanFirst)) {
				heavyJob();
			} finally {
				spanFirst.finish();
			}

			// 後半(spanSecond)
			spanAll.annotate("annotate second-half");
			Span spanSecond = tracer.nextSpan().name("span-second-half").start();

			try (SpanInScope wsSecond = tracer.withSpanInScope(spanSecond)) {
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
