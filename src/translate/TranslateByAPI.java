package translate;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.alibaba.fastjson.JSONObject;

public class TranslateByAPI {
	private final static String APPID = "20170517000048032";
	private final static String key = "mZeTx4wQwxENeKzXbPo6";
	private final static String apiUrl = "http://api.fanyi.baidu.com/api/trans/vip/translate";
	protected static String translateProcess(String src, String from, String to) {
		
		if (src == null || src.length() == 0) {
			MainWindow.setErrorFlag(2);
			return "";
		} else if (src.length() > 1500) {
			MainWindow.setErrorFlag(1);
			MainWindow.setErrorMessage("翻译的文字太多了！");
			return "";
		}
		if (from == null) {
			MainWindow.setErrorFlag(1);
			MainWindow.setErrorMessage("输入的文字看不懂啊！");
			return "";
		}
		if (to == null) {
			MainWindow.setErrorFlag(1);
			MainWindow.setErrorMessage("我还不会你想要的语言！");
			return "";
		}
		
		int salt = new Random().nextInt(10000);
		// 签名
		String sign = Encrypt.MD5(APPID + src + salt + key);
		if (sign == "") {
			return "";
		}
		
		Map<String, String> params = new HashMap<String, String>();
		
		params.put("appid", APPID);
		params.put("salt", String.valueOf(salt));
		params.put("sign", sign);
		params.put("from", from);
		params.put("to", to);
		params.put("q", src);

		JSONObject translate = HttpRequestUtils.httpPost(apiUrl, params);
		
		String errorCode = translate.getString(APIJSON.ERROR);
		if (errorCode != null && errorCode.length() > 0) {
			MainWindow.setErrorFlag(2);
			return "";
		}
		
		JSONObject transResult = null;
		try {
			transResult = translate.getJSONArray(APIJSON.TRANS_RESULT).getJSONObject(0);
		} catch (Exception e) {
			MainWindow.setErrorFlag(2);
			return "";
		}
		
		if (transResult == null) {
			MainWindow.setErrorFlag(2);
			return "";
		}
		
		String result = transResult.getString(APIJSON.RESULT_DST);
		
		if (result == null) {
			MainWindow.setErrorFlag(2);
			return "";
		}
		
		return result;
		
	}
	
}
