package hello.kbobatch.batch.league;

import hello.kbobatch.batch.league.repository.LeagueStatRepository;
import hello.kbobatch.domain.LeagueStat;
import hello.kbobatch.dto.LeagueStatDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class LeagueStatProcessor implements ItemProcessor<LeagueStatDto, LeagueStat> {

    private final LeagueStatRepository leagueStatRepository;

    @Override
    public LeagueStat process(LeagueStatDto item) throws Exception {
        log.info("LeagueStatDto -> leagueStat Entity.");

        List<LeagueStat> all = leagueStatRepository.findAll();


        if (all.isEmpty()) {
            return LeagueStat.builder()
                    .lg_obp(item.getLg_obp())
                    .lg_slg(item.getLg_slg())
                    .lg_hbp(item.getLg_hbp())
                    .lg_ibb(item.getLg_ibb())
                    .lg_bb(item.getLg_bb())
                    .lg_sf(item.getLg_sf())
                    .lg_hr(item.getLg_hr())
                    .lg_3b(item.getLg_3b())
                    .lg_2b(item.getLg_2b())
                    .lg_h(item.getLg_h())
                    .lg_r(item.getLg_r())
                    .lg_pa(item.getLg_pa())
                    .build();
        } else {
            return all.getFirst().update(item);
        }
    }
}
