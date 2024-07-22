package hello.kbobatch.batch.player;

import hello.kbobatch.batch.BatchListener;
import hello.kbobatch.batch.league.repository.LeagueStatRepository;
import hello.kbobatch.domain.LeagueStat;
import hello.kbobatch.domain.Player;
import hello.kbobatch.dto.PlayerDto;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class PlayerBatchConfig {

    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;
    private final LeagueStatRepository leagueStatRepository;

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
        return new PlayerProcessor(teamRepository, playerRepository, leagueStatRepository);
    }

    @Bean
    public ItemWriter<Player> writer() {
        return items -> {
            for (Player player : items) {
                playerRepository.save(player);
            }
        };
    }

    @Bean
    public Job playerBatchJob(JobRepository jobRepository, @Qualifier("playerStep") Step step, BatchListener listener) {
        return new JobBuilder("playerBatchJob19", jobRepository)
                .listener(listener)
                .start(step)
                .build();
    }

    @Bean
    public Step playerStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("playerStep", jobRepository)
                .<PlayerDto, Player> chunk(10, transactionManager)
                .reader(playerReader())
                .processor(processor())
                .writer(writer())
                .build();
    }
}
