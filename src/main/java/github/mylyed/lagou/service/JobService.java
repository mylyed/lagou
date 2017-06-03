package github.mylyed.lagou.service;

import github.mylyed.lagou.db.DB;
import github.mylyed.lagou.model.Job;
import io.ebean.Ebean;

import java.util.List;

/**
 * Created by lilei on 2017/6/1.
 */
public class JobService {

    public static void saveJob(Job job) {
        if (null == job) {
            return;
        }
        DB.init();
        Ebean.save(job);
    }

    public static void saveJobs(List<Job> jobs) {
        DB.init();
        jobs.stream().filter(j -> null != j).forEach(job -> {
            Ebean.save(job);
        });
    }


}
