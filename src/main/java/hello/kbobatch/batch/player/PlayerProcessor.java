package hello.kbobatch.batch.player;

import hello.kbobatch.dto.PlayerDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class PlayerProcessor implements ItemProcessor<PlayerDto, PlayerDto> {
    @Override
    public PlayerDto process(PlayerDto item) throws Exception {
        log.info(item.toString());
        return item;
    }
}
