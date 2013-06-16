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
    private NewsVector tmp = new NewsVector();
    public  Map<String,Double> fullBasis;

    
    public New() throws NextCommandException, IOException, ParseException {
    	RSSParser rsspars = new RSSParser();
   	    List<News> newsList = rsspars.LoadText();
   	    newsIt = newsList.iterator();
   	    curNew = newsIt.next();
    }
    
    public void updateFullBasis() throws NextCommandException, IOException, ParseException
    {
    	this.fullBasis = tmp.GetSortedFullBasisFreq();
    }

    
    public String getDateOfNew()
    {
        return curNew.getDate();
    }

    public Map<String,Double> getVectorOfNewInSmallBasis() throws NextCommandException, NeuronNotFound, IOException, ParseException, org.apache.lucene.queryparser.classic.ParseException 
    {
    	  return tmp.BuildFinalVectorForOneNew(curNew);
    }
    /*
    public List<Double> extendVectorOfNewToFullBasis(Map<String,Double> VectorOfNew) throws NextCommandException, NeuronNotFound, IOException, ParseException, org.apache.lucene.queryparser.classic.ParseException 
    {
    	        
        Map<String,Double> mergedMap = new HashMap<String, Double>();
        mergedMap.putAll(fullBasis);
        mergedMap.putAll(VectorOfNew);
        //System.out.println(mapInNewBasis);
        //System.out.println(fullBasis.keySet());
        // System.out.println(mergedMap.keySet());
        List<Double> thisOutput = new ArrayList<Double>(mergedMap.values());
        
    	return thisOutput;
    }
    */
    
    public boolean NextNew () {
    	if (newsIt.hasNext()) {
    	curNew = newsIt.next();
    		return true;
    	} else
    		return false;
    	
    }

    
   


}
