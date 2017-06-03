package github.mylyed.lagou.service;

import github.mylyed.lagou.db.DB;
import github.mylyed.lagou.model.Href;
import io.ebean.Ebean;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by lilei on 2017/6/1.
 */
public class HrefService {
    static Set<String> urls = new HashSet<>(1000);

    public static void saveHref(Href href) {
        if (href == null) {
            return;
        }
        DB.init();
        Ebean.save(href);
        urls.add(href.getUrl());
    }

    public static void saveHref(List<Href> hrefs) {
        hrefs.stream().filter(href -> !urls.contains(href.getUrl())).forEach(href -> saveHref(href));
    }

}
