package github.mylyed.lagou.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by lilei on 2017/6/1.
 */


@EqualsAndHashCode
@Data
@ToString
@Entity
@Table(name = "t_job")
public class Job {
    @Id
    private Long id;
    private String url;
    private Double salaryBottom;
    private Double salaryTop;
    private Double experienceBottom;
    private Double experienceTop;
    /**
     * 爬取时间
     */
    private Date crawlTime;
    /**
     * 公司
     */
    private String company;
    /**
     * 工作地址
     */
    private String address;

    /**
     * 学历要求
     */
    private String diploma;
    /**
     * 工作类型
     */
    private String type;

    /**
     * 工作名称
     */
    private String jobName;

}
