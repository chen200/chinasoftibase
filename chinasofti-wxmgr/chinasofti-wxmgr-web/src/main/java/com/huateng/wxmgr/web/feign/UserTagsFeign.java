package com.huateng.wxmgr.web.feign;

import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.huateng.wxmgr.web.hystrix.UserTagsFeignHystrix;

@FeignClient(name="chinasofti-wxmgr-userservice",url="http://localhost:7701",fallback=UserTagsFeignHystrix.class)
public interface UserTagsFeign {
	/**
	 * 分页查询
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/usertags/findTagsByPage",method=RequestMethod.POST)
	public String findTagsByPage(@RequestParam Map<String, String> map);
	/**
	 * 添加标签
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/usertags/addtags",method=RequestMethod.POST)
	public String addTags(@RequestParam Map<String, String> map);
	/**
	 * 删除标签
	 * @param ids
	 * @return
	 */
	@RequestMapping(value="/usertags/deletetagbyid",method=RequestMethod.POST)
	public String deleteTagByID(@RequestParam("ids") String ids);
	/**
	 * 修改标签
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/usertags/updatetags",method=RequestMethod.POST)
	public String updataTag(@RequestParam Map<String, String> map);
	
	/**
	 * 同步标签
	 * @return
	 */
	@RequestMapping(value="/usertags/synchrotags",method=RequestMethod.POST)
	public String synchroTags();

}
