package com.fangxiaofeng.o2o.web.shopadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value="shopadmin",method={RequestMethod.GET})
public class ShopAdminController {
	@RequestMapping(value="/shopoperation")
	public String shopOperation(){
		return "shop/shopoperation";
		//这里的路径在xml已经配置了前缀（WEB_INF/html/）和后缀（.html）
		//正常的路径是WEB-INF/html/shopoperation.html
	}
	@RequestMapping(value="/shoplist")
	public String shopList(){
		return "shop/shoplist";
	}
	@RequestMapping(value="/shopmanagement")
	public String shopManagement(){
		return "shop/shopmanagement";
	}
}
