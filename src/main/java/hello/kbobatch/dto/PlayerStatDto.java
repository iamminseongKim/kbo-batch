package hello.kbobatch.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PlayerStatDto {

    private int pa;
    private int h;

    @JsonProperty("2b")
    private int twoH;
    @JsonProperty("3b")
    private int threeH;
    private int hr;
    private int sf;
    private int bb;
    private int ibb;
    private int hbp;
    private double slg;
    private double obp;
    private int ab;

    private String era;
    private String inning;
}
