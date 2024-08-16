package hello.kbobatch.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hello.kbobatch.domain.type.TeamName;
import hello.kbobatch.dto.PlayerDto;
import hello.kbobatch.dto.PlayerStatDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    PlayerDto playerDto;
    Team team;
    PlayerStat playerStat;

    @BeforeEach
    void setUp() throws Exception {
        String playerJsonData = "{\"team\":\"HT\",\"backNum\":\"1\",\"name\":\"박찬호\",\"position\":\"IF\",\"type\":\"우투우타\",\"age\":29,\"birthday\":\"1995-06-05\",\"height\":\"178\",\"weight\":\"72\",\"detail\":{\"pa\":360,\"h\":98,\"2b\":16,\"3b\":1,\"hr\":2,\"sf\":4,\"bb\":26,\"ibb\":1,\"hbp\":1,\"slg\":0.375,\"obp\":0.351}}";
        ObjectMapper mapper = new ObjectMapper();
        PlayerDto playerDto = mapper.readValue(playerJsonData, PlayerDto.class);

        team = Team.builder()
                .teamCode(TeamName.HT)
                .name("KIA 타이거즈")
                .parkFactor(1.021)
                .build();
        PlayerStatDto detail = playerDto.getDetail();
        playerStat = PlayerStat.builder()
                .ab(detail.getAb())
                .bb(detail.getBb())
                .h(detail.getH())
                .h_2b(detail.getTwoH())
                .h_3b(detail.getThreeH())
                .hr(detail.getHr())
                .hbp(detail.getHbp())
                .ibb(detail.getIbb())
                .obp(detail.getObp())
                .pa(detail.getPa())
                .sf(detail.getSf())
                .slg(detail.getSlg())
                .build();
    }

    @Test
    void createPlayer() throws Exception {

        Player player = Player.builder()
                .type(playerDto.getType())
                .team(team)
                .stat(playerStat)
                .position(playerDto.getPosition())
                .name(playerDto.getName())
                .age(playerDto.getAge())
                .weight(Integer.parseInt(playerDto.getWeight()))
                .height(Integer.parseInt(playerDto.getHeight()))
                .birthDate(LocalDate.parse(playerDto.getBirthday()))
                .backNum(playerDto.getBackNum())
                .build();



    }

}