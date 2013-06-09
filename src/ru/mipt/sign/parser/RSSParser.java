package ru.mipt.sign.parser;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;

import ru.mipt.sign.core.exceptions.NextCommandException;
import ru.mipt.sign.neurons.NeuronConst;
import ru.mipt.sign.news.INews;
import ru.mipt.sign.news.News;
import ru.mipt.sign.util.XmlFormatter;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

public class RSSParser {
	private List<INews> news_list = new ArrayList<INews>();
	private URL url;
	private String path = NeuronConst.DEFAULT_CONF_PATH + "News" + ".xml";
	private Element newsBaseXml = new Element("AllNews");
	Document doc;

	public RSSParser() throws NextCommandException {
		if (!path.isEmpty()) {
			SAXBuilder builder = new SAXBuilder();
			String data = "";

			File file = new File(path);
			int ch;
			StringBuffer strContent = new StringBuffer("");
			FileInputStream fin = null;
			try {
				fin = new FileInputStream(file);
				while ((ch = fin.read()) != -1)
					strContent.append((char) ch);
				fin.close();
			} catch (Exception e) {
				System.out.println("File '" + path + "' not found");
				throw new NextCommandException();
			}

			data = strContent.toString();

			try {
				doc = builder.build(new ByteArrayInputStream(data.getBytes()));
				// System.out.println(doc);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Path for configuration is empty!");
		}
	}

	public List<String> LoadText() throws IOException, ParseException {
		List<String> news = new ArrayList<String>();
		try {
			XPath xpath;
			xpath = XPath.newInstance("//AllNews");
			Element nn = (Element) xpath.selectSingleNode(doc);

			List<Element> results = nn.getChildren();
			if (results.size() > 0) {
				for (Iterator<Element> it = results.iterator(); it.hasNext();) {
					Element Site = it.next();

					// System.out.println(Site.getAttributes());
					List<Element> res2 = Site.getChildren();

					for (Iterator<Element> it2 = res2.iterator(); it2.hasNext();) {
						news.add(it2.next().getChild("text")
								.getAttributeValue("text"));

						// System.out.println(it2.next().getChild("text").getAttributeValue("text"));
						/*
						 * List<Element> res3 = it2.next().getChildren(); for
						 * (Iterator<Element> it3 = res3.iterator();
						 * it3.hasNext();) { Element tmp2 = it3.next(); //Date
						 * date2 = new
						 * SimpleDateFormat().parse(tmp2.getAttribute
						 * ("date").toString()); //news.add( new
						 * News(tmp2.getAttribute("text").toString(), date2,
						 * tmp2.getAttribute("source").toString() ));
						 * System.out.println(tmp2); }
						 */
					}

				}
			}

		} catch (JDOMException e) {
			e.printStackTrace();
		}
		return news;

	}

	public void Save() {

		doc = new Document(newsBaseXml);
		String out = "";
		XMLOutputter outputter = new XMLOutputter();
		out = outputter.outputString(doc);
		File f = new File(NeuronConst.DEFAULT_CONF_PATH + "News" + ".xml");
		f.setWritable(true);
		try {
			FileWriter fw = new FileWriter(f);
			fw.write(XmlFormatter.format(out));
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<INews> getNews() {

		try {
			Update();
		} catch (Exception e) {
			System.out.println("Error" + e.getMessage());
		}

		List<INews> news = new ArrayList<INews>(news_list);

		return news;
	}

	private void Update() throws Exception {

		BufferedReader rssFile = new BufferedReader(new FileReader(
				NeuronConst.DEFAULT_CONF_PATH + "rss.txt"));
		String rssSite = rssFile.readLine();
		while (rssSite != null) {
			Element rssSiteXml = new Element("Site"); // xml
			rssSiteXml.setAttribute("site", rssSite);
			URL url = new URL(rssSite);
			XmlReader reader = null;
			news_list = new ArrayList<INews>();
			try {

				reader = new XmlReader(url);
				SyndFeed feed = new SyndFeedInput().build(reader);
				// System.out.println("Feed Title: " + feed.getAuthor());

				for (Iterator<SyndEntry> i = feed.getEntries().iterator(); i
						.hasNext();) {
					SyndEntry entry = i.next();

					Date date = entry.getPublishedDate();
					String text = entry.getDescription().getValue()
							.replaceAll("\\<.*?\\>", "")
							.replaceAll("\\&.*?\\;", "");
					News curNew = new News(text, date, rssSite);
					rssSiteXml.addContent(curNew.getXml());// xml
					news_list.add(curNew);
				}
			} finally {
				if (reader != null)
					reader.close();
			}
			newsBaseXml.addContent(rssSiteXml);
			rssSite = rssFile.readLine();
		}
		rssFile.close();
	}
}