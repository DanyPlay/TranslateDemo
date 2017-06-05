package translate;

import java.util.HashMap;
import java.util.Map;

public class ApiLanguage {
	protected static final String AUTO = "自动识别";
	protected static final String ENGLISH = "英语";
	protected static final String CHINESE = "中文";
	protected static Map<String, String> language = new HashMap<String, String>();
	static{
		language.put("自动识别", "auto");
		language.put("中文", "zh");
		language.put("英语", "en");
		language.put("粤语", "yue");
		language.put("文言文", "wyw");
		language.put("日语", "jp");
		language.put("韩语", "kor");
		language.put("法语", "fra");
		language.put("西班牙语", "spa");
		language.put("泰语", "th");
		language.put("阿拉伯语", "ara");
		language.put("俄语", "ru");
		language.put("葡萄牙语", "pt");
		language.put("德语", "de");
		language.put("意大利语", "it");
		language.put("希腊语", "el");
		language.put("荷兰语", "nl");
		language.put("波兰语", "pl");
		language.put("保加利亚语", "bul");
		language.put("爱沙尼亚语", "est");
		language.put("丹麦语", "dan");
		language.put("芬兰语", "fin");
		language.put("捷克语", "cs");
		language.put("罗马尼亚语", "rom");
		language.put("斯洛文尼亚语", "slo");
		language.put("瑞典语", "swe");
		language.put("匈牙利语", "hu");
		language.put("繁体中文", "cht");
		language.put("越南语", "vie");
	}
}
