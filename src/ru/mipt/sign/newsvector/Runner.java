package ru.mipt.sign.newsvector;

import java.io.IOException;
import java.text.ParseException;

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
		NewsVector tmp= new NewsVector();
		//System.out.println(tmp.GetTermBasis());


	}

}
