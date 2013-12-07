package com.power.oj.contest;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

import jodd.util.HtmlEncoder;
import jodd.util.StringBand;

import com.jfinal.log.Logger;

@ServerEndpoint(value = "/contest/rank.ws")
public class ContestRankWebSocket
{
  private static final Logger log = Logger.getLogger(ContestRankWebSocket.class);
  private static final Set<ContestRankWebSocket> connections = new CopyOnWriteArraySet<ContestRankWebSocket>();
  private Session session;
  private int cid = 0;

  public ContestRankWebSocket()
  {

  }

  @OnOpen
  public void start(Session session)
  {
    this.session = session;
    connections.add(this);
    cid = Integer.parseInt(session.getQueryString());
    log.info("WebSocket client open.");
  }

  @OnClose
  public void end()
  {
    connections.remove(this);

    log.info("WebSocket client close.");
  }

  @OnMessage
  public void incoming(String message)
  {
    broadcast(cid, HtmlEncoder.block(message));

    log.info(new StringBand(2).append("Accept Message: ").append(message).toString());
  }

  @OnError
  public void onError(Throwable t) throws Throwable
  {
    log.error(new StringBand(2).append("WebSocket Error: ").append(t.toString()).toString(), t);
  }

  public static void broadcast(int cid, String msg)
  {
    for (ContestRankWebSocket client : connections)
    {
      if (cid != 0 && cid != client.cid)
        continue;

      try
      {
        synchronized (client)
        {
          client.session.getBasicRemote().sendText(msg);
        }
      } catch (IOException e)
      {
        log.warn("WebSocket Error: Failed to send message to client", e);
        connections.remove(client);
        try
        {
          client.session.close();
        } catch (IOException e1)
        {
          // Ignore
        }
        // String message = String.format("* %s %s",
        // client.nickname, "has been disconnected.");
        // broadcast(message);
      }
    }
  }
}
