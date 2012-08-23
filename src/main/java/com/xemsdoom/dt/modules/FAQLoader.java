package com.xemsdoom.dt.modules;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class FAQLoader {

	public void copy() {

		File file = new File("plugins/DragonTravel/FAQ.txt");
		if (file.exists())
			return;

		InputStream in = getClass().getResourceAsStream("FAQ.txt");

		try {
			OutputStream out = new FileOutputStream(file);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) != -1) {
				out.write(buf, 0, len);
			}
			out.close();
			in.close();

			System.out.println("[DragonTravel] Created FAQ-file");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
