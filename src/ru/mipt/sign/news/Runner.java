package ru.mipt.sign.news;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import ru.mipt.sign.core.exceptions.NeuronNotFound;
import ru.mipt.sign.core.exceptions.NextCommandException;

public class Runner
{

	/**
	 * @param args
	 * @throws NextCommandException 
	 * @throws ParseException 
	 * @throws IOException 
	 * @throws org.apache.lucene.queryparser.classic.ParseException 
	 * @throws NeuronNotFound 
	 */
	public static void main(String[] args) throws NextCommandException, IOException, ParseException, org.apache.lucene.queryparser.classic.ParseException, NeuronNotFound
	{
		New tmp = new New();
		
		
		
	//	System.out.println(tmp.getDateOfNew());
		//System.out.println(tmp.getVectorOfNewInSmallBasis());
		while (tmp.NextNew()) {
			
			//System.out.println(tmp.getDateOfNew());
	tmp.getVectorOfNewInSmallBasis();
			
		}
		//System.out.println(vectorslist);
tmp.updateFullBasis();
System.out.println(tmp.fullBasis.size());
	}

}
