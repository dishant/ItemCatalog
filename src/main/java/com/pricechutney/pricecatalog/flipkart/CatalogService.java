package com.pricechutney.pricecatalog.flipkart;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.pricechutney.pricecatalog.utils.DBUtils;


public class CatalogService
{
	private static String sitemap="http://www.flipkart.com/sitemap/sitemap_index.xml";
	
	public static void createCatalog()
	{
		try
		{
			List<SiteMap> siteMap = XMLParser.getSubSiteMaps(sitemap);
			List<String> XMLFiles = downloadGzipFiles(siteMap);
			
			String outFileName = "out.txt";
			XMLParser.writeItemList(XMLFiles,outFileName);
			DataUpload.uploadData(DBUtils.getFlipkartDBConnection(),outFileName);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	public static List<String> downloadGzipFiles(List<SiteMap> siteMap) throws IOException, InterruptedException
	{
		List<String> XMLFiles = Collections.synchronizedList(new ArrayList<String>());
		
		List threads = new ArrayList();
		
		for(int i=0; i<siteMap.size(); i++)
		{
			DownloadGzipFiles dgzf = new DownloadGzipFiles(new URL(siteMap.get(i).getLoc()), XMLFiles);
		    dgzf.start();
		    threads.add(dgzf);
		}
		
		for(int i=0;i<threads.size();i++)
		{
			/* Wait 10 minutes to finish */
			((Thread)threads.get(i)).join(600000);
		}
		
		return XMLFiles;
	}
	
	
	
}
