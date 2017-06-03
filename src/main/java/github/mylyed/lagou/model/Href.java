package github.mylyed.lagou.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by lilei on 2017/6/2.
 */
@Data
@ToString
@Entity
@Table(name = "t_href")
public class Href {
    @Id
    private Long id;

    private String url;
    @Enumerated(EnumType.STRING)
    private Type type;

    private Date addTime;

    @Enumerated(EnumType.STRING)
    private Status hrefStatus;

    public enum Type {
        ZHAOPIN, JOB, GONGSI
    }

    public enum Status {
        AWAIT, SUCCESS, FAIL
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Href href = (Href) o;

        return url != null ? url.equals(href.url) : href.url == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }
}
