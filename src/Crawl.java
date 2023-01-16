import java.io.IOException;
import java.util.HashSet;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Crawl {
    public static void main(String[] args) {
        String url = "https://www.amazon.com";
        crawl(1, url, new HashSet<String>());
    }

    private static void crawl(int level, String url, HashSet<String> visited) {
        if (level > 5)
            return;

        Document doc = request(url, visited);
        if (doc != null) {
            for (Element link : doc.select("a[href]")) {
                String next_link = link.absUrl("href");

                if (next_link.indexOf("http") < 0)
                    continue;
                if (!visited.contains(next_link))
                    crawl(level + 1, next_link, visited);
            }
        }
    }

    private static Document request(String url, HashSet<String> list) {
        try {
            Connection conn = Jsoup.connect(url);
            Document doc = conn.get();

            if (conn.response().statusCode() == 200) {
                System.out.println("Link: " + url);
                System.out.println(doc.title());
                list.add(url);

                return doc;
            }
            return null;
        } catch (IOException e) {
            return null;
        }

    }
}
