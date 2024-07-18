package hello.kbobatch.domain;

import hello.kbobatch.batch.league.repository.LeagueStatRepository;
import hello.kbobatch.dto.LeagueStatDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LeagueStatTest {

    @Autowired
    LeagueStatRepository leagueStatRepository;

    @Test
    void makeLeagueStat() {
        LeagueStat leagueStat = LeagueStat.builder()
                .lg_pa(35398)
                .lg_r(4722)
                .lg_h(8536)
                .lg_2b(1439)
                .lg_3b(138)
                .lg_hr(850)
                .lg_sf(330)
                .lg_bb(3360)
                .lg_ibb(106)
                .lg_hbp(493)
                .lg_slg(0.414)
                .lg_obp(0.353)
                .build();

        System.out.println(leagueStat.getLg_wOBA());
    }

    @Test
    void saveTest() throws Exception {
        //given
        LeagueStat leagueStat = LeagueStat.builder()
                .lg_pa(35398)
                .lg_r(4722)
                .lg_h(8536)
                .lg_2b(1439)
                .lg_3b(138)
                .lg_hr(850)
                .lg_sf(330)
                .lg_bb(3360)
                .lg_ibb(106)
                .lg_hbp(493)
                .lg_slg(0.414)
                .lg_obp(0.353)
                .build();

        //when
        LeagueStat savedStat = leagueStatRepository.save(leagueStat);
        //then
        assertThat(savedStat).isEqualTo(leagueStat);
    }

    @Test
    void updateTest() throws Exception {
        //given
        LeagueStat leagueStat = LeagueStat.builder()
                .lg_pa(35398)
                .lg_r(4722)
                .lg_h(8536)
                .lg_2b(1439)
                .lg_3b(138)
                .lg_hr(850)
                .lg_sf(330)
                .lg_bb(3360)
                .lg_ibb(106)
                .lg_hbp(493)
                .lg_slg(0.414)
                .lg_obp(0.353)
                .build();

        leagueStatRepository.save(leagueStat);
        //when
        LeagueStat leagueStat1 = leagueStatRepository.findAll().get(0);

        LeagueStatDto dto = new LeagueStatDto();

        dto.setLg_2b(1439);
        dto.setLg_3b(134);
        dto.setLg_bb(439);
        dto.setLg_h(5439);
        dto.setLg_hbp(39);
        dto.setLg_hr(439);
        dto.setLg_ibb(1439);
        dto.setLg_pa(1439);
        dto.setLg_r(1439);
        dto.setLg_sf(1439);
        dto.setLg_pa(1439);
        dto.setLg_obp(0.3);
        dto.setLg_slg(0.4);

        LeagueStat updated = leagueStat.update(dto);

        leagueStatRepository.save(updated);

        //then
        assertThat(updated.getId()).isEqualTo(leagueStat.getId());
    }



}