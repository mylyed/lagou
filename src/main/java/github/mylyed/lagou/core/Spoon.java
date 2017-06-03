package github.mylyed.lagou.core;

import github.mylyed.lagou.model.Href;
import github.mylyed.lagou.model.Job;
import github.mylyed.lagou.service.HrefService;
import github.mylyed.lagou.service.JobService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static github.mylyed.lagou.core.NetWork.get;

/**
 * Created by lilei on 2017/5/31.
 */
@Slf4j
public class Spoon {


    static Set<String> crawled = new HashSet<String>();

    static Set<Href> crawlTarget = new HashSet<Href>();

    public static void main(String[] args) {
        Href href = new Href();
        href.setUrl("https://www.lagou.com");
        yzxh(href);
    }

    public static void yzxh(Href url) {
        log.debug("爬取{}", url);
        if (crawled.contains(url)) {
            log.debug("已经爬取过了{}", url);
            return;
        }
        List<Href> hrefs = crawlHref(url.getUrl());
        crawled.add(url.getUrl());
        HrefService.saveHref(hrefs);
        for (Href h : hrefs) {
            if (crawled.contains(h.getUrl())) {
                log.debug("for->已经爬取过了{}", url);
                continue;
            }
            if (h.getType().equals(Href.Type.JOB)) {
                Job job = parseJob(h.getUrl());
                JobService.saveJob(job);
                crawled.add(h.getUrl());
            } else if (h.getType().equals(Href.Type.ZHAOPIN)) {
                yzxh(h);
            } else if (h.getType().equals(Href.Type.JOB)) {
                yzxh(h);
            }
        }
    }

//    public static void crawl(String url) throws IOException {
//        if (crawled.contains(url)) {
//            return;
//        }
//        Document document = get(url);
//        Elements link = document.select("a[class=\"position_link\"]");
//        Elements page_no = document.select("a[class=\"page_no\"]");
//        crawled.add(url);
//        for (Element e : link) {
//            String absHref = e.attr("href");
//            log.debug("job->{}", absHref);
//
//            zhaopin.add(absHref);
//        }
//        for (Element e : page_no) {
//            String absHref = e.attr("href");
//            crawl(absHref);
//        }
//    }

    static final Pattern EXPERIENCE = Pattern.compile("(\\d+)-(\\d+)");
    /**
     * 小写k
     */
    static final Pattern SALARY = Pattern.compile("(\\d+)k-(\\d+)k");
    /**
     * 大写K
     */
    static final Pattern SALARY_2 = Pattern.compile("(\\d+)K-(\\d+)K");

    public static Job parseJob(String url) {
        try {
            Document document = get(url);
            Element job_request = document.select("dd[class=\"job_request\"]>p").first();
            Elements jr = job_request.select("span");
            Job job = new Job();
            job.setCrawlTime(new Date());

            job.setUrl(url);
            String salary = jr.get(0).text();

            Matcher matcherSalary = SALARY.matcher(salary);
            if (matcherSalary.find()) {
                job.setSalaryBottom(Double.parseDouble(matcherSalary.group(1)));
                job.setSalaryTop(Double.parseDouble(matcherSalary.group(2)));
            } else {
                Matcher matcherSalary_2 = SALARY_2.matcher(salary);
                if (matcherSalary_2.find()) {
                    job.setSalaryBottom(Double.parseDouble(matcherSalary_2.group(1)));
                    job.setSalaryTop(Double.parseDouble(matcherSalary_2.group(2)));
                }
            }

            String address = jr.get(1).text().replace("/", "").trim();

            job.setAddress(address);

            String jy = jr.get(2).text().replace("/", "").trim();

            //经验：经验3-5年

            Matcher m = EXPERIENCE.matcher(jy);
            if (m.find()) {
                job.setExperienceBottom(Double.parseDouble(m.group(1)));
                job.setExperienceTop(Double.parseDouble(m.group(2)));
            }
            String diploma = jr.get(3).text().replace("/", "").trim();
            job.setDiploma(diploma);
            String type = jr.get(4).text().replace("/", "").trim();
            job.setType(type);

            //公司
            Element company_name = document.select("div[class=\"job-name\"]>div[class=\"company\"]").first();
            job.setCompany(company_name.text());

            Element job_name = document.select("div[class=\"job-name\"]>span[class=\"name\"]").first();
            job.setJobName(job_name.text());
            log.debug("job->{}", job.toString());
            return job;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("出错时url：{}", url);
            try {
                //放慢速度
                Thread.sleep(2000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }


    public static List<Href> crawlHref(String url) {
        Document document = get(url);
        List<Href> hrefs = new ArrayList<>();
        if (document == null) {
            return hrefs;
        }
        Elements zhaopins = document.select("a[href~=https://www.lagou.com/zhaopin/(\\w+)]");

        Elements jobs = document.select("a[href~=https://www.lagou.com/jobs/(\\d+).html]");

        Elements gongsi = document.select("a[href~=https://www.lagou.com/gongsi/(\\d+|j\\d+).html]");

        for (Element e : zhaopins) {
            String href = e.attr("href");
            Href hr = new Href();
            hr.setAddTime(new Date());
            hr.setUrl(href);
            hr.setType(Href.Type.ZHAOPIN);
            hr.setHrefStatus(Href.Status.AWAIT);
            hrefs.add(hr);
            log.debug("zhaopins->{}", href);
        }
        for (Element e : jobs) {
            String href = e.attr("href");
            Href hr = new Href();
            hr.setAddTime(new Date());
            hr.setUrl(href);
            hr.setType(Href.Type.JOB);
            hr.setHrefStatus(Href.Status.AWAIT);
            hrefs.add(hr);
            log.debug("jobs->{}", href);
        }
        for (Element e : gongsi) {
            String href = e.attr("href");
            Href hr = new Href();
            hr.setAddTime(new Date());
            hr.setUrl(href);
            hr.setType(Href.Type.GONGSI);
            hr.setHrefStatus(Href.Status.AWAIT);
            hrefs.add(hr);
            log.debug("gongsi->{}", href);
        }
        return hrefs;
    }

}
