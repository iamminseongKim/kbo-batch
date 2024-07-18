package hello.kbobatch.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.kbobatch.dto.PlayerDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerDtoTest {

    @Test
    void makePlayerDto() throws Exception {
        String playerJsonData = "{\"team\":\"HT\",\"backNum\":\"1\",\"name\":\"박찬호\",\"position\":\"IF\",\"type\":\"우투우타\",\"age\":29,\"birthday\":\"1995-06-05\",\"height\":\"178\",\"weight\":\"72\",\"detail\":{\"pa\":360,\"h\":98,\"2b\":16,\"3b\":1,\"hr\":2,\"sf\":4,\"bb\":26,\"ibb\":1,\"hbp\":1,\"slg\":0.375,\"obp\":0.351}}";
        ObjectMapper mapper = new ObjectMapper();

        PlayerDto playerDto = mapper.readValue(playerJsonData, PlayerDto.class);

        assertNotNull(playerDto);
    }

}