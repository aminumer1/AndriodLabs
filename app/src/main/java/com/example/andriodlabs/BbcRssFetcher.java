package com.example.andriodlabs;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BbcRssFetcher {

    // IMPORTANT: use HTTPS (Android blocks HTTP by default)
    public static final String RSS_URL = "https://feeds.bbci.co.uk/news/world/us_and_canada/rss.xml";

    public List<Article> fetch() throws Exception {
        URL url = new URL(RSS_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(15000);
        conn.setReadTimeout(15000);

        try (InputStream in = conn.getInputStream()) {
            return parse(in);
        } finally {
            conn.disconnect();
        }
    }

    private List<Article> parse(InputStream in) throws Exception {
        List<Article> items = new ArrayList<>();

        XmlPullParser p = Xml.newPullParser();
        p.setInput(in, null);

        boolean inItem = false;
        String title = null, link = null, desc = null, date = null;

        int event = p.getEventType();
        while (event != XmlPullParser.END_DOCUMENT) {
            String name = p.getName();

            if (event == XmlPullParser.START_TAG) {
                if ("item".equalsIgnoreCase(name)) {
                    inItem = true;
                    title = link = desc = date = null;
                } else if (inItem && "title".equalsIgnoreCase(name)) {
                    title = safeNextText(p);
                } else if (inItem && "link".equalsIgnoreCase(name)) {
                    link = safeNextText(p);
                } else if (inItem && "description".equalsIgnoreCase(name)) {
                    desc = safeNextText(p);
                } else if (inItem && "pubDate".equalsIgnoreCase(name)) {
                    date = safeNextText(p);
                }
            } else if (event == XmlPullParser.END_TAG) {
                if ("item".equalsIgnoreCase(name)) {
                    inItem = false;
                    if (title != null && link != null) {
                        items.add(new Article(title, link, desc, date));
                    }
                }
            }
            event = p.next();
        }

        return items;
    }

    private String safeNextText(XmlPullParser p) throws Exception {
        String t = p.nextText();
        return t == null ? "" : t.trim();
    }
}
