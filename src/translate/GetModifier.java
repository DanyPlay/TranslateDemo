package translate;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public class GetModifier {
	private final static String appKey= "91ead2ee1dfe496cbb333fa81150f1e5";
	private final static String appSecret= "a99c16ecd8ca472c84f63b363a79eabb";
	private final static String api= "nli";
	private final static String apiUrl = "https://cn.olami.ai/cloudservice/api";
	private final static String cusid = "translatedemo";
	
	protected static JSONObject GetNLI(String src) {
		JSONObject data = new JSONObject();
		data.put("input_type", 1);
		data.put("text", src);
		
		JSONObject send = new JSONObject();
		send.put("data_type", "stt");
		send.put("data", data);
		
		long timestamp = new Timestamp(System.currentTimeMillis()).getTime();
		
		// your_app_secret + api=api_parameter + appkey=your_app_key + timestamp=current_timestamp + your_app_secret
		String sign = appSecret + "api=" + api + "appkey=" + appKey + "timestamp=" + timestamp + appSecret;
		sign = Encrypt.MD5(sign);
		
		// appkey=your_app_key&api=nlu&timestamp=current_timestamp&sign=your_sign&rq=
		// String post_data = "appkey=" + appKey + "&api=" + api + "&timestamp=" + timestamp + "&sign=" + sign + "&rq=" + send.toString();
		Map<String, String> post_data = new HashMap<String, String>();
		post_data.put("appkey", appKey);
		post_data.put("api", api);
		post_data.put("timestamp", String.valueOf(timestamp));
		post_data.put("sign", sign);
		post_data.put("rq", send.toString());
		post_data.put("cusid", cusid);

		return HttpRequestUtils.httpPost(apiUrl, post_data);
	}
	
}
