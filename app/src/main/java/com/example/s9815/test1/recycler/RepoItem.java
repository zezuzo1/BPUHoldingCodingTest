package com.example.s9815.test1.recycler;

import com.example.s9815.test1.model.*;

import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public class RepoItem {
    private JSONObject m_jsonobj;

    public RepoItem( JSONObject ele )
    {
    	m_jsonobj = ele;
    }

    public String getTitle()
    {
    	String retval = "";
		try {
			retval = (String)m_jsonobj.get("name");
		} catch (Exception err)
		{
			retval = "(error)";
		}
    	return retval;
    }

    public String getSubTitle()
    {
    	String retval = "";
		try {
			retval = (String)m_jsonobj.get("description");
		} catch (Exception err)
		{
			retval = "(error)";
		}
    	return retval;
    }

    public String getThumbnail()
    {
    	String retval = "";
		try {
			retval = (String)GlobalFunction.sharedInstance.profile.get("avatar_url");
		} catch (Exception err)
		{
			retval = "(error)";
		}
    	return retval;
    }

    public String getTargetUrl()
    {
    	String retval = "";

		try {
			retval = (String)m_jsonobj.get("homepage");
		} catch (Exception err)
		{
			retval = "";
		}

    	return retval;
    }

    public String getStarCount()
    {
        String retval = "";

        try {
            retval = "Star Count : " + Integer.toString((int)m_jsonobj.get("stargazers_count"));
        } catch (Exception err)
        {
            retval = "";
        }

        return retval;
    }
}
