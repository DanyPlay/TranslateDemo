package translate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecFactory;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BrowserCompatSpec;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;

@SuppressWarnings("deprecation")
public class HttpRequestUtils {

    /**
     * post请求
     * @param url         url地址
     * @param jsonParam     参数
     * @return JSONObject
     */
	protected static JSONObject httpPost(String url,Map<String, String> text){
        // post请求返回结果
        @SuppressWarnings("resource")
		DefaultHttpClient httpClient = new DefaultHttpClient();
        JSONObject jsonResult = null;
        HttpPost method = new HttpPost(url);
        
        // cookie rejected 
        CookieSpecFactory csf = new CookieSpecFactory() {
        	public CookieSpec newInstance(HttpParams params) {
        		return new BrowserCompatSpec() {   
        			@Override
        			public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {}
   	         	};
   	     	}
        };
        httpClient.getCookieSpecs().register("easy", csf);
        httpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY, "easy");
        
        List<NameValuePair> formparams = new ArrayList<NameValuePair>(); 
        
        for (String key : text.keySet()) {
			formparams.add(new BasicNameValuePair(key, text.get(key)));
		} 

        try {
        	UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");  
            method.setEntity(uefEntity); 

            HttpResponse result = httpClient.execute(method);
            
            if (result.getStatusLine().getStatusCode() == 200) {
                String str = "";
                try {
                    str = EntityUtils.toString(result.getEntity());
                    jsonResult = JSONObject.parseObject(str);
                } catch (Exception e) {
                	MainWindow.setErrorFlag(2);
                	return null;
                }
            }
        } catch (IOException e) {
        	MainWindow.setErrorFlag(2);
        	return null;
        }
        return jsonResult;
    }
}
