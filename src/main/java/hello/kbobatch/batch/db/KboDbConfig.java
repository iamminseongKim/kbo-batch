package hello.kbobatch.batch.db;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableJpaRepositories(
        basePackages = "hello.kbobatch",
        entityManagerFactoryRef = "dataEntityManager",
        transactionManagerRef = "dataTransactionManager"
)
public class KboDbConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource-kbo")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }


    @Bean
    public LocalContainerEntityManagerFactoryBean dataEntityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();

        em.setDataSource(dataSource());
        em.setPackagesToScan("hello.kbobatch");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "update");
        //properties.put("hibernate.show_sql", "false");
        em.setJpaPropertyMap(properties);

        return em;
    }


    @Bean
    public PlatformTransactionManager dataTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();

        transactionManager.setEntityManagerFactory(dataEntityManager().getObject());

        return transactionManager;
    }
}
