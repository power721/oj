package com.power.oj.user.interceptor;

public class AutoLoginException extends RuntimeException
{

  /**
   * 
   */
  private static final long serialVersionUID = -4604851790601777914L;

  public AutoLoginException()
  {
    super();
  }

  public AutoLoginException(String message, Throwable cause)
  {
    super(message, cause);
  }

  public AutoLoginException(String message)
  {
    super(message);
  }

  public AutoLoginException(Throwable cause)
  {
    super(cause);
  }

}
