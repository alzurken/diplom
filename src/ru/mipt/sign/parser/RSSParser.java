package ru.mipt.sign.parser;

import java.io.*;
import java.net.URL;
import java.text.ParseException;
import java.util.*;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;

import ru.mipt.sign.core.exceptions.NextCommandException;
import ru.mipt.sign.neurons.NeuronConst;
import ru.mipt.sign.news.News;
import ru.mipt.sign.util.XmlFormatter;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

public class RSSParser implements NeuronConst
{
    private List<News> news_list = new ArrayList<News>();
    private Map <String, String> mapSiteDate = new HashMap(); 
    /* map site , date of latest new*/
    private URL url;
    private Element newsBaseXml = new Element("AllNews");
    Document doc;

    public RSSParser(String path) throws NextCommandException
    {
        if (!path.isEmpty())
        {
            path = DEFAULT_CONF_PATH + path + ".xml";
            SAXBuilder builder = new SAXBuilder();
            String data = "";

            File file = new File(path);
            int ch;
            StringBuffer strContent = new StringBuffer("");
            FileInputStream fin = null;
            try
            {
                fin = new FileInputStream(file);
                while ((ch = fin.read()) != -1)
                    strContent.append((char) ch);
                fin.close();
            } catch (Exception e)
            {
                System.out.println("File '" + path + "' not found");
                throw new NextCommandException();
            }

            data = strContent.toString();

            try
            {
                doc = builder.build(new ByteArrayInputStream(data.getBytes()));
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            System.out.println("Path for configuration is empty!");
        }
    }

    public List<News> LoadText() throws IOException, ParseException    // map of text and dates
    {
        List<News> news = new ArrayList<News>();
        try
        {
            XPath xpath;
            xpath = XPath.newInstance("//AllNews");
            Element nn = (Element) xpath.selectSingleNode(doc);

            List<Element> results = nn.getChildren();
            if (results.size() > 0)
            {
                for (Iterator<Element> it = results.iterator(); it.hasNext();)
                {
                    Element Site = it.next();

                    List<Element> res2 = Site.getChildren();

                    for (Iterator<Element> it2 = res2.iterator(); it2.hasNext();)
                    {
                    	Element curEl= it2.next();
                    	String text = curEl.getChild("text").getAttributeValue("text");
                    	String date = curEl.getChild("date").getAttributeValue("date");
                    	String source = curEl.getChild("source").getAttributeValue("source");
                    	News curNew = new News(text,date,source);
                    	
                        news.add(curNew);
                    }

                }
            }

        } catch (JDOMException e)
        {
            e.printStackTrace();
        }
        return news;

    }

    public void Save()
    {

        doc = new Document(newsBaseXml);
        String out = "";
        XMLOutputter outputter = new XMLOutputter();
        out = outputter.outputString(doc);
        File f = new File(DEFAULT_CONF_PATH + "News" + ".xml");
        f.setWritable(true);
        try
        {
            FileWriter fw = new FileWriter(f);
            fw.write(XmlFormatter.format(out));
            fw.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public List<News> getNews()
    {

        try
        {
            BufferedReader rssFile = new BufferedReader(new FileReader(DEFAULT_CONF_PATH + "rss.txt"));
            String rssSite = rssFile.readLine();
            while (rssSite != null)
            {
                Element rssSiteXml = new Element("Site"); // xml
                rssSiteXml.setAttribute("site", rssSite);
                URL url = new URL(rssSite);
                XmlReader reader = null;
                news_list = new ArrayList<News>();
                try
                {
            
                    reader = new XmlReader(url);
                    SyndFeed feed = new SyndFeedInput().build(reader);
            
                    for (Iterator<SyndEntry> i = feed.getEntries().iterator(); i.hasNext();)
                    {
                        SyndEntry entry = i.next();
            
                        String date = entry.getPublishedDate().toString();
                        String text = entry.getDescription().getValue().replaceAll("\\<.*?\\>", "")
                                .replaceAll("\\&.*?\\;", "");
                        News curNew = new News(text, date, rssSite);
                        rssSiteXml.addContent(curNew.getXml());// xml
                        news_list.add(curNew);
                    }
                }
                finally
                {
                    if (reader != null)
                        reader.close();
                }
                newsBaseXml.addContent(rssSiteXml);
                rssSite = rssFile.readLine();
            }
            rssFile.close();
        } catch (Exception e)
        {
            System.out.println("Error" + e.getMessage());
        }

        List<News> news = new ArrayList<News>(news_list);

        return news;
    }
}