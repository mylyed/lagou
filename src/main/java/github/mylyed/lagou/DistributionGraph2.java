package github.mylyed.lagou;

import github.mylyed.lagou.model.Job;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * X 代表 经验
 * y 代表  薪资
 * <p>
 * Created by lilei on 2017/6/1.
 */
@Slf4j
public class DistributionGraph2 extends JPanel {


    int x_translation = 50;
    int y_translation = 20;

    int x_w = 20;//小方格宽度
    int y_h = 20;//小方格高度

    int x_max = 10;
    int y_max = 30;

    int x_max_w = x_w * x_max;
    int y_max_h = y_h * y_max;

    List<Job> jobs;

    public DistributionGraph2(List<Job> jobs) {
        this.jobs = jobs;
    }

    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
        //画框
        graphics.setColor(Color.GREEN);
        graphics.drawRect(x_translation, y_translation, x_max_w, y_max_h);

        graphics.setColor(Color.RED);
        for (int i = 0; i < x_max; i++) {
            // 绘制第i条竖直线
            graphics.drawLine((i * x_w) + x_translation, y_translation, (i * x_w) + x_translation, y_max_h + y_translation);
        }

        graphics.setColor(Color.BLUE);
        for (int i = 0; i < y_max; i++) {
            graphics.drawLine(x_translation, (i * y_h) + y_translation, x_max_w + x_translation, (i * y_h) + y_translation);
        }

        graphics.setColor(Color.BLACK);

        //X
        for (int i = 0; i < x_max + 1; i++) {
            graphics.drawString(i + "年", x_w * i + x_translation - 5, y_max_h + 20 + y_translation);
        }

        //Y
        for (int i = 0; i < y_max; i++) {
            graphics.drawString(((y_max) - i) + "k", x_translation - 25, y_h * i + 10 + y_translation);
        }

        graphics.setColor(Color.BLACK);
        for (Job job : jobs) {
            if (job.getExperienceBottom() == null ||
                    job.getSalaryBottom() == null ||
                    job.getExperienceTop() == null ||
                    job.getSalaryTop() == null
                    ) {
                continue;
            }
            int x1 = (int) (job.getExperienceBottom() * y_h) + x_translation;
            int y1 = y_max_h - (int) (job.getSalaryBottom() * x_w) + y_translation;
            int x2 = (int) (job.getExperienceTop() * y_h) + x_translation;
            int y2 = y_max_h - (int) (job.getSalaryTop() * x_w) + y_translation;
//            //System.out.println("x1:" + x1 + ",y1:" + y1 + ",x2:" + x2 + ",y2:" + y2);
//            log.debug("x1:" + x1 + ",y1:" + y1 + ",x2:" + x2 + ",y2:" + y2);
            graphics.drawLine(x1, y1, x2, y2);
        }
    }

}
