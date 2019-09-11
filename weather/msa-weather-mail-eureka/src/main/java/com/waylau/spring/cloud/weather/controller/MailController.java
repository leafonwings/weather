package com.waylau.spring.cloud.weather.controller;

import com.waylau.spring.cloud.weather.service.DataClient;
import com.waylau.spring.cloud.weather.service.MailDataService;
import com.waylau.spring.cloud.weather.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Hello Controller.
 * 
 * @since 1.0.0 2017年11月20日
 * @author <a href="https://waylau.com">Way Lau</a> 
 */
@RestController
@RequestMapping("/mail")
public class MailController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MailDataService mailDataService;
	@Autowired
	private DataClient dataClient;

	@GetMapping
	public boolean mailNotice() throws Exception {
		String to = "xxxxxxx@163.com";
		String subject = "天气情况";
		String content = "天气内容";
		String[] receivers = MailCityReceiverConfig.receivers;
		if(receivers.length == 0)
			return false;
		else{
			for(String receiver : receivers){
				String cityId = receiver.split("!!")[0];
				to = receiver.split("!!")[1];
				try {
					WeatherResponse res = dataClient.getDataByCityId(cityId);
					Weather data = res.getData();
					subject = data.getCity()+"subject";
					content  =getWeatherContent(data);
					mailDataService.sendSimpleMail(to,subject,content);
				} catch (Exception e) {
					logger.error("发送邮件异常");
				}
			}
		}
		return true;
	}


	private String getWeatherContent(Weather data){
		String city = data.getCity();
		String ganmao = data.getGanmao();
		String wendu = data.getWendu();
		Yeaterday yesterday = data.getYesterday();
		List<Forecast> forecast = data.getForecast();
		String yes = yesterday.getDate()+","+yesterday.getHigh()+","+yesterday.getFx()+","+yesterday.getLow()+","+yesterday.getFl()+","+yesterday.getType();
		String forecastStr = "";
		for (Forecast fc :forecast)
		{
			forecastStr += fc.getDate()+","+fc.getHigh()+","+fc.getFengxiang()+","+fc.getLow()+","+fc.getFengli()+","+fc.getType();
		}
		String content = "城市:"+city+"\r\n"
						+"温度"+wendu+"\r\n"
						+"温馨提示："+ganmao+"\r\n"
						+"最近天气情况：\r\n"
						+yes+"\r\n"
						+forecastStr;
		return content;
	}
}
