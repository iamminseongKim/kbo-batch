package hello.kbobatch.batch.player;

import hello.kbobatch.batch.league.repository.LeagueStatRepository;
import hello.kbobatch.domain.LeagueStat;
import hello.kbobatch.domain.Player;
import hello.kbobatch.domain.PlayerStat;
import hello.kbobatch.domain.Team;
import hello.kbobatch.domain.type.Position;
import hello.kbobatch.domain.type.TeamName;
import hello.kbobatch.dto.PlayerDto;
import hello.kbobatch.dto.PlayerStatDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDate;
import java.util.Optional;


@Slf4j
public class PlayerProcessor implements ItemProcessor<PlayerDto, Player> {

    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final LeagueStat leagueStat;

    public PlayerProcessor(final TeamRepository teamRepository, final PlayerRepository playerRepository, final LeagueStatRepository leagueStatRepository) {
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
        this.leagueStat = leagueStatRepository.findAll().getFirst();
    }

    @Override
    public Player process(PlayerDto item) {

        Optional<Player> playerOptional = playerRepository.findByNameAndBirthDate(item.getName(), LocalDate.parse(item.getBirthday()));
        if (playerOptional.isPresent()) {
            return updatePlayer(playerOptional.get(), item);
        } else {
            return createPlayer(item);
        }
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

    private Player updatePlayer(Player player, PlayerDto item) {

        PlayerStat stat = player.getStat();
        stat.updateStat(item.getDetail());

        if (!item.getPosition().equals(Position.P) && item.getDetail()!=null) {
            stat.setWrcPlus(calculateWrcPlus(item, leagueStat));
        }

        return player.updateStat(stat);
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
                    .build();

            log.info("{} 선수의 스탯 정보가 없음.", item.getName());
        }

        if (!item.getPosition().equals(Position.P) && item.getDetail()!=null) {
            playerStat.setWrcPlus(calculateWrcPlus(item, leagueStat));
        }
        return playerStat;
    }

    private int calculateWrcPlus(PlayerDto item, LeagueStat leagueStat) {
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

    private double getWOba(PlayerDto item) {
        PlayerStatDto stat = item.getDetail();
        return (((0.7 * stat.getBb()) + (0.7 * stat.getIbb()) + (0.7 * stat.getHbp())) +
                (0.9 * (stat.getH()-stat.getTwoH()-stat.getThreeH()-stat.getHr())) + (1.2 * stat.getTwoH()) + (1.6 * stat.getThreeH()) + (2.0 * stat.getHr()))
                / (stat.getAb() + stat.getBb() - stat.getIbb() + stat.getHbp() + stat.getSf())
                ;
    }


}
