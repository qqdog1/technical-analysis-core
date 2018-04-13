package name.qd.analysis.dataSource.TWSE.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BSRBrokerParser {
	private final String dataDir = "./file/TWSE/bsr/";
	private final Path configPath = Paths.get("./config/TWSEBrokers.txt");
	private Set<String> set = new HashSet<>();
	
	public BSRBrokerParser() {
	}
	
	public void parse(String date) throws UnsupportedEncodingException, FileNotFoundException, IOException {
		set.clear();
		
		readConfig();
		
		parseData(date);
		
		writeConfig();
	}
	
	private void readConfig() throws UnsupportedEncodingException, FileNotFoundException, IOException {
		if(!Files.exists(configPath)) {
			Files.createFile(configPath);
		}
		
		List<String> lst = Files.readAllLines(configPath);
		for(String broker : lst) {
			set.add(broker);
		}
	}
	
	private void parseData(String date) throws IOException {
		Files.walk(Paths.get(dataDir + date)).forEach(path->{
			try {
				getBrokers(path);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}
	
	private void getBrokers(Path path) throws IOException {
		if(Files.isDirectory(path)) return;
		
		List<String> lst = Files.readAllLines(path, Charset.forName("Big5"));
		for(int i = 1 ; i < lst.size() ; i++) {
			if(i == 1) {
				if(!checkProduct(lst.get(1), path)) {
					System.out.println("Diff product No !!!, [" + path.toString() + "]");
					break;
				}
			} else {
				addBroker(lst.get(i));
			}
		}
	}
	
	private boolean checkProduct(String line, Path path) {
		String product = line.split("\"")[1];
		return path.toString().contains(product);
	}
	
	private void addBroker(String line) {
		String[] s = line.split(",");
		if(!"".equals(s[1])) {
			set.add(s[1]);
		}
		if(!"".equals(s[7])) {
			set.add(s[7]);
		}
	}
	
	private void writeConfig() throws IOException {
		Files.write(configPath, set);
	}
	
	public static void main(String[] args) {
		BSRBrokerParser parser = new BSRBrokerParser();
		try {
			parser.parse("20180411");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
