package example.spring.webflux.config;

import java.util.Random;
import java.util.stream.IntStream;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import example.spring.webflux.model.Person;
import jp.co.future.uroborosql.SqlAgent;
import jp.co.future.uroborosql.SqlAgentFactory;
import jp.co.future.uroborosql.SqlAgentFactoryImpl;
import jp.co.future.uroborosql.UroboroSQL;
import jp.co.future.uroborosql.config.SqlConfig;
import jp.co.future.uroborosql.context.SqlContextFactory;
import jp.co.future.uroborosql.context.SqlContextFactoryImpl;
import jp.co.future.uroborosql.filter.SqlFilterManager;
import jp.co.future.uroborosql.store.SqlLoader;
import jp.co.future.uroborosql.store.SqlLoaderImpl;
import jp.co.future.uroborosql.store.SqlManagerImpl;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class DBConfig {

	@Bean
	public SqlConfig sqlConfig(final DataSource dataSource) {

		SqlLoader sqlLoader = new SqlLoaderImpl("sql", ".sql");
		sqlLoader.setSqlEncoding("UTF-8");

		SqlAgentFactory sqlAgentFactory = new SqlAgentFactoryImpl();

		SqlContextFactory sqlContextFactory = new SqlContextFactoryImpl();

		SqlConfig sqlConfig = UroboroSQL.builder(dataSource)
				.setSqlManager(new SqlManagerImpl(sqlLoader))
				.setSqlAgentFactory(sqlAgentFactory)
				.setSqlContextFactory(sqlContextFactory)
				.build();

		SqlFilterManager sqlFilterManager = sqlConfig.getSqlFilterManager();
		sqlFilterManager.initialize();

		return sqlConfig;
	}

	private static String[] fnames = { "Sophia", "Emma", "Olivia", "Isabella", "Ava", "Mia", "Emily", "Abigail",
			"Madison",
			"Elizabeth", "Charlotte", "Avery", "Sofia", "Chloe", "Ella", "Harper", "Amelia", "Aubrey",
			"Addison", "Evelyn", "Natalie", "Grace", "Hannah", "Zoey", "Victoria", "Lillian", "Lily",
			"Brooklyn", "Samantha", "Layla", "Zoe", "Audrey", "Leah", "Allison", "Anna", "Aaliyah",
			"Savannah", "Gabriella", "Camila", "Aria", "Noah", "Liam", "Jacob", "Mason", "William", "Ethan",
			"Michael", "Alexander", "Jayden", "Daniel", "Elijah", "Aiden", "James", "Benjamin", "Matthew",
			"Jackson", "Logan", "David", "Anthony", "Joseph", "Joshua", "Andrew", "Lucas", "Gabriel",
			"Samuel", "Christopher", "John", "Dylan", "Isaac", "Ryan", "Nathan", "Carter", "Caleb", "Luke",
			"Christian", "Hunter", "Henry", "Owen", "Landon", "Jack" };
	private static String[] lnames = { "Smith", "Johnson", "Williams", "Brown", "Jones", "Miller", "Davis", "Garcia",
			"Rodriguez", "Wilson", "Martinez", "Anderson", "Taylor", "Thomas", "Hernandez", "Moore",
			"Martin", "Jackson", "Thompson", "White", "Lopez", "Lee", "Gonzalez", "Harris", "Clark",
			"Lewis", "Robinson", "Walker", "Perez", "Hall", "Young", "Allen", "Sanchez", "Wright", "King",
			"Scott", "Green", "Baker", "Adams", "Nelson", "Hill", "Ramirez", "Campbell", "Mitchell",
			"Roberts", "Carter", "Phillips", "Evans", "Turner", "Torres", "Parker", "Collins", "Edwards",
			"Stewart", "Flores", "Morris", "Nguyen", "Murphy", "Rivera", "Cook", "Rogers", "Morgan",
			"Peterson", "Cooper", "Reed", "Bailey", "Bell", "Gomez", "Kelly", "Howard", "Ward", "Cox",
			"Diaz", "Richardson", "Wood", "Watson", "Brooks", "Bennett", "Gray", "James", "Reyes", "Cruz",
			"Hughes", "Price", "Myers", "Long", "Foster", "Sanders", "Ross", "Morales", "Powell",
			"Sullivan", "Russell", "Ortiz", "Jenkins", "Gutierrez", "Perry", "Butler", "Barnes", "Fisher" };

	@Bean
	public String initDatabase(final SqlConfig config) {
		try (SqlAgent agent = config.agent()) {
			agent.required(() -> {
				agent.update("setup/schema").count();

				var dataCount = (int) agent.query(Person.class).count();
				if (dataCount >= 1000000) {
					return;
				}
				log.info("100万件の初期データを登録します。");

				var random = new Random();
				agent.inserts(
						IntStream.rangeClosed(dataCount + 1, 31250)
								.mapToObj(id -> {
									var fname = fnames[random.nextInt(fnames.length)];
									var lname = lnames[random.nextInt(lnames.length)];
									var email = (fname.replaceAll("-", "_") + lname.replaceAll("-", "_")
											+ "@example.com").toLowerCase();
									if (id % 10000 == 0) {
										log.info(id + "件目を登録します。");
									}

									return new Person(id, fname, lname, email);
								}));
				agent.commit();

				var insertCount = 31250;
				while (insertCount < 1000000) {
					if (dataCount < insertCount * 2) {
						agent.updateWith("DELETE FROM PERSON WHERE ID > " + insertCount).count();
						agent.updateWith("INSERT INTO PERSON SELECT ID + " + insertCount
								+ ", FNAME, LNAME, EMAIL FROM PERSON WHERE ID <= " + insertCount).count();
						log.info(insertCount * 2 + "件登録しました。");
						agent.commit();
					}
					insertCount *= 2;
				}
			});
		}
		return null;
	}
}
