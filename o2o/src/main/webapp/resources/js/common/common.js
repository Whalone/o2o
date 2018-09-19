/**
 * 
 */
function changeVerifyCode(img){
	img.src="../Kaptcha?"+Math.floor(Math.random()*100);
}

function getQueryString(name){
	//这个正则是寻找&+url参数名字=值+&
	//&可以不存在。
	var reg = new RegExp("(^|&)"+name+"=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if(r!=null){
		//decodeURIComponent() 函数可对 encodeURIComponent() 函数编码的 URI 进行解码。
		return decodeURIComponent(r[2]);
	}
	return '';
}