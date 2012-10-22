package video;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3Object;

public class S3 {

	String key;
	String secret;
	private String bucket;

	public S3() {
		key = getConf("AWS_KEY", "AKIAJDJV7E7ILRKUWPQQ");
		secret = getConf("AWS_SECRET",
				"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");
		bucket = getConf("LVR_BUCKET", "66and33-dev");
	}

	public String getConf(String name, String def) {
		String result = System.getenv(name);
		if (result == null)
			result = def;
		return result;
	}

	public void delete(String key) {
		AWSCredentials awsCredentials = credentials();
		AmazonS3Client client = new AmazonS3Client(awsCredentials);
		client.deleteObject(bucket, key);
	}

	private AWSCredentials credentials() {
		AWSCredentials awsCredentials = new BasicAWSCredentials(key, secret);
		return awsCredentials;
	}

	public InputStream open(String key) {
		AWSCredentials awsCredentials = credentials();
		AmazonS3Client client = new AmazonS3Client(awsCredentials);
		S3Object object = client.getObject(bucket, key);
		return object.getObjectContent();
	}

	public File download(String key) throws IOException {
		File file = File.createTempFile("s3-", ".tmp");
		copy(open(key), new FileOutputStream(file));
		return file;
	}

	private void copy(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		while (true) {
			int len = in.read(buffer);
			out.write(buffer, 0, len);
			if (len < buffer.length)
				break;
		}
	}

	public void upload(String key, File file) {
		AWSCredentials awsCredentials = credentials();
		AmazonS3Client client = new AmazonS3Client(awsCredentials);
		client.putObject(bucket, key, file);
	}
}
