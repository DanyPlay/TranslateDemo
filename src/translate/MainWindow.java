package translate;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import com.alibaba.fastjson.JSONObject;

public class MainWindow {

	private Shell translateShell;
	private Text inputText;
	private Text NLPText;
	private Text answerText;
	private Label answerLabel;
	private Label inputLabel;
	private Label NLPLabel;
	private static int errorFlag = 0;
	private static String errorMessage;
	private static String lastSrcLanguage;
	private static String lastDstLanguage;

	protected static void setErrorFlag(int errorFlag) {
		MainWindow.errorFlag = errorFlag;
	}

	protected static void setErrorMessage(String errorMessage) {
		MainWindow.errorMessage = errorMessage;
	}
	
	protected static void resetError() {
		errorFlag = 0;
		errorMessage = null;
	}
	
	protected static String getLastSrcLanguage() {
		return lastSrcLanguage;
	}

	protected static void setLastSrcLanguage(String lastSrcLanguage) {
		MainWindow.lastSrcLanguage = lastSrcLanguage;
	}

	protected static String getLastDstLanguage() {
		return lastDstLanguage;
	}

	protected static void setLastDstLanguage(String lastDstLanguage) {
		MainWindow.lastDstLanguage = lastDstLanguage;
	}
	
	protected static void resetLastLanguage() {
		lastDstLanguage = null;
		lastSrcLanguage = null;
	}

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MainWindow window = new MainWindow();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	}

	/**
	 * Open the window.
	 */
	protected void open() {
		Display display = Display.getDefault();
		createContents();
		translateShell.open();
		translateShell.layout();
		while (!translateShell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		translateShell = new Shell();
		translateShell.setSize(422, 553);
		translateShell.setText("对话系统：翻译");
		
		inputLabel = new Label(translateShell, SWT.NONE);
		inputLabel.setLocation(10, 10);
		inputLabel.setSize(55, 17);
		inputLabel.setText("输入对话");
		
		inputText = new Text(translateShell, SWT.BORDER);
		inputText.setLocation(10, 29);
		inputText.setSize(389, 56);
		
		answerLabel = new Label(translateShell, SWT.NONE);
		answerLabel.setText("回答");
		answerLabel.setBounds(10, 117, 55, 17);
		
		answerText = new Text(translateShell, SWT.BORDER | SWT.WRAP);
		answerText.setBounds(10, 137, 389, 56);

		NLPLabel = new Label(translateShell, SWT.NONE);
		NLPLabel.setText("语义解析");
		NLPLabel.setBounds(10, 209, 55, 17);
		
		NLPText = new Text(translateShell, SWT.BORDER | SWT.WRAP | SWT.MULTI | SWT.V_SCROLL | SWT.READ_ONLY);
		NLPText.setBounds(10, 232, 389, 273);

		Button btnNewButton = new Button(translateShell, SWT.NONE);
		translateShell.setDefaultButton(btnNewButton);
		btnNewButton.setLocation(319, 91);
		btnNewButton.setSize(80, 27);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				NLPText.setText("");
				String src = inputText.getText();
				if (src == null || src.length() == 0) {
					answerText.setText("你还没有输入内容！");
					return;
				}
				
				// 把string用接口拿到语义
				JSONObject nlp = GetModifier.GetNLI(src);
				NLPText.setText(Format.formatJson(nlp.toString()));

				// 处理语义
				String answer = ModifierProcess.NLPProcess(nlp);
				answerText.setText(answer);
				
				if (errorFlag == 1) {
					answerText.setText(errorMessage);
				} else if (errorFlag == 2) {
					answerText.setText("遇到了错误，但这不是我的锅！");
				}
				
				resetError();

			}
		});
		btnNewButton.setText("发送");
	}
}
