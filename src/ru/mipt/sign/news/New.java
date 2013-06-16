package ru.mipt.sign.news;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ru.mipt.sign.core.exceptions.NeuronNotFound;
import ru.mipt.sign.core.exceptions.NextCommandException;
import ru.mipt.sign.newsvector.NewsVector;
import ru.mipt.sign.parser.RSSParser;

public class New 
{

    private Iterator<News> newsIt;
    private News curNew;

    
    public New() throws NextCommandException, IOException, ParseException {
    	RSSParser rsspars = new RSSParser();
   	    List<News> newsList = rsspars.LoadText();
   	    newsIt = newsList.iterator();
   	    curNew = newsIt.next();
    }
    
    
    public String getDateOfNew()
    {
        return curNew.getDate();
    }

    public Map<String,Double> getVectorOfNewInNewBasis() throws NextCommandException, NeuronNotFound, IOException, ParseException, org.apache.lucene.queryparser.classic.ParseException 
    {
    	NewsVector tmp = new NewsVector();
        return tmp.BuildFinalVectorForOneNew(curNew);
    }
    
    public List<Double> getVectorOfNewInFullBasis() throws NextCommandException, NeuronNotFound, IOException, ParseException, org.apache.lucene.queryparser.classic.ParseException 
    {
    	NewsVector tmp = new NewsVector();
    	Map<String, Double> mapInNewBasis = tmp.BuildFinalVectorForOneNew(curNew);
        Map<String,Double> fullBasis = tmp.GetTermsBasisForAllNews();
        Map<String,Double> mergedMap = new HashMap();
        mergedMap.putAll(fullBasis);
        mergedMap.putAll(mapInNewBasis);
        //System.out.println(mapInNewBasis);
        //System.out.println(fullBasis.keySet());
        // System.out.println(mergedMap.keySet());
        List<Double> thisOutput = new ArrayList(mergedMap.values());
        
    	return thisOutput;
    }
    
    public boolean NextNew () {
    	if (newsIt.hasNext()) {
    	curNew = newsIt.next();
    		return true;
    	} else
    		return false;
    	
    }

    
   


}
