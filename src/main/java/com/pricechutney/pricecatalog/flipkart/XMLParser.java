package com.pricechutney.pricecatalog.flipkart;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XMLParser
{
	/**
	 * We create a Big File to be 
	 * loaded into MySQL DB
	 * Separate Fields by Tab, Rows by NewLine 
	 * @param XMLFiles
	 * @return
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	public static void writeItemList(List<String> XMLFiles, String filename) throws ParserConfigurationException, SAXException, IOException
	{
		
		File file = new File(filename);

		// if file doesnt exists, then create it
		if (!file.exists()) {
			file.createNewFile();
		}

		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
		for(int i=0; i<XMLFiles.size(); i++)
		{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(XMLFiles.get(i)));
			Document doc = dBuilder.parse(is);
			
			doc.getDocumentElement().normalize();  
			NodeList nodeList = doc.getElementsByTagName("loc");
			
			for (int j=0; j<nodeList.getLength(); j++) 
			{
				Node node = nodeList.item(j);
				
				/* Find URL Value */
				String nodeURL = "";
				NodeList childNodes = node.getChildNodes();
				for(int k=0; k<childNodes.getLength(); k++)
				{
					Node data = childNodes.item(k);
					if (data.getNodeType() == Node.TEXT_NODE)
						nodeURL = data.getNodeValue();
				}
				
				/* Parse the URL */
				String itemString = parseItemURL(nodeURL);
				if(itemString != null)
				{
					bw.write(itemString);
				}
			}
		}
		
		bw.close();
	}
	
	
	private static String parseItemURL(String itemURL)
	{

		String name;
		String id;
		String finalURL;
		
		URL url;
		try
		{
			url = new URL(itemURL);
		} catch (MalformedURLException e)
		{
			return null;
		}
		
		String[] splits = url.getPath().split("/");
		
		if(splits.length < 4) return null;
		else
		{
			name = splits[1].replace('-', ' ');
			id = splits[3];
			finalURL = url.getProtocol()+"://"+url.getHost() + url.getPath();
		}
		
		return id+'\t'+name+'\t'+finalURL+'\n';
	}
	
	
	
	/**
	 * We parse the top level SiteMap domain
	 * and create Sublevel Domains
	 * for flipkart sitemap
	 * @param sitemap
	 * @return
	 * @throws IOException
	 * @throws XMLStreamException
	 */
	public static List<SiteMap> getSubSiteMaps(String sitemap) throws IOException, XMLStreamException
	{
		URL url = new URL(sitemap);
		URLConnection conn = url.openConnection();
		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		
		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLStreamReader reader = factory.createXMLStreamReader(br);
		
		String tagContent=null;
		String loc = null;
		String lastmod = null;
		String priority = null;
		
		List<SiteMap> siteMaps = new ArrayList<SiteMap>();
		
		while(reader.hasNext())
		{
			int event = reader.next();
			
			switch(event)
			{
				case XMLStreamConstants.START_ELEMENT:
					if("sitemap".equals(reader.getLocalName()))
					{
						tagContent=null;
						loc = null;
						lastmod = null;
						priority = null;
					}
					break;
				case XMLStreamConstants.CHARACTERS:
					tagContent = reader.getText().trim();
					break;
				case XMLStreamConstants.END_ELEMENT:
					switch(reader.getLocalName())
					{
						case "sitemap":
							siteMaps.add(new SiteMap(loc,lastmod,priority));
							break;
						case "loc":
							loc = tagContent;
							break;
						case "lastmod":
							lastmod = tagContent;
							break;
						case "priority":
							priority = tagContent;
							break;
					}
					break;
				case XMLStreamConstants.START_DOCUMENT:
					break;
			}
		}
		
		return siteMaps;
	}
}
