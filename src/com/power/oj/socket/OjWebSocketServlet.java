package com.power.oj.socket;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;

@SuppressWarnings("serial")
public class OjWebSocketServlet extends WebSocketServlet
{
	private static ArrayList<OjMessageInbound> OjSocketList = new ArrayList<OjMessageInbound>();

	@Override
	protected StreamInbound createWebSocketInbound(String arg0, HttpServletRequest arg1)
	{
		// TODO Auto-generated method stub
		return new OjMessageInbound();
	}

	public static synchronized List<OjMessageInbound> getSocketList()
	{
		return OjSocketList;
	}

}
