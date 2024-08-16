package hello.kbobatch;

import hello.kbobatch.batch.player.PlayerProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class KbobatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(KbobatchApplication.class, args);
	}

}
