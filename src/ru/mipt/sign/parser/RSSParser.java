package ru.mipt.sign.parser;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ru.mipt.sign.news.INew;
import ru.mipt.sign.news.New;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

public class RSSParser
{
    private List<SyndEntry> news_list = new ArrayList<SyndEntry>();
    private URL url;

    public RSSParser(URL url) {
        this.url = url;
    }

    public List<INew> getNews()
    {
        List<INew> news = new ArrayList<INew>();
        try
        {
            Update();
        } catch (Exception e)
        {
        }

        for (SyndEntry se : news_list)
        {
            news.add(new New(se.getDescription().getValue().replaceAll("\\<.*?\\>", "").replaceAll("\\&.*?\\;", "")));
        }

        return news;
    }

    public URL getUrl()
    {
        return url;
    }

    public void setUrl(URL url)
    {
        this.url = url;
    }

    private void Update() throws Exception
    {

        URL url = new URL("http://finansy.blog.ru/rss");
        XmlReader reader = null;
        news_list = new ArrayList<SyndEntry>();
        try
        {

            reader = new XmlReader(url);
            SyndFeed feed = new SyndFeedInput().build(reader);
            // System.out.println("Feed Title: " + feed.getAuthor());

            for (Iterator<SyndEntry> i = feed.getEntries().iterator(); i.hasNext();)
            {
                news_list.add(i.next());
                SyndEntry entry = i.next();
                System.out.println(entry.getPublishedDate());
                System.out.println(entry.getTitle());
                System.out.println(entry.getDescription().getValue().replaceAll("\\<.*?\\>", "")
                        .replaceAll("\\&.*?\\;", ""));

            }
        }
        finally
        {
            if (reader != null)
                reader.close();
        }
    }
}