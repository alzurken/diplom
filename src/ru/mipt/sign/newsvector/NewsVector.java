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
import ru.mipt.sign.parser.RSSParser;

public class NewsVector {
	
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
	public  void GetIndexOfNews() throws IOException, ParseException, NextCommandException, org.apache.lucene.queryparser.classic.ParseException, NeuronNotFound
	{
		RSSParser rsspars = new RSSParser();
		List<String> StringList = rsspars.LoadText();
		
		 // 0. Specify the analyzer for tokenizing text.
	     //    The same analyzer should be used for indexing and searching
	     StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);

	     // 1. create the index
	     Directory index = new RAMDirectory();
	     IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_CURRENT, analyzer);

	     IndexWriter w = new IndexWriter(index, config);
	     
	     int documentId=0;
	     for (Iterator<String> it = StringList.iterator(); it.hasNext();)
         {
	    	 documentId++;
	    	String s = it.next().replaceAll("\n|\r\n", " ");
	    	System.out.println(Integer.toString(documentId)+" "+s);
	    	addDoc(w, s, Integer.toString(documentId));
         }
	     
	     w.close();
	     
	     // 2. query
	    

	     // the "title" arg specifies the default field to use
	     // when no field is explicitly specified in the query.
	    
	     // 3. search
	     int hitsPerPage = 10;
	     IndexReader reader = DirectoryReader.open(index);
	     Set<String> terms = new HashSet<String>();    // базис термов
	     for (int docId = 0;docId<documentId;docId++) {
	    
	    	 Terms vector = reader.getTermVector(docId, "text");
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
	    	     terms.add(term);
	    	     
	    	 }
	    	
	    	 //from here building correaltion matrix
	    	 //working in 1 document not in all index
	    	 double[][] corMatrix = new double[termsInDoc.size()][termsInDoc.size()];
	    	 
	    	 
	    	 Directory index2 = new RAMDirectory();
		     IndexWriterConfig config2 = new IndexWriterConfig(Version.LUCENE_CURRENT, analyzer);
		     IndexWriter w2 = new IndexWriter(index2, config2);
		     w2.addDocument(reader.document(docId));  //adding current doc and search in it
		     w2.close();
		     IndexReader reader2 = DirectoryReader.open(index2);
	    	 
		     for (String term1 : termsInDoc.keySet()) 
	    	 {
	    		 for (String term2 : termsInDoc.keySet())
	    		 {
	    			 if (term1!=term2) {
	    				 IndexSearcher searcher = new IndexSearcher(reader2);
		    			 String querystr = "\""+term1+" "+term2+"\"~6";
		    			 Query q = new QueryParser(Version.LUCENE_CURRENT, "text", analyzer).parse(querystr);  
		    			// TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
		    		     //TotalHitCountCollector collector = new TotalHitCountCollector();
		    			 TopDocs topDocs = searcher.search(q, 10);
		    			//System.out.println(term1+" "+term2);
		    			
		    			 
		    			 for (int i = 0; i < topDocs.totalHits; i++) {
		    		            ScoreDoc match = topDocs.scoreDocs[i];
		    		            //System.out.println("match.score: " + match.score);
		    		            //Explanation explanation = searcher.explain(q, match.doc); //#1
		    		            //System.out.println("----------");
		    		           // Document doc = searcher.doc(match.doc);
		    		           // System.out.println(doc.get("text"));
		    		           // System.out.println(explanation.toString());
		    		        
		    			 }
		    			 if (topDocs.totalHits == 0)  corMatrix[termsInDoc.get(term1)][termsInDoc.get(term2)]=0;
		    			 else 
		    				 {
		    				 ScoreDoc match = topDocs.scoreDocs[0];
		    				 corMatrix[termsInDoc.get(term1)][termsInDoc.get(term2)]=match.score;
		    				 }
	    			 //searcher.search(q, collector);
   		     	   // ScoreDoc[] hits = collector.topDocs().scoreDocs;
		    		    
		    		    // System.out.println(collector.getTotalHits());
			    		    
	    			 } else
	    			 {
	    				 corMatrix[termsInDoc.get(term1)][termsInDoc.get(term2)]=0;
	    			 }
	    			 

	    		 }
	    		 
	    		 
	    	 }
	    	 /*
	    	 System.out.println(docId);
	    	 for (int i = 0; i <corMatrix.length; i++) {
	    		 for (int j = 0; j <corMatrix.length; j++) 
	    	 System.out.print(corMatrix[i][j]+"\t");
	    		 System.out.println();
	    	 }
*/
		     BuildHopsfieldByVector(frequencies,termsInDoc,corMatrix);
	    	 
	    	 
	    	    
	     
	     }
	     System.out.println(terms);
	     System.out.println(terms.size());
	         
	     reader.close();
	}
	
	
	
	private static void addDoc(IndexWriter w, String text, String documentId) throws IOException {
	     Document doc = new Document();
	     Field field = new Field("text", text, TYPE_STORED);
	     doc.add(field);
	     //doc.add(new TextField("text", text, Field.Store.YES));
	     doc.add(new StringField("documentId", documentId, Field.Store.YES));
	     // doc.addField(new Field("content", content, Store.YES, Index.ANALYZED, TermVector.WITH_POSITIONS_OFFSETS));

	     // use a string field for isbn because we don't want it tokenized
	     //doc.add(new StringField("isbn", isbn, Field.Store.YES));
	     w.addDocument(doc);
	   }
	
	public  void BuildHopsfieldByVector( Map<String, Double> frequencies,  Map<String, Integer> termsInDoc, double[][] corMatrix ) throws IOException, ParseException, NextCommandException, org.apache.lucene.queryparser.classic.ParseException, NeuronNotFound
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
		 System.out.println("input:"+inputData);
		 System.out.println("output1:"+net.getResult());
		 List<Double> result = net.getResult();
		 net.setInputProvider(new InputDataProviderByData(result,1));
		 net.calc();
		
		 System.out.println("output2:"+net.getResult());
		//System.out.println(termsToNeurons);
		  
		
	}

		
}
