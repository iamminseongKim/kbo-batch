package hello.kbobatch.batch.player;

import hello.kbobatch.batch.league.repository.LeagueStatRepository;
import hello.kbobatch.domain.LeagueStat;
import hello.kbobatch.domain.Player;
import hello.kbobatch.domain.PlayerStat;
import hello.kbobatch.domain.Team;
import hello.kbobatch.domain.type.Position;
import hello.kbobatch.dto.PlayerDto;
import hello.kbobatch.dto.PlayerStatDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.Optional;

@Slf4j
public class PlayerProcessor implements ItemProcessor<PlayerDto, Player> {

    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;
    private final LeagueStatRepository leagueStatRepository;
    private final PlayerStatRepository playerStatRepository;

    private LeagueStat leagueStat;

    public PlayerProcessor(PlayerRepository playerRepository, TeamRepository teamRepository, LeagueStatRepository leagueStatRepository1, PlayerStatRepository playerStatRepository) {
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
        this.leagueStatRepository = leagueStatRepository1;
        this.playerStatRepository = playerStatRepository;
    }

    @Override
    public Player process(PlayerDto item) throws Exception {

        // 신규 선수라면 새로 만들어야 하고, 기존 선수라면 스텟을 업데이트해야함.

        Optional<Player> findPlayer = playerRepository.findByNameAndBirthDateWithTeamAndStat(item.getName(), LocalDate.parse(item.getBirthday()));
        Player player;
        if (findPlayer.isPresent()) {
            player = findPlayer.get();

            if (player.getTeam().getTeamCode() != item.getTeam()) {
                player.changeTeam(teamRepository.findByTeamCode(item.getTeam()));
            }
            return updatePlayer(player, item);
        } else {
             return createPlayer(item);
        }
    }

    private Player updatePlayer(Player player, PlayerDto item) {

        PlayerStat stat = player.getStat();
        try {
            stat.updateStat(item.getDetail(), player);
            if (!item.getPosition().equals(Position.P) && item.getDetail()!=null) {
                stat.setWrcPlus(calculateWrcPlus(item, leagueStat));
            }
        } catch (Exception e) {
            PlayerStatDto detail = new PlayerStatDto();
            detail.setAb(0);
            detail.setBb(0);
            detail.setEra("0");
            detail.setH(0);
            detail.setHbp(0);
            detail.setHr(0);
            detail.setTwoH(0);
            detail.setThreeH(0);
            detail.setObp(0.0);
            detail.setSlg(0.0);
            detail.setPa(0);
            detail.setSf(0);
            stat.updateStat(detail, player);
            log.info("{} 선수의 스탯 정보가 없음.", player.getName());
        }

        playerStatRepository.save(stat);
        return player.updateStat(stat);
    }

    private Player createPlayer(PlayerDto item) {

        Team playersTeam = teamRepository.findByTeamCode(item.getTeam());
        PlayerStat playerStat = getPlayerStat(item);

        return Player.builder()
                .age(item.getAge())
                .backNum(item.getBackNum())
                .birthDate(LocalDate.parse(item.getBirthday()))
                .height(Integer.parseInt(item.getHeight()))
                .weight(Integer.parseInt(item.getWeight()))
                .name(item.getName())
                .position(item.getPosition())
                .stat(playerStat)
                .team(playersTeam)
                .type(item.getType())
                .build();
    }

    private PlayerStat getPlayerStat(PlayerDto item) {
        PlayerStatDto stat = item.getDetail();
        PlayerStat playerStat;

        try {
            playerStat = PlayerStat.builder()
                    .pa(stat.getPa())
                    .h(stat.getH())
                    .h_2b(stat.getTwoH())
                    .h_3b(stat.getThreeH())
                    .hr(stat.getHr())
                    .bb(stat.getBb())
                    .ibb(stat.getIbb())
                    .hbp(stat.getHbp())
                    .sf(stat.getSf())
                    .obp(stat.getObp())
                    .slg(stat.getSlg())
                    .ab(stat.getAb())
                    .build();

        } catch (Exception e) {
            playerStat = PlayerStat.builder()
                    .ibb(0)
                    .bb(0)
                    .h(0)
                    .h_2b(0)
                    .h_3b(0)
                    .hr(0)
                    .hbp(0)
                    .pa(0)
                    .sf(0)
                    .obp(0.0)
                    .slg(0.0)
                    .ab(0)
                    .build();

            log.info("{} 선수의 스탯 정보가 없음.", item.getName());
        }

        if (!item.getPosition().equals(Position.P) && item.getDetail()!=null) {
            playerStat.setWrcPlus(calculateWrcPlus(item, leagueStat));
        }
        return playerStat;
    }

    private int calculateWrcPlus(PlayerDto item, LeagueStat leagueStat) {

        if (leagueStat == null) {
            leagueStat = leagueStatRepository.findAll().getFirst();
        }


        double wOba = getWOba(item);
        double lg_wOba = leagueStat.getLg_wOBA();
        double wOba_sc = wOba / lg_wOba;
        double lg_avg_r = (double) leagueStat.getLg_r() / leagueStat.getLg_pa();
        double lg_wRcPerPa = (lg_avg_r / leagueStat.getLg_pa()) * leagueStat.getLg_pa();
        int pa = item.getDetail().getPa();
        double wRAA = ((wOba - lg_wOba)/wOba_sc) * pa;
        double parkFactor = teamRepository.findByTeamCode(item.getTeam()).getParkFactor();
        double wrcPlus = ((wRAA/pa +lg_avg_r) + (lg_avg_r - (parkFactor * lg_avg_r))) / lg_wRcPerPa * 100;
        return (int) wrcPlus;
    }

    private double getWOba(PlayerDto playerDto) {
        PlayerStatDto stat = playerDto.getDetail();
        return (((0.7 * stat.getBb()) + (0.7 * stat.getIbb()) + (0.7 * stat.getHbp())) +
                (0.9 * (stat.getH()-stat.getTwoH()-stat.getThreeH()-stat.getHr())) + (1.2 * stat.getTwoH()) + (1.6 * stat.getThreeH()) + (2.0 * stat.getHr()))
                / (stat.getAb() + stat.getBb() - stat.getIbb() + stat.getHbp() + stat.getSf())
                ;
    }
}
