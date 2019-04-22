package example.spring.webflux.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import example.spring.webflux.model.Person;
import jp.co.future.uroborosql.config.SqlConfig;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;

@RestController
@RequestMapping("/api/persons")
public class PersonController {
	@Autowired
	private SqlConfig uroboroSQLConfig;

	@Autowired
	@Qualifier("jdbcScheduler")
	private Scheduler jdbcScheduler;

	@GetMapping
	public Flux<Person> all() {
		return Flux.<Person> create(sink -> {
			try (var agent = uroboroSQLConfig.agent()) {
				// テーブルをSELECTしてPersonクラスにMappingしたStreamを生成
				agent.queryWith("SELECT * FROM PERSON ORDER BY ID")
						.stream(Person.class)
						// キャンセルされていたら終了させる
						.dropWhile(p -> sink.isCancelled())
						// FluxSinkにエンティティを渡す
						.forEach(sink::next);

				if (!sink.isCancelled()) {
					// 終了を通知
					sink.complete();
				}
			}
		}).subscribeOn(jdbcScheduler);
	}

	@GetMapping("/sync")
	public List<Person> synk() {
		try (var agent = uroboroSQLConfig.agent()) {
			return agent.queryWith("SELECT * FROM PERSON ORDER BY ID")
					.collect(Person.class);
		}
	}
}
