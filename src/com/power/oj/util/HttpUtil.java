package com.power.oj.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jodd.util.StringUtil;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.jfinal.log.Logger;
import com.power.oj.core.OjConfig;

public class HttpUtil
{
  private final static Logger log = Logger.getLogger(HttpUtil.class);

  private static final String DEFAULT_CHARSET = "UTF-8";

  public static final String METHOD_POST = "POST";

  public static final String METHOD_GET = "GET";

  public static final int HTTPSTATUS_OK = 200;

  public static final int CONNECTIMEOUT = 5000;

  public static final int READTIMEOUT = 5000;

  public static String doGet(String url) throws HttpHostConnectException
  {
    return doGet(url, new HashMap<String, String>());
  }
  
  public static String doGet(String url, Map<String, String> params) throws HttpHostConnectException
  {
    if (StringUtil.isBlank(url))
    {
      return null;
    }
    String html = null;

    HttpClient httpclient = new DefaultHttpClient();
    try
    {
      url += "?" + buildQuery(params, DEFAULT_CHARSET);
      HttpGet httpGet = new HttpGet(url);
      log.info("executing request " + httpGet.getURI());

      // Create a response handler
      ResponseHandler<String> responseHandler = new BasicResponseHandler();
      html = httpclient.execute(httpGet, responseHandler);

    } catch (ClientProtocolException e)
    {
      if (OjConfig.getDevMode())
        e.printStackTrace();
      log.warn(e.getLocalizedMessage());
    } catch (IOException e)
    {
      if (OjConfig.getDevMode())
        e.printStackTrace();
      log.warn(e.getLocalizedMessage());
    } finally
    {
      httpclient.getConnectionManager().shutdown();
    }

    return html;
  }

  public static String doPost(String url, Map<String, String> params) throws HttpHostConnectException
  {
    if (StringUtil.isBlank(url))
    {
      return null;
    }
    String html = null;

    HttpClient httpclient = new DefaultHttpClient();
    try
    {
      HttpPost httpPost = new HttpPost(url);
      log.info("executing request " + httpPost.getURI());
      List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
      for (Entry<String, String> entry : params.entrySet())
      {
        String key = entry.getKey();
        String value = entry.getValue();
        if (StringUtil.isNotBlank(key) && StringUtil.isNotBlank(value))
        {
          paramsList.add(new BasicNameValuePair(key, value));
        }
      }

      UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramsList);
      httpPost.setEntity(entity);

      // Create a response handler
      ResponseHandler<String> responseHandler = new BasicResponseHandler();
      html = httpclient.execute(httpPost, responseHandler);

    } catch (ClientProtocolException e)
    {
      if (OjConfig.getDevMode())
        e.printStackTrace();
      log.warn(e.getLocalizedMessage());
    } catch (IOException e)
    {
      if (OjConfig.getDevMode())
        e.printStackTrace();
      log.warn(e.getLocalizedMessage());
    } finally
    {
      httpclient.getConnectionManager().shutdown();
    }

    return html;
  }

  public static String buildQuery(Map<String, String> params, String charset)
  {
    if (params == null || params.isEmpty())
    {
      return null;
    }
    StringBuilder sb = new StringBuilder();
    boolean first = true;
    for (Entry<String, String> entry : params.entrySet())
    {
      if (first)
      {
        first = false;
      } else
      {
        sb.append("&");
      }
      String key = entry.getKey();
      String value = entry.getValue();
      if (StringUtil.isNotBlank(key) && StringUtil.isNotBlank(value))
      {
        try
        {
          sb.append(key).append("=").append(URLEncoder.encode(value, charset));
        } catch (UnsupportedEncodingException e)
        {
        }
      }
    }
    return sb.toString();

  }

  public static Map<String, String> splitQuery(String query, String charset)
  {
    Map<String, String> ret = new HashMap<String, String>();
    if (StringUtil.isNotBlank(query))
    {
      String[] splits = query.split("\\&");
      for (String split : splits)
      {
        String[] keyAndValue = split.split("\\=");
        if (keyAndValue.length == 2 && StringUtil.isNotBlank(keyAndValue[0]) && StringUtil.isNotBlank(keyAndValue[1]))
        {
          try
          {
            ret.put(keyAndValue[0], URLDecoder.decode(keyAndValue[1], charset));
          } catch (UnsupportedEncodingException e)
          {
          }
        }
      }
    }
    return ret;
  }

}
