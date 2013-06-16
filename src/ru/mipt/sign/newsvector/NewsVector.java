package ru.mipt.sign.newsvector;
import java.io.IOException;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.*;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;

import ru.mipt.sign.core.exceptions.NeuronNotFound;
import ru.mipt.sign.core.exceptions.NextCommandException;
import ru.mipt.sign.neurons.HopfieldNeuroNet;
import ru.mipt.sign.neurons.Neuron;
import ru.mipt.sign.neurons.data.impl.InputDataProviderByData;
import ru.mipt.sign.news.News;
import ru.mipt.sign.parser.RSSParser;

public class NewsVector {
	
	// Here we can build full term basis for all news 
	// or just build one vector from one new
	
	
	
	public static final FieldType TYPE_STORED = new FieldType();

	
	
	    static {
	        TYPE_STORED.setIndexed(true);
	        TYPE_STORED.setTokenized(true);
	        TYPE_STORED.setStored(true);
	        TYPE_STORED.setStoreTermVectors(true);
	        TYPE_STORED.setStoreTermVectorPositions(true);
	        TYPE_STORED.freeze();
	    }
	    
	
	
	/**
	 * @param args
	 * @throws ParseException 
	 * @throws IOException 
	 * @throws NextCommandException 
	 * @throws org.apache.lucene.queryparser.classic.ParseException 
	 * @throws NeuronNotFound 
	 */
	    
	   
	    public  Map<String,Double> GetTermsBasisForAllNews() throws NextCommandException, IOException, ParseException
	    {
	    	 Directory index = new RAMDirectory();
	    	 Map <String,Double> termsAndFreqs = new HashMap<String, Double>();
	    	
	    	 RSSParser rsspars = new RSSParser();
	    	 List<News> newsList = rsspars.LoadText();
			 List<String> StringList = new ArrayList<String>(); 
			 
			 for (Iterator<News> it = newsList.iterator(); it.hasNext();) {
				 StringList.add(it.next().getText());
			 }
			 
			 StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
		     // 1. create the index
		     IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_CURRENT, analyzer);
		     IndexWriter w = new IndexWriter(index, config);
		     
		     int documentId=0;
		     for (Iterator<String> it = StringList.iterator(); it.hasNext();)
	         {
		    	documentId++;
		    	String s = it.next().toLowerCase().replaceAll("\n|\r\n", " ");
		    	addDoc(w, s, Integer.toString(documentId));
	         }
		     w.close();
		     
	    	 IndexReader reader = DirectoryReader.open(index);
		   	      
		     for (int docId = 0;docId<reader.numDocs();docId++) {
		       	 Terms vector = reader.getTermVector(docId, "text");
		    	 TermsEnum termsEnum = null;
		    	 termsEnum = vector.iterator(termsEnum);
		    	 BytesRef text = null;
		    	 while ((text = termsEnum.next()) != null) {
		    		 String term = text.utf8ToString();
		    		 double freq = (double) termsEnum.totalTermFreq();
		    		 double oldfreq = termsAndFreqs.get(term) == null? 0 : termsAndFreqs.get(term);
		    	     termsAndFreqs.put(term, freq+oldfreq);		    	     
		    	 }
		     }
		    	     
		     reader.close();
		     
