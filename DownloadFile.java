package netJava2020MsC_fn26393_project_final;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadFile {
	private static final String IMAGE_URL = "http://upbook.le.tsdoit.org/upbook2012_milen.petrov.png";
	
	public static void download(String nickname) {
		try {
			URL resourceURL = new URL(IMAGE_URL);
			String fileName = resourceURL.getFile();
			File directory = new File(nickname);
			if (!directory.exists()) {
				directory.mkdir();
			}
			String destName = "./" + nickname + fileName;

			try (InputStream is = resourceURL.openStream(); 
					OutputStream os = new FileOutputStream(destName)) {
				byte[] buffer = new byte[1024];
				int length;

				while ((length = is.read(buffer)) > 0) {
					os.write(buffer, 0, length);
				}
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		} catch (MalformedURLException e) {
			System.out.println(e.getMessage());
		}

	}
}
