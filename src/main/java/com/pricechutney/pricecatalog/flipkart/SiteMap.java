package com.pricechutney.pricecatalog.flipkart;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class SiteMap
{
	public SiteMap(String loc, Date lastmod, Double priority)
	{
		super();
		this.loc = loc;
		this.lastmod = lastmod;
		this.priority = priority;
	}
	
	public SiteMap(String loc, String lastmod, String priority)
	{
		super();
		this.loc = loc;
		try
		{
			this.lastmod = new SimpleDateFormat("yyyy-MM-dd").parse(lastmod);
		} catch (ParseException e)
		{
			this.lastmod = null;
		}
		try
		{
			this.priority = Double.parseDouble(priority);
		}
		catch (NullPointerException | NumberFormatException e)
		{
			this.priority = null;
		}
	}
	
	
	private String loc;
	private Date lastmod;
	private Double priority;
	
	
	public String getLoc()
	{
		return loc;
	}
	public void setLoc(String loc)
	{
		this.loc = loc;
	}
	public Date getLastmod()
	{
		return lastmod;
	}
	public void setLastmod(Date lastmod)
	{
		this.lastmod = lastmod;
	}
	public Double getPriority()
	{
		return priority;
	}
	public void setPriority(Double priority)
	{
		this.priority = priority;
	}

}
