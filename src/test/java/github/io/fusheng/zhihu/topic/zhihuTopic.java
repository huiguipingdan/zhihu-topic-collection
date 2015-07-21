package github.io.fusheng.zhihu.topic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class zhihuTopic {
	private static final String TOPIC_URL = "http://www.zhihu.com/topic/19585313/top-answers?page=";
	private static final String QA_URL_PREFIX = "http://www.zhihu.com";
	private static final String SAVE_PATH = "C:\\Users\\Ben\\Desktop\\SanGuoTopic.txt";
	
	private static final String TITLTE_CLASS = ".zm-item-title.zm-editable-content";
	private static final String ANSWER_CLASS = ".zm-editable-content.clearfix:not(.fixed-summary)";
	public static void main(String[] args) throws Exception {
		zhihuTopic zhihu = new zhihuTopic();
		zhihu.saveAsTxt();
	}
	public void saveAsTxt() throws Exception {
		int totalNum = getNumPage();
		File f = new File(SAVE_PATH);
		f.delete();
		f.createNewFile();
		BufferedWriter bfw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f),"utf-8"));
		for(int i=1; i<=totalNum; i++) {
			bfw.write("========pageNum"+i+"====start======");
			bfw.newLine();
			for(Element a : getTopicList(i)) {
				bfw.write(getQAList(a.attr("href"),TITLTE_CLASS,ANSWER_CLASS));
				bfw.newLine();
			}
			bfw.write("========pageNum"+i+"====end======");
			bfw.newLine();
		}
		bfw.flush();
		bfw.close();
		System.out.println("ok...........");
	}

	public Elements getTopicList(int pageNum) throws IOException {
		Document doc = Jsoup.connect(TOPIC_URL+pageNum).get();
		Elements newsHeadlines = doc.select(".toggle-expand");
		return newsHeadlines;
	}

	public int getNumPage() throws IOException {
		Document doc = Jsoup.connect(TOPIC_URL+"1").get();
		Elements newsHeadlines = doc.select(".zm-invite-pager").select("a").eq(2);
		return Integer.parseInt(newsHeadlines.text());
	}

	public String getQAList(String suffix, String filterTitleClass,String filterAnswerClass) throws IOException {
		Document doc = Jsoup.connect(QA_URL_PREFIX+suffix).get();
		String a = doc.select(filterTitleClass).text();
		String b = doc.select(filterAnswerClass).text();
		return a+"------>"+b;
	}
}