/**
 * Copyright (c) 2011-2014, lianghuan  梁欢(lianghuan1314@163.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package com.jfinal.weixin.sdk.msg;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.jfinal.kit.StrKit;
import com.jfinal.weixin.sdk.msg.in.InImageMsg;
import com.jfinal.weixin.sdk.msg.in.InLinkMsg;
import com.jfinal.weixin.sdk.msg.in.InLocationMsg;
import com.jfinal.weixin.sdk.msg.in.InMsg;
import com.jfinal.weixin.sdk.msg.in.InShortVideoMsg;
import com.jfinal.weixin.sdk.msg.in.InTextMsg;
import com.jfinal.weixin.sdk.msg.in.InVideoMsg;
import com.jfinal.weixin.sdk.msg.in.InVoiceMsg;
import com.jfinal.weixin.sdk.msg.in.event.InCustomEvent;
import com.jfinal.weixin.sdk.msg.in.event.InFollowEvent;
import com.jfinal.weixin.sdk.msg.in.event.InLocationEvent;
import com.jfinal.weixin.sdk.msg.in.event.InMassEvent;
import com.jfinal.weixin.sdk.msg.in.event.InMenuEvent;
import com.jfinal.weixin.sdk.msg.in.event.InQrCodeEvent;
import com.jfinal.weixin.sdk.msg.in.event.InTemplateMsgEvent;
import com.jfinal.weixin.sdk.msg.in.event.InTicketCardEvent;
import com.jfinal.weixin.sdk.msg.in.speech_recognition.InSpeechRecognitionResults;

public class InMsgParser {
	private static final Logger log = LogManager.getLogger(InMsgParser.class);
	
	private InMsgParser() {}
	
	/**
	 * 从 xml 中解析出各类消息与事件
	 */
	public static InMsg parse(String xml) {
		try {
			return doParse(xml);
		} catch (DocumentException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 消息类型
	 * 1：text 文本消息
	 * 2：image 图片消息
	 * 3：voice 语音消息
	 * 4：video 视频消息
	 *    shortvideo 小视频消息
	 * 5：location 地址位置消息
	 * 6：link 链接消息
	 * 7：event 事件
	 */
	private static InMsg doParse(String xml) throws DocumentException {
		Document doc = DocumentHelper.parseText(xml);
        Element root = doc.getRootElement();
        String toUserName = root.elementText("ToUserName");
        String fromUserName = root.elementText("FromUserName");
        Integer createTime = Integer.parseInt(root.elementText("CreateTime"));
        String msgType = root.elementText("MsgType");
        if ("text".equals(msgType))
        	return parseInTextMsg(root, toUserName, fromUserName, createTime, msgType);
        if ("image".equals(msgType))
        	return parseInImageMsg(root, toUserName, fromUserName, createTime, msgType);
        if ("voice".equals(msgType))
        	return parseInVoiceMsgAndInSpeechRecognitionResults(root, toUserName, fromUserName, createTime, msgType);
        if ("video".equals(msgType))
        	return parseInVideoMsg(root, toUserName, fromUserName, createTime, msgType);
		if ("shortvideo".equals(msgType))     //支持小视频
			return parseInShortVideoMsg(root, toUserName, fromUserName, createTime, msgType);
        if ("location".equals(msgType))
        	return parseInLocationMsg(root, toUserName, fromUserName, createTime, msgType);
        if ("link".equals(msgType))
        	return parseInLinkMsg(root, toUserName, fromUserName, createTime, msgType);
        if ("event".equals(msgType))
        	return parseInEvent(root, toUserName, fromUserName, createTime, msgType);
        throw new RuntimeException("无法识别的消息类型 " + msgType + "，请查阅微信公众平台开发文档");
	}
	
	private static InMsg parseInTextMsg(Element root, String toUserName, String fromUserName, Integer createTime, String msgType) {
		InTextMsg msg = new InTextMsg(toUserName, fromUserName, createTime, msgType);
		msg.setContent(root.elementText("Content"));
		msg.setMsgId(root.elementText("MsgId"));
		return msg;
	}
	
	private static InMsg parseInImageMsg(Element root, String toUserName, String fromUserName, Integer createTime, String msgType) {
		InImageMsg msg = new InImageMsg(toUserName, fromUserName, createTime, msgType);
		msg.setPicUrl(root.elementText("PicUrl"));
		msg.setMediaId(root.elementText("MediaId"));
		msg.setMsgId(root.elementText("MsgId"));
		return msg;
	}
	
	private static InMsg parseInVoiceMsgAndInSpeechRecognitionResults(Element root, String toUserName, String fromUserName, Integer createTime, String msgType) {
		String recognition = root.elementText("Recognition");
		if (StrKit.isBlank(recognition)) {
			InVoiceMsg msg = new InVoiceMsg(toUserName, fromUserName, createTime, msgType);
			msg.setMediaId(root.elementText("MediaId"));
			msg.setFormat(root.elementText("Format"));
			msg.setMsgId(root.elementText("MsgId"));
			return msg;
		}
		else {
			InSpeechRecognitionResults msg = new InSpeechRecognitionResults(toUserName, fromUserName, createTime, msgType);
			msg.setMediaId(root.elementText("MediaId"));
			msg.setFormat(root.elementText("Format"));
			msg.setMsgId(root.elementText("MsgId"));
			msg.setRecognition(recognition);			// 与 InVoiceMsg 唯一的不同之处
			return msg;
		}
	}
	
	private static InMsg parseInVideoMsg(Element root, String toUserName, String fromUserName, Integer createTime, String msgType) {
		InVideoMsg msg = new InVideoMsg(toUserName, fromUserName, createTime, msgType);
		msg.setMediaId(root.elementText("MediaId"));
		msg.setThumbMediaId(root.elementText("ThumbMediaId"));
		msg.setMsgId(root.elementText("MsgId"));
		return msg;
	}

	private static InMsg parseInShortVideoMsg(Element root, String toUserName, String fromUserName, Integer createTime, String msgType) {
		InShortVideoMsg msg = new InShortVideoMsg(toUserName, fromUserName, createTime, msgType);
		msg.setMediaId(root.elementText("MediaId"));
		msg.setThumbMediaId(root.elementText("ThumbMediaId"));
		msg.setMsgId(root.elementText("MsgId"));
		return msg;
	}

	private static InMsg parseInLocationMsg(Element root, String toUserName, String fromUserName, Integer createTime, String msgType) {
		InLocationMsg msg = new InLocationMsg(toUserName, fromUserName, createTime, msgType);
		msg.setLocation_X(root.elementText("Location_X"));
		msg.setLocation_Y(root.elementText("Location_Y"));
		msg.setScale(root.elementText("Scale"));
		msg.setLabel(root.elementText("Label"));
		msg.setMsgId(root.elementText("MsgId"));
		return msg;
	}
	
	private static InMsg parseInLinkMsg(Element root, String toUserName, String fromUserName, Integer createTime, String msgType) {
		InLinkMsg msg = new InLinkMsg(toUserName, fromUserName, createTime, msgType);
		msg.setTitle(root.elementText("Title"));
		msg.setDescription(root.elementText("Description"));
		msg.setUrl(root.elementText("Url"));
		msg.setMsgId(root.elementText("MsgId"));
		return msg;
	}
	
	// 解析各种事件
	private static InMsg parseInEvent(Element root, String toUserName, String fromUserName, Integer createTime, String msgType) {
		String event = root.elementText("Event");
		String eventKey = root.elementText("EventKey");
		log.info("event======="+event+"  eventKey="+eventKey);
		
		// 扫描带参数二维码事件之一		1: 用户未关注时，进行关注后的事件推送
		String ticket = root.elementText("Ticket");
		if ("subscribe".equals(event) && StrKit.notBlank(eventKey) && eventKey.startsWith("qrscene_")) {
			InQrCodeEvent e = new InQrCodeEvent(toUserName, fromUserName, createTime, msgType, event);
			e.setEventKey(eventKey);
			e.setTicket(ticket);
			return e;
		}
		
		// 关注/取消关注事件（包括二维码扫描关注，二维码扫描关注事件与扫描带参数二维码事件是两回事）
		if (("subscribe".equals(event) || "unsubscribe".equals(event))) {
			return new InFollowEvent(toUserName, fromUserName, createTime, msgType, event);
		}
				
		// 扫描带参数二维码事件之二		2: 用户已关注时的事件推送
		if ("SCAN".equals(event)) {
			InQrCodeEvent e = new InQrCodeEvent(toUserName, fromUserName, createTime, msgType, event);
			e.setEventKey(eventKey);
			e.setTicket(ticket);
			return e;
		}
		
		// 上报地理位置事件
		if ("LOCATION".equals(event)) {
			InLocationEvent e = new InLocationEvent(toUserName, fromUserName, createTime, msgType, event);
			e.setLatitude(root.elementText("Latitude"));
			e.setLongitude(root.elementText("Longitude"));
			e.setPrecision(root.elementText("Precision"));
			return e;
		}
		
		// 自定义菜单事件之一			1：点击菜单拉取消息时的事件推送
		if ("CLICK".equals(event)) {
			InMenuEvent e = new InMenuEvent(toUserName, fromUserName, createTime, msgType, event);
			e.setEventKey(eventKey);
			return e;
		}
		// 自定义菜单事件之二			2：点击菜单跳转链接时的事件推送
		if ("VIEW".equals(event)) {
			InMenuEvent e = new InMenuEvent(toUserName, fromUserName, createTime, msgType, event);
			e.setEventKey(eventKey);
			return e;
		}
		// 模板消息是否送达成功通知事件
		if ("TEMPLATESENDJOBFINISH".equals(event)) {
			InTemplateMsgEvent e = new InTemplateMsgEvent(toUserName, fromUserName, createTime, msgType, event);
			e.setMsgId(root.elementText("MsgID"));
			e.setStatus(root.elementText("Status"));
			return e;
		}
		// 群发任务结束时是否送达成功通知事件
		if ("MASSSENDJOBFINISH".equals(event)) {
			InMassEvent e = new InMassEvent(toUserName, fromUserName, createTime, msgType, event);
			e.setMsgId(root.elementText("MsgID"));
			e.setStatus(root.elementText("Status"));
			e.setTotalCount(root.elementText("TotalCount"));
			e.setFilterCount(root.elementText("FilterCount"));
			e.setSentCount(root.elementText("SentCount"));
			e.setErrorCount(root.elementText("ErrorCount"));
			return e;
		}
        // 多客服接入会话事件
        if ("kf_create_session".equals(event)) {
            InCustomEvent e = new InCustomEvent(toUserName, fromUserName, createTime, msgType, event);
            e.setKfAccount(root.elementText("KfAccount"));
            return e;
        }
        // 多客服关闭会话事件
        if ("kf_close_session".equals(event)) {
            InCustomEvent e = new InCustomEvent(toUserName, fromUserName, createTime, msgType, event);
            e.setKfAccount(root.elementText("KfAccount"));
            return e;
        }
        // 多客服转接会话事件
        if ("kf_switch_session".equals(event)) {
            InCustomEvent e = new InCustomEvent(toUserName, fromUserName, createTime, msgType, event);
            e.setKfAccount(root.elementText("KfAccount"));
            e.setToKfAccount(root.elementText("ToKfAccount"));
            return e;
        }
        
        // 卡券审核通知事件
      
        if("card_pass_check".equals(event) || "card_not_pass_check".equals(event)){
        	InTicketCardEvent e = new InTicketCardEvent(toUserName, fromUserName, createTime, msgType, event);
        	e.setCardId(root.elementText("CardId"));
        	return e;
        }
        // 卡券删除通知事件
        if("user_del_card".equals(event)){
        	InTicketCardEvent e = new InTicketCardEvent(toUserName, fromUserName, createTime, msgType, event);
        	e.setCardId(root.elementText("CardId"));
        	e.setUserCardCode(root.elementText("UserCardCode"));
        	return e;
        }
        // 卡券领取通知事件
        if("user_get_card".equals(event)){
        	InTicketCardEvent e = new InTicketCardEvent(toUserName, fromUserName, createTime, msgType, event);
        	e.setCardId(root.elementText("CardId"));
        	e.setIsGiveByFriend(root.elementText("IsGiveByFriend"));
        	e.setOuterId(root.elementText("OuterId"));
        	e.setUserCardCode(root.elementText("UserCardCode"));
        	e.setFriendUserName(root.elementText("FriendUserName"));
        	e.setOldUserCardCode(root.elementText("OldUserCardCode"));
        	return e;
        }
        
        // 卡券核销通知事件
        if ("user_consume_card".equals(event)) {
        	InTicketCardEvent e = new InTicketCardEvent(toUserName, fromUserName, createTime, msgType, event);
        	e.setCardId(root.elementText("CardId"));
        	e.setUserCardCode(root.elementText("UserCardCode"));
        	e.setConsumeSource(root.elementText("ConsumeSource"));
        	//e.setOutTradeNo(root.elementText("outTradeNo"));
        	//e.setTransId(root.elementText("transId"));
        	e.setLocationId(root.elementText("LocationName"));
        	e.setStaffOpenId(root.elementText("StaffOpenId"));
        	return e;
		}

		throw new RuntimeException("无法识别的事件类型" + event + "，请查阅微信公众平台开发文档");
	}
	
	@SuppressWarnings("unused")
	public static void main(String[] args) throws DocumentException {
		String xml = 
			"<xml>\n" +
				"<ToUserName><![CDATA[James]]></ToUserName>\n" +
				"<FromUserName><![CDATA[JFinal]]></FromUserName>\n" +
				"<CreateTime>1348831860</CreateTime>\n" +
				"<MsgType><![CDATA[text]]></MsgType>\n" +
					"<Content><![CDATA[this is a test]]></Content>\n" +
					"<MsgId>1234567890123456</MsgId>\n" +
			"</xml>";
		
//		InTextMsg msg = (InTextMsg)parse(xml);
//		System.out.println(msg.getToUserName());
//		System.out.println(msg.getFromUserName());
//		System.out.println(msg.getContent());
		
		
		String xml_2 = 
				"<xml>\n" +
					"<ToUserName><![CDATA[James]]></ToUserName>\n" +
					"<FromUserName><![CDATA[JFinal]]></FromUserName>\n" +
					"<CreateTime>1348831860</CreateTime>\n" +
					"<MsgType><![CDATA[text]]></MsgType>\n" +
						"<Content><![CDATA[this is a test]]></Content>\n" +
						"<MsgId>1234567890123456</MsgId>\n" +
				"</xml>";
		
		Document doc = DocumentHelper.parseText(xml_2);
        Element root = doc.getRootElement();
        String value = root.elementText("abc");
        System.out.println(value);
	}
}


