package psl.archiver.daemon;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import psl.archiver.model.FileHistory;
import psl.archiver.model.FileArchive;
import psl.archiver.repository.FileLogRepository;
import psl.archiver.repository.FileRepository;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Date;
import java.util.List;

@Component
public class Archiver {

	private static final Logger logger = LoggerFactory.getLogger(Archiver.class);
	private static ObjectMapper MAPPER = new ObjectMapper();

	@Value("${app.environment}")
	private String environment;

	@Value("${app.dataFolder}")
	private String dataFolder;

	@Autowired
	private FileRepository fileRepo;

	@Autowired
	private FileLogRepository fileLogRepo;

	public void run() {
		Boolean end = false;
		while(!end) {
			try {
				archive();
			} catch (Throwable t) {
				t.printStackTrace();
			}
			try {
				Thread.sleep(15 * 1000);
			} catch (InterruptedException e) {}
		}
	}

	@Transactional
	private void archive() throws Throwable {
		File dir = new File(dataFolder);
		System.out.println("Getting all files in " + dir.getCanonicalPath() + " including those in subdirectories");
		List<File> files = (List<File>) FileUtils.listFiles(dir, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
		for (File file : files) {
			psl.archiver.model.File dbFile = fileRepo.findByName(file.getCanonicalPath());
			if (dbFile == null) {
				dbFile = new psl.archiver.model.File();
				dbFile.setName(file.getCanonicalPath());
				fileRepo.save(dbFile);
				FileHistory fileHistory = new FileHistory();
				fileHistory.setFile(dbFile);
				fileHistory.setStatus("detected");
				fileLogRepo.save(fileHistory);
				String content = IOUtils.toString(new FileInputStream(file), "UTF-8");
				FileArchive archive = MAPPER.readValue(content, FileArchive.class);
				archive.setArchived(new Date().toString());
				String updated = MAPPER.writeValueAsString(archive);
				BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
				writer.write(updated);
				writer.flush();
			}
			else {
				FileHistory fileHistory = new FileHistory();
				fileHistory.setFile(dbFile);
				fileHistory.setStatus("viewed");
				fileLogRepo.save(fileHistory);
			}
		}
	}
}
