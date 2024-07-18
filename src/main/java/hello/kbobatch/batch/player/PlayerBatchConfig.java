package hello.kbobatch.batch.player;

import hello.kbobatch.batch.BatchListener;
import hello.kbobatch.dto.PlayerDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
public class PlayerBatchConfig {

    @Bean
    public ItemReader<PlayerDto> playerReader() {
        return new JsonItemReaderBuilder<PlayerDto>()
                .name("playerItemReader")
                .resource(new ClassPathResource("player.json"))
                .jsonObjectReader(new JacksonJsonObjectReader<>(PlayerDto.class))
                .build();
    }


    @Bean
    public PlayerProcessor processor() {
        return new PlayerProcessor();
    }

    @Bean
    public ItemWriter<PlayerDto> writer() {
        return items -> {
            for (PlayerDto player : items) {
                //save 로직
                log.info("Player: {} 저장 완료~", player.getName());
            }
        };
    }

    @Bean
    public Job playerBatchJob(JobRepository jobRepository, @Qualifier("playerStep") Step step, BatchListener listener) {
        return new JobBuilder("playerBatchJob", jobRepository)
                .listener(listener)
                .start(step)
                .build();
    }

    @Bean
    public Step playerStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("playerStep", jobRepository)
                .<PlayerDto, PlayerDto> chunk(10, transactionManager)
                .reader(playerReader())
                .processor(processor())
                .writer(writer())
                .build();
    }
}
