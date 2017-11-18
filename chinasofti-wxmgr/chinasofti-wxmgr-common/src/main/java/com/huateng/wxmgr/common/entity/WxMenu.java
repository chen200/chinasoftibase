package com.huateng.wxmgr.common.entity;

import java.io.Serializable;
import java.util.List;

import com.huateng.wxmgr.common.utils.PageBean;

public class WxMenu extends PageBean implements Serializable{
    private String ids;

    private String urltoken;

    private String appid;

    private Byte level;

    private String pid;

    private String title;

    private String keyword;

    private String url;

    private Byte isShow;

    private Byte sort;

    private String gid;
    
    private int id;
    
    private List<WxMenu> children;
    
    
    public List<WxMenu> getChildren() {
		return children;
	}

	public void setChildren(List<WxMenu> children) {
		this.children = children;
	}

	

    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIds() {
        return ids;
    }   
    

	public void setIds(String ids) {
        this.ids = ids == null ? null : ids.trim();
    }

    public String getUrltoken() {
        return urltoken;
    }

    public void setUrltoken(String urltoken) {
        this.urltoken = urltoken == null ? null : urltoken.trim();
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid == null ? null : appid.trim();
    }

    public Byte getLevel() {
        return level;
    }

    public void setLevel(Byte level) {
        this.level = level;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid == null ? null : pid.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword == null ? null : keyword.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public Byte getIsShow() {
        return isShow;
    }

    public void setIsShow(Byte isShow) {
        this.isShow = isShow;
    }

    public Byte getSort() {
        return sort;
    }

    public void setSort(Byte sort) {
        this.sort = sort;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid == null ? null : gid.trim();
    }

	@Override
	public String toString() {
		return "WxMenu [ids=" + ids + ", urltoken=" + urltoken + ", appid=" + appid + ", level=" + level + ", pid="
				+ pid + ", title=" + title + ", keyword=" + keyword + ", url=" + url + ", isShow=" + isShow + ", sort="
				+ sort + ", gid=" + gid + "]";
	}
    
}