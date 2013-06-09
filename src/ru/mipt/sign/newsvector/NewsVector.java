package ru.mipt.sign.newsvector;
import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jdom.Element;

import ru.mipt.sign.core.exceptions.NextCommandException;
import ru.mipt.sign.parser.RSSParser;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.DocsEnum;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.AtomicReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.SlowCompositeReaderWrapper;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.similarities.DefaultSimilarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.search.similarities.TFIDFSimilarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;

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
	 */
	public  void GetIndexOfNews() throws IOException, ParseException, NextCommandException, org.apache.lucene.queryparser.classic.ParseException
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
	    	//System.out.println(Integer.toString(documentId)+" "+s);
	    	addDoc(w, s, Integer.toString(documentId));
         }
	     
	     w.close();
	     
	     // 2. query
	     String querystr = "firm";

	     // the "title" arg specifies the default field to use
	     // when no field is explicitly specified in the query.
	     Query q = new QueryParser(Version.LUCENE_CURRENT, "text", analyzer).parse(querystr);

	     // 3. search
	     int hitsPerPage = 10;
	     IndexReader reader = DirectoryReader.open(index);
	     Set<String> terms = new HashSet<String>();
	     for (int docId = 1;docId<documentId;docId++) {
	    
	    	 Terms vector = reader.getTermVector(docId, "text");
	    	 TermsEnum termsEnum = null;
	    	 termsEnum = vector.iterator(termsEnum);
	    	 Map<String, Integer> frequencies = new HashMap<String, Integer>();
	    	
	    	 BytesRef text = null;
	    	 while ((text = termsEnum.next()) != null) {
	    	     String term = text.utf8ToString();
	    	     int freq = (int) termsEnum.totalTermFreq();
	    	     frequencies.put(term, freq);
	    	     terms.add(term);
	    	 }
	    	     
	     }
	     
	     System.out.println(terms);
	     
	     IndexSearcher searcher = new IndexSearcher(reader);
	     TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
	     searcher.search(q, collector);
	     ScoreDoc[] hits = collector.topDocs().scoreDocs;
	     
	     // 4. display results
	     System.out.println("Found " + hits.length + " hits.");
	     for(int i=0;i<hits.length;++i) {
	       int docId = hits[i].doc;
	       Document d = searcher.doc(docId);
	       System.out.println((i + 1) + ". " + d.get("documentId") + "\t" + d.get("text"));
	     }

	     // reader can only be closed when there
	     // is no need to access the documents any more.
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

}
