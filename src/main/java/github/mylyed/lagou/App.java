package github.mylyed.lagou;

import github.mylyed.lagou.db.DB;
import github.mylyed.lagou.model.Job;
import io.ebean.Ebean;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.util.List;

/**
 * Created by lilei on 2017/6/1.
 */
@Slf4j
public class App {

    public static void main(String[] args) {
        try {

            DB.init();
            List<Job> jobList = Ebean.findNative(Job.class, "SELECT * FROM T_JOB ").findList();

            log.debug("jobList.size->{}", jobList.size());
            JFrame jFrame = new JFrame();
            jFrame.add(new DistributionGraph2(jobList));
            jFrame.setSize(500, 800);
            jFrame.setVisible(true);
            jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
