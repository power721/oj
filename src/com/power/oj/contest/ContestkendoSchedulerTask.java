package com.power.oj.contest;

public class ContestkendoSchedulerTask
{
	private String taskId;
	private String oj;
	private String title;
	private String url;
	private String start;
	private String end;
	private String startTimezone;
	private String endTimezone;
	private String description = "";
	private String recurrenceId;
	private String recurrenceRule;
	private String recurrenceException;
	private boolean isAllDay = false;

	public String getTaskId()
	{
		return taskId;
	}

	public void setTaskId(String taskId)
	{
		this.taskId = taskId;
	}

	public String getOj()
	{
		return oj;
	}

	public void setOj(String oj)
	{
		this.oj = oj;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getStart()
	{
		return start;
	}

	public void setStart(String start)
	{
		this.start = start;
	}

	public String getEnd()
	{
		return end;
	}

	public void setEnd(String end)
	{
		this.end = end;
	}

	public String getStartTimezone()
	{
		return startTimezone;
	}

	public void setStartTimezone(String startTimezone)
	{
		this.startTimezone = startTimezone;
	}

	public String getEndTimezone()
	{
		return endTimezone;
	}

	public void setEndTimezone(String endTimezone)
	{
		this.endTimezone = endTimezone;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getRecurrenceId()
	{
		return recurrenceId;
	}

	public void setRecurrenceId(String recurrenceId)
	{
		this.recurrenceId = recurrenceId;
	}

	public String getRecurrenceRule()
	{
		return recurrenceRule;
	}

	public void setRecurrenceRule(String recurrenceRule)
	{
		this.recurrenceRule = recurrenceRule;
	}

	public String getRecurrenceException()
	{
		return recurrenceException;
	}

	public void setRecurrenceException(String recurrenceException)
	{
		this.recurrenceException = recurrenceException;
	}

	public boolean isAllDay()
	{
		return isAllDay;
	}

	public void setAllDay(boolean isAllDay)
	{
		this.isAllDay = isAllDay;
	}

}
