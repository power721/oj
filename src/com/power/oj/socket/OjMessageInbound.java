package com.power.oj.socket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

import jodd.util.StringBand;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.WsOutbound;

import com.jfinal.log.Logger;

public class OjMessageInbound extends MessageInbound
{
	private static final Logger log = Logger.getLogger(OjMessageInbound.class);
	
	protected void onOpen(WsOutbound outbound)
	{
		OjWebSocketServlet.getSocketList().add(this);
		log.info("Client open.");
	}

	protected void onClose(int status)
	{
		OjWebSocketServlet.getSocketList().remove(this);
		log.info("Client close.");
	}

	@Override
	protected void onBinaryMessage(ByteBuffer arg0) throws IOException
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onTextMessage(CharBuffer arg0) throws IOException
	{
		// TODO Auto-generated method stub
		log.info(new StringBand(2).append("Accept Message: ").append(arg0).toString());
		broadcast(arg0.toString());
	}
	
	public static void broadcast(String textMessage) throws IOException
	{
		for(OjMessageInbound message : OjWebSocketServlet.getSocketList())
		{
			 CharBuffer buffer = CharBuffer.wrap(textMessage);
			 WsOutbound outbound = message.getWsOutbound();
			 
			 outbound.writeTextMessage(buffer);
			 outbound.flush();
		}
	}

}
