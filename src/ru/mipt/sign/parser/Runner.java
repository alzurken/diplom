package ru.mipt.sign.parser;

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
	 */
	public static void main(String[] args) throws NextCommandException, IOException, ParseException
	{
		RSSParser test = new RSSParser();
		test.LoadText();
		System.out.println(test.LoadText()); 
		//test.getNews();
		//test.Save();
		// TODO Auto-generated method stub

	}

}