package ru.mipt.sign.news;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ru.mipt.sign.core.exceptions.NextCommandException;
import ru.mipt.sign.newsvector.NewsVector;
import ru.mipt.sign.parser.RSSParser;

public class NewsContainer
{
    private List<RealNew> container = new ArrayList<RealNew>();
    private Map<String, Double> fullBasis;
    
    public List<RealNew> getNews()
    {
        return container;
    }

    public RealNew getLastNew()
    {
        RSSParser parser;
        List <News> news = new ArrayList<News>();
        try
        {
            parser = new RSSParser("New");
            news = parser.LoadText();
        } catch (NextCommandException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ParseException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        
        NewsVector vector = new NewsVector();
        News n = news.get(0);
        return new RealNew(vector.BuildFinalVectorForOneNew(n), n.getDate());
    }
    
    public Map<String, Double> getFullBasis()
    {
        return fullBasis;
    }

    public NewsContainer()
    {
        RSSParser parser;
        List <News> news = new ArrayList<News>();
        try
        {
            parser = new RSSParser("News");
            news = parser.LoadText();
        } catch (NextCommandException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ParseException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        
        NewsVector vector = new NewsVector();
        for (News n : news)
        {
            container.add(new RealNew(vector.BuildFinalVectorForOneNew(n), n.getDate()));
        }
        fullBasis = vector.GetSortedFullBasisFreq();
    }
}
