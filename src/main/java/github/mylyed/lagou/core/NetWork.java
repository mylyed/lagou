package github.mylyed.lagou.core;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by lilei on 2017/6/2.
 * 网络相关
 */
@Slf4j
public class NetWork {
    /**
     * 模拟浏览器
     */
    static Map<String, String> headers = new HashMap<>();

    static {
        headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        headers.put("Accept-Encoding", "gzip, deflate, sdch, br");
        headers.put("Accept-Language", "zh-CN,zh;q=0.8");
        headers.put("Connection", "keep-alive");
        headers.put("Cache-Control", "max-age=0");
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
    }

    /**
     * 重试次数
     */
    static ThreadLocal<AtomicInteger> times = new ThreadLocal();

    public static Document get(final String url) {
        if (times.get() == null) {
            times.set(new AtomicInteger(0));
        }

        if (times.get().intValue() >= 3) {
            log.warn("{}重试三次都失败了", url);
            return null;
        }
        log.debug("get url->{}", url);
        Document document;
        try {
            document = Jsoup.connect(url).headers(headers)
                    .proxy("127.0.0.1", 1080)
                    .timeout(3000)
                    .post();
            times.remove();
            return document;
        } catch (Exception e) {
            e.printStackTrace();
            document = get(url);
            times.get().getAndIncrement();
            return document;
        }
    }

    public static void main(String[] args) throws IOException {

        Map<String, String> data = new HashMap<>();
        data.put("portrait", "i/image/M00/21/91/CgqKkVcRwp-AQYK7AABCodztl7k27.jpeg");
        data.put("userName", "王子哥");
        data.put("sex", "MALE");
        data.put("positionName", "bbbb");
        data.put("introduce", "asdasd");
        headers.put("Cookie", "user_trace_token=20170426151439-463554e72b2348e5863c9e9e037cce12; LGUID=20170426151439-02f4f9a1-2a50-11e7-8057-525400f775ce; active_success_callback=\"http://www.lagou.com/jobs\"; ab_test_random_num=0; witkey_login_authToken=\"DTpGMWI5+AHkgjSHYmjVw5FGo09gLjOOSXU7ev6cb/vZwP0JC7K65rFMfmtixyOpNM+lRXXcA+bugLkj0Jh/VmZIgtgzjRN+h7PPnAENVijrIxVpVNaG8jDWZJQQ43V2bX38imynfYlk2LBO+bkHUigXrQBGX8k1NnoRMaJEjFB4rucJXOpldXhUiavxhcCELWDotJ+bmNVwmAvQCptcy5e7czUcjiQC32Lco44BMYXrQ+AIOfEccJKHpj0vJ+ngq/27aqj1hWq8tEPFFjdnxMSfKgAnjbIEAX3F9CIW8BSiMHYmPBt7FDDY0CCVFICHr2dp5gQVGvhfbqg7VzvNsw==\"; index_location_city=%E5%85%A8%E5%9B%BD; _gat=1; JSESSIONID=ABAAABAAAGFABAFB277C6DA692795EA5C1AEF86CE05DB0A; _putrc=1272A1C9E141AB5F; login=true; unick=%E6%9D%8E%E7%A3%8A; Hm_lvt_4233e74dff0ae5bd0a3d81c6ccf756e6=1496282039,1496330181,1496365036,1496416312; Hm_lpvt_4233e74dff0ae5bd0a3d81c6ccf756e6=1496418127; _ga=GA1.3.528408182.1493190877; LGSID=20170602231156-d182d601-47a5-11e7-9517-525400f775ce; LGRID=20170602234211-0b5f9328-47aa-11e7-9707-5254005c3644; _ga=GA1.2.528408182.1493190877; _gid=GA1.2.901828726.1496371902");
        Document document = Jsoup.connect("https://account.lagou.com/account/cuser/saveInfo.json").headers(headers).data(data).post();
        System.out.println(document.toString());
    }
}
