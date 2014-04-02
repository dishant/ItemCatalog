package com.pricechutney.pricecatalog.flipkart;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.zip.GZIPInputStream;

public class DownloadGzipFiles extends Thread
{
	URL url;
	List<String> synchronizedListXML;
	
	public DownloadGzipFiles(URL url, List<String> synchronizedListXML)
	{
		this.url = url;
		this.synchronizedListXML = synchronizedListXML;
	}
	
	public void run()
	{
		try
		{
			URLConnection con = url.openConnection();
			GZIPInputStream gzis = new GZIPInputStream(con.getInputStream());			
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			
			byte[] buffer = new byte[40960];
			
			while (gzis.available() > 0)
			{
				int len = gzis.read(buffer);
				if(len < 0) break;
				out.write(buffer, 0, len);
			}
			
			String XMLFile = out.toString("UTF-8");
			synchronizedListXML.add(XMLFile);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
