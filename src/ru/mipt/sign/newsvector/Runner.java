package ru.mipt.sign.newsvector;

import java.io.IOException;
import java.text.ParseException;

import ru.mipt.sign.core.exceptions.NextCommandException;

public class Runner
{

	/**
	 * @param args
	 * @throws NextCommandException 
	 * @throws ParseException 
	 * @throws IOException 
	 * @throws org.apache.lucene.queryparser.classic.ParseException 
	 */
	public static void main(String[] args) throws NextCommandException, IOException, ParseException, org.apache.lucene.queryparser.classic.ParseException
	{
		NewsVector tmp= new NewsVector();
		tmp.GetIndexOfNews();


	}

}
