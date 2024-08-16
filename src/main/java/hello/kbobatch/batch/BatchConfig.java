package hello.kbobatch.batch;

import hello.kbobatch.batch.league.LeagueStatProcessor;
import hello.kbobatch.batch.league.repository.LeagueStatRepository;
import hello.kbobatch.batch.player.PlayerProcessor;
import hello.kbobatch.batch.player.PlayerRepository;
import hello.kbobatch.batch.player.TeamRepository;
import hello.kbobatch.domain.LeagueStat;
import hello.kbobatch.domain.Player;
import hello.kbobatch.dto.LeagueStatDto;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class BatchConfig {

    private final LeagueStatRepository leagueStatRepository;
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;

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
        return new LeagueStatProcessor(leagueStatRepository);
    }

    @Bean
    public ItemWriter<LeagueStat> leagueStatWriter() {
        return item -> {
            for (LeagueStat leagueStat : item) {
                leagueStatRepository.save(leagueStat);
            }
        };
    }

    @Bean
    public ItemReader<PlayerDto> playerReader() {
        return new JsonItemReaderBuilder<PlayerDto>()
                .name("playerReader")
                .resource(new ClassPathResource("player.json"))
                .jsonObjectReader(new JacksonJsonObjectReader<>(PlayerDto.class))
                .build();
    }

    @Bean
    public PlayerProcessor playerProcessor() {
        return new PlayerProcessor(playerRepository, teamRepository, leagueStatRepository);
    }

    @Bean
    public ItemWriter<Player> playerWriter() {
        return item -> {
            for (Player player : item) {
                playerRepository.save(player);
            }
        };
    }

    @Bean
    public Job kboJob(JobRepository jobRepository, @Qualifier("playerStep") Step playerStep, @Qualifier("leagueStatStep")Step leagueStatStep, BatchListener batchListener) {
        String jobName = "kboJob" + System.currentTimeMillis();
        return new JobBuilder(jobName, jobRepository)
                .listener(batchListener)
                .start(leagueStatStep)
                .next(playerStep)
                .build();
    }

    @Bean
    public Step leagueStatStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        String stepName = "leagueStatStep-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
        return new StepBuilder(stepName, jobRepository)
                .<LeagueStatDto, LeagueStat>chunk(1, transactionManager)
                .reader(leagueStatReader())
                .processor(leagueStatProcessor())
                .writer(leagueStatWriter())
                .build();
    }

    @Bean
    public Step playerStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        String stepName = "playerStep-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
        return new StepBuilder(stepName, jobRepository)
                .<PlayerDto, Player>chunk(10, transactionManager)
                .reader(playerReader())
                .processor(playerProcessor())
                .writer(playerWriter())
                .build();
    }

}
