package hello.kbobatch.batch.league;

import hello.kbobatch.batch.BatchListener;
import hello.kbobatch.batch.league.repository.LeagueStatRepository;
import hello.kbobatch.domain.LeagueStat;
import hello.kbobatch.dto.LeagueStatDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.type.JavaObjectType;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class LeagueBatchConfig {

    private final LeagueStatRepository repository;


    @Bean
    public ItemReader<LeagueStatDto> leagueStatReader() {
        return new JsonItemReaderBuilder<LeagueStatDto>()
                .name("leagueStatReader")
                .resource(new ClassPathResource("leagueStat.json"))
                .jsonObjectReader(new JacksonJsonObjectReader<>(LeagueStatDto.class))
                .build();
    }

    @Bean
    public LeagueStatProcessor leagueStatProcessor() {
        return new LeagueStatProcessor(repository);
    }

    @Bean
    public ItemWriter<LeagueStat> leagueStatItemWriter() {
        return item -> {
            for (LeagueStat stat : item) {
                repository.save(stat);
            }
        };
    }


    @Bean
    public Job leagueStatJob(JobRepository jobRepository, @Qualifier("leagueStatStep") Step step, BatchListener listener) {
        return new JobBuilder("leagueStatJob5", jobRepository)
                .listener(listener)
                .start(step)
                .build();
    }

    @Bean
    public Step leagueStatStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("leagueStatStep4", jobRepository)
                .<LeagueStatDto, LeagueStat>chunk(1, transactionManager)
                .reader(leagueStatReader())
                .processor(leagueStatProcessor())
                .writer(leagueStatItemWriter())
                .build();
    }


}
