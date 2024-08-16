package hello.kbobatch.domain;

import hello.kbobatch.dto.LeagueStatDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LeagueStat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer lg_pa;
    private Integer lg_r;
    private Integer lg_h;
    private Integer lg_2b;
    private Integer lg_3b;
    private Integer lg_hr;
    private Integer lg_sf;
    private Integer lg_bb;
    private Integer lg_ibb;
    private Integer lg_hbp;
    private Integer lg_ab;
    private Double lg_slg;
    private Double lg_obp;

    private Double lg_wOBA;

    @Builder
    public LeagueStat(Integer lg_pa, Integer lg_r, Integer lg_h, Integer lg_2b, Integer lg_3b, Integer lg_hr, Integer lg_sf, Integer lg_bb, Integer lg_ibb, Integer lg_hbp, Double lg_slg, Double lg_obp, Integer lg_ab) {
        this.lg_pa = lg_pa;
        this.lg_ab = lg_ab;
        this.lg_r = lg_r;
        this.lg_h = lg_h;
        this.lg_2b = lg_2b;
        this.lg_3b = lg_3b;
        this.lg_hr = lg_hr;
        this.lg_sf = lg_sf;
        this.lg_bb = lg_bb;
        this.lg_ibb = lg_ibb;
        this.lg_hbp = lg_hbp;
        this.lg_slg = lg_slg;
        this.lg_obp = lg_obp;
        this.lg_wOBA = wObaCalculation();
    }

    public LeagueStat update(LeagueStatDto dto) {
        this.lg_pa = dto.getLg_pa();
        this.lg_ab = dto.getLg_ab();
        this.lg_r = dto.getLg_r();
        this.lg_h = dto.getLg_h();
        this.lg_2b = dto.getLg_2b();
        this.lg_3b = dto.getLg_3b();
        this.lg_hr = dto.getLg_hr();
        this.lg_sf = dto.getLg_sf();
        this.lg_bb = dto.getLg_bb();
        this.lg_ibb = dto.getLg_ibb();
        this.lg_hbp = dto.getLg_hbp();
        this.lg_slg = dto.getLg_slg();
        this.lg_obp = dto.getLg_obp();
        this.lg_wOBA = wObaCalculation();
        return this;
    }

    public double wObaCalculation() {
        return  ((0.7 * lg_bb) + (0.7 * lg_ibb) + (0.7 * lg_hbp) + (0.9* (lg_h - lg_2b - lg_3b - lg_hr) +
                (1.2 * lg_2b) + (1.6 * lg_3b) + (2.0 * lg_hr))) / (lg_pa + lg_bb - lg_ibb + lg_hbp + lg_sf);
    }
}
