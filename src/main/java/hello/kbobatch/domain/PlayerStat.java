package hello.kbobatch.domain;

import hello.kbobatch.dto.PlayerStatDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlayerStat {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stat_id")
    private Long id;

    private Integer pa;
    private Integer h;
    @Column(name = "2b")
    private Integer h_2b;
    @Column(name = "3b")
    private Integer h_3b;
    private Integer hr;
    private Integer sf;
    private Integer bb;
    private Integer ibb;
    private Integer hbp;
    private Double slg;
    private Double obp;
    private Integer ab;

    private Double ops;
    private Integer wrcPlus;

    @Builder
    public PlayerStat(Long id, Integer pa, Integer h, Integer h_2b, Integer h_3b, Integer hr, Integer sf, Integer bb, Integer ibb, Integer hbp, Double slg, Double obp, Integer ab) {
        this.id = id;
        this.pa = pa;
        this.h = h;
        this.h_2b = h_2b;
        this.h_3b = h_3b;
        this.hr = hr;
        this.sf = sf;
        this.bb = bb;
        this.ibb = ibb;
        this.hbp = hbp;
        this.slg = slg;
        this.obp = obp;
        this.ops = obp + slg;
        this.ab = ab;
    }

    public PlayerStat setWrcPlus(int wrcPlus) {
        this.wrcPlus = wrcPlus;
        return this;
    }

    public void updateStat(PlayerStatDto detail) {
        this.pa = detail.getPa();
        this.h = detail.getH();
        this.h_2b = detail.getTwoH();
        this.h_3b = detail.getThreeH();
        this.hr = detail.getHr();
        this.sf = detail.getSf();
        this.bb = detail.getBb();
        this.ibb = detail.getIbb();
        this.hbp = detail.getHbp();
        this.slg = detail.getSlg();
        this.obp = detail.getObp();
        this.ops = detail.getObp() + detail.getSlg();
        this.ab = detail.getAb();
    }
}
