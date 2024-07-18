package hello.kbobatch.dto;

import hello.kbobatch.domain.type.Position;
import hello.kbobatch.domain.type.TeamName;
import hello.kbobatch.domain.type.HandsType;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PlayerDto {

    private TeamName team;
    private String name;
    private Position position;
    private String backNum;
    private HandsType type;
    private int age;
    private String birthday;
    private String height;
    private String weight;
    private PlayerStatDto detail;
}
