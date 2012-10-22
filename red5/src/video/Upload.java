package video;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import stream.Publisher;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;

public class Upload extends HttpServlet {

	private static final long serialVersionUID = 1282675977276894763L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (!ServletFileUpload.isMultipartContent(req)) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		try {
			HashMap<String, FileItem> request = parseRequest(req);
			process(request);
		} catch (Exception e) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					e.getMessage());
			return;
		}
	}

	private HashMap<String, FileItem> parseRequest(HttpServletRequest req)
			throws FileUploadException {
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		@SuppressWarnings("unchecked")
		List<FileItem> items = upload.parseRequest(req);
		HashMap<String, FileItem> result = new HashMap<String, FileItem>();
		for (FileItem item : items) {
			result.put(item.getFieldName(), item);
		}
		return result;
	}

	private void process(HashMap<String, FileItem> request) throws Exception {
		FileItem fileItem = request.get("file");
		File file = File.createTempFile("upload", fileItem.getName());
		fileItem.write(file);
		String client = request.get("client").getString();
		String title = request.get("title").getString();
		String page = request.get("page").getString();
		String category = request.get("category").getString();
		String description = request.get("description").getString();
		String key = request.get("key").getString();
		Handler.handle(file, true, client, title, page, category, description,
				key);
	}

}
