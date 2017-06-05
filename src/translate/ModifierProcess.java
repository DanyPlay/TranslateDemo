package translate;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class ModifierProcess {
	protected static final int outputLanguageNumber = 5;
	
	protected static String NLPProcess(JSONObject nlpJson) {
		String status = nlpJson.getString(NLPJSON.JSON_STATUS);
		if (status == null || !NLPJSON.STATUS_OK.equalsIgnoreCase(status)) {
			MainWindow.setErrorFlag(2);
			return "";
		}
		
		// Get info from JSON
		String result = "";
		String modifier = null;
		String src_language = null;
		String dst_language = null;
		String src_code = null;
		String dst_code = null;
		String content = null;
		String resultLanguage = null;
		
		try {
			JSONObject nli = nlpJson.getJSONObject(NLPJSON.JSON_DATA).getJSONArray(NLPJSON.DATA_NLI).getJSONObject(0);
			JSONObject descobj = nli.getJSONObject(NLPJSON.NLI_DESCOBJ);
			if (!"0".equals(descobj.getString(NLPJSON.DESCOBJ_STATUS))) {
				MainWindow.setErrorFlag(1);
				MainWindow.setErrorMessage(descobj.getString(NLPJSON.DESCOBJ_RESULT));
				return "";
			}
			JSONObject semantic = nli.getJSONArray(NLPJSON.NLI_SEMANTIC).getJSONObject(0);
			modifier = semantic.getJSONArray(NLPJSON.SEMANTIC_MODIFIER).getString(0);
			
			JSONArray slotsArray = semantic.getJSONArray(NLPJSON.SEMANTIC_SLOTS);
			if (slotsArray != null && slotsArray.size() > 0) {
				Map<String, String> slotsMap = new HashMap<String, String>();
				for (int i = 0; i < slotsArray.size(); i++) {
					JSONObject slots = slotsArray.getJSONObject(i);
					String name = slots.getString(NLPJSON.SLOTS_NAME);
					String value = slots.getString(NLPJSON.SLOTS_VALUE);
					slotsMap.put(name, value);
				}
				
				src_language = slotsMap.get(NLPJSON.S_SRCLANGUAGE);
				dst_language = slotsMap.get(NLPJSON.S_DSTLANGUAGE);
				content = slotsMap.get(NLPJSON.S_CONTENT);
			}
			
		} catch (Exception e) {
			MainWindow.setErrorFlag(2);
			return "";
		}
		
		// Process the info
		if (modifier == null || modifier.length() < 1) {
			MainWindow.setErrorFlag(2);
			return "";
		}
		if (modifier.equalsIgnoreCase(NLPJSON.M_CAN)) {
			if (dst_language != null && dst_language.length() > 0) {
				String language = ApiLanguage.language.get(dst_language);
				if (language != null && language.length() > 0) {
					MainWindow.resetLastLanguage();
					MainWindow.setLastDstLanguage(dst_language);
					result = "没问题！";
					return result;
				}
				result = "我还不懂" + dst_language + "，但我会慢慢学习的！";
				return result;
			}
			result = "我可以翻译，问我吧，但最好不要太难哟！";
			return result;
		} else if (modifier.equalsIgnoreCase(NLPJSON.M_CANDOWHICH)) {
			// map.keyset get 5 languages
			Set<String> set = ApiLanguage.language.keySet();
			set.remove(ApiLanguage.AUTO);
			String[] language = set.toArray(new String[set.size()]);
			int start = 0;
			for (int i = 0; i < outputLanguageNumber; i++) {
				Random random = new Random();
				start += random.nextInt(language.length - outputLanguageNumber + i - start) + 1;
				result += language[start] + "、";
			}
			MainWindow.resetLastLanguage();
			result = "我会翻译" + result.substring(0, result.length() - 1) + "...还有好多种语言！";
			return result;
		} else if (modifier.equalsIgnoreCase(NLPJSON.M_TRANSLATE)) {
			// If content exist, get languages, translate it by translateApi
			if (content != null && content.length() > 0) {
				if (src_language == null || src_language.length() == 0) {
					String lastSrcLanguage = MainWindow.getLastSrcLanguage();
					if (lastSrcLanguage == null || lastSrcLanguage.length() == 0) {
						src_code = ApiLanguage.language.get(ApiLanguage.AUTO);
					} else {
						src_code = ApiLanguage.language.get(lastSrcLanguage);
					}
				} else {
					src_code = ApiLanguage.language.get(src_language);
					if (src_code == null || src_code.length() == 0) {
						result = "我还没有学会" + src_language + "，等我学会之后你再问我吧！";
						return result;
					}
				}
				if (dst_language == null || dst_language.length() == 0) {
					String lastDstLanguage = MainWindow.getLastDstLanguage();
					if (lastDstLanguage == null || lastDstLanguage.length() == 0) {
						dst_code = ApiLanguage.language.get(ApiLanguage.ENGLISH);
						resultLanguage = ApiLanguage.ENGLISH;
					} else {
						dst_code = ApiLanguage.language.get(lastDstLanguage);
						resultLanguage = lastDstLanguage;
					}
				} else {
					dst_code = ApiLanguage.language.get(dst_language);
					resultLanguage = dst_language;
					if (dst_code == null || dst_code.length() == 0) {
						result = "我还没有学会" + dst_language + "，等我学会之后你再问我吧！";
						return result;
					}
				}

				MainWindow.setLastSrcLanguage(src_language);
				MainWindow.setLastDstLanguage(dst_language);
				
				String transResult = TranslateByAPI.translateProcess(content, src_code, dst_code);
				if (transResult.equals(content)) {
					dst_code = ApiLanguage.language.get(ApiLanguage.CHINESE);
					resultLanguage = ApiLanguage.CHINESE;
					transResult = TranslateByAPI.translateProcess(content, src_code, dst_code);
				}
				if ("".equals(transResult)) {
					return "";
				}
				
				result = "【" + content + "】翻译成" + resultLanguage + "的结果是【" + transResult + "】";
				return result;
			} else {
				if (src_language != null && src_language.length() > 0 && dst_language != null && dst_language.length() > 0) {
					MainWindow.setLastSrcLanguage(src_language);
					MainWindow.setLastDstLanguage(dst_language);
				}
				result = "你说，我来翻译！";
				return result;
			}

		}
		
		MainWindow.setErrorFlag(2);
		return result;
	}

}