		     ValueComparator bvc =  new ValueComparator(termsAndFreqs);
		     TreeMap<String,Double> sorted_map = new TreeMap<String,Double>(bvc);
		     sorted_map.putAll(termsAndFreqs);
		     return sorted_map;
	    }
	    
	  
	    
	    
	    
	    
	    public  Map<String,Double> BuildFinalVectorForOneNew(News thisNew) throws IOException, ParseException, NextCommandException, org.apache.lucene.queryparser.classic.ParseException, NeuronNotFound
	    {
	    	//build frequencies of terms in this new
	    	//build the matrix of correlation for terms 
		    	
		    	 		    	 
		    	 Directory index2 = new RAMDirectory();
		    	 StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
			     IndexWriterConfig config2 = new IndexWriterConfig(Version.LUCENE_CURRENT, analyzer);
			     IndexWriter w2 = new IndexWriter(index2, config2);
			     addDoc(w2, thisNew.getText().replaceAll("\n|\r\n", " "), "0");
			  
			     //  w2.addDocument(reader.document(docId));  //adding current doc and search in it
			     w2.close();
			     
			     IndexReader reader2 = DirectoryReader.open(index2);
			     
			     Terms vector = reader2.getTermVector(0, "text");
		    	 TermsEnum termsEnum = null;
		    	 termsEnum = vector.iterator(termsEnum);
		    	 Map<String, Double> frequencies = new HashMap<String, Double>(); // freqs map
		    	 Map<String, Integer> termsInDoc = new HashMap<String, Integer>(); // terms map with Ids
		    	 int termId = 0;
		    	 BytesRef text = null;
		    	 while ((text = termsEnum.next()) != null) {
		    	     String term = text.utf8ToString();
		    	     int freq = (int) termsEnum.totalTermFreq();
		    	     Double freq2 = (double)freq;
		    	     frequencies.put(term, freq2);
		    	     termsInDoc.put(term, termId);
		    	     termId++;
		    	 }
		    	 //System.out.println(frequencies);
		    	 //System.out.println(termsInDoc);
		    	 double[][] corMatrix = new double[termsInDoc.size()][termsInDoc.size()];
			     for (String term1 : termsInDoc.keySet()) 
		    	 {
		    		 for (String term2 : termsInDoc.keySet())
		    		 {
		    			 if (term1!=term2) {
		    				 IndexSearcher searcher = new IndexSearcher(reader2);
			    			 String querystr = "\""+term1+" "+term2+"\"~6";
			    			 Query q = new QueryParser(Version.LUCENE_CURRENT, "text", analyzer).parse(querystr);  
			    			 TopDocs topDocs = searcher.search(q, 10);			    			    			 
			    			 if (topDocs.totalHits == 0)  corMatrix[termsInDoc.get(term1)][termsInDoc.get(term2)]=0;
			    			 else 
			    				 {
			    				 ScoreDoc match = topDocs.scoreDocs[0];
			    				 corMatrix[termsInDoc.get(term1)][termsInDoc.get(term2)]=match.score;
			    				 }
		    			 	 } else
		    			 {
		    				 corMatrix[termsInDoc.get(term1)][termsInDoc.get(term2)]=0;
		    			 }
		    			 
	
		    		 }
	    		 
	    		 
		    	 }
			     reader2.close();
		     
			     return BuildHopsfieldByVector(frequencies,termsInDoc,corMatrix);
		 }
	
	
	
	private static void addDoc(IndexWriter w, String text, String documentId) throws IOException {
	     Document doc = new Document();
	     Field field = new Field("text", text, TYPE_STORED);
	     doc.add(field);
	     doc.add(new StringField("documentId", documentId, Field.Store.YES));
	     w.addDocument(doc);
	   }
	
	public  Map<String,Double> BuildHopsfieldByVector( Map<String, Double> frequencies,  Map<String, Integer> termsInDoc, double[][] corMatrix ) throws IOException, ParseException, NextCommandException, org.apache.lucene.queryparser.classic.ParseException, NeuronNotFound
	{
		HopfieldNeuroNet net = new HopfieldNeuroNet(frequencies.size());
		List<BigInteger> neurons = net.getRealNeurons();
		Set<String> terms = frequencies.keySet();
		Iterator<String> it = terms.iterator();
		Map<String, BigInteger>  termsToNeurons = new HashMap<String, BigInteger>(); // neurons - terms connection
		Iterator<BigInteger> it2 = neurons.iterator();
		
		while (it.hasNext()) {
			termsToNeurons.put(it.next(),it2.next());
		}
		
		
		 for (String term1 : termsInDoc.keySet()) 
    	 {
    		 for (String term2 : termsInDoc.keySet())
    		 {
    			 if (term1!=term2) {
    				double weightFromMatrix = corMatrix[termsInDoc.get(term1)][termsInDoc.get(term2)];
    				if (weightFromMatrix!=0) {
	    				
						net.connectNeuron(termsToNeurons.get(term1), termsToNeurons.get(term2), 1)	;
						Neuron neur = net.getNeuron(termsToNeurons.get(term1));
						neur.setWeight(0, neur.getOutNumber()-1, weightFromMatrix);
	    				
    				}
	    		 }
	    			
    		 }
    	 }
		 
		 List<Double> inputData = new ArrayList<Double> (frequencies.values());
		 net.setInputProvider(new InputDataProviderByData(inputData, 1));
		 net.calc();
		 //System.out.println("input:"+inputData);
		// System.out.println("output1:"+net.getResult());
         double energy = net.getEnergy();
		 for (int k = 0; k< 10; k++)
		 {
		 List<Double> result = net.getResult();
		 net.setInputProvider(new InputDataProviderByData(result,1));
		 net.calc();

		 double currentEnergy = net.getEnergy();
        // System.out.println("energy: " + (energy - currentEnergy));
         energy = currentEnergy;
		 }
		// System.out.println("output2:"+net.getResult());
		//System.out.println(termsToNeurons);
		 Map<String,Double> outputMap = new HashMap<String, Double>();
		 it = terms.iterator();
		 Iterator<Double> it3 = net.getResult().iterator();
		 while (it.hasNext()) {
				outputMap.put(it.next(),it3.next());
			}
		 
		  return outputMap;
		
	}
	
}

class ValueComparator implements Comparator<String> {

    Map<String, Double> base;
    public ValueComparator(Map<String, Double> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.    
    public int compare(String a, String b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}