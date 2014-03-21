package com.vccloud.portal.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

/**
 * ͼ��ü��Լ�ѹ����������
 * 
 * ��Ҫ��Զ�̬��GIF��ʽͼƬ�ü�֮��ֻ����һ֡��̬Ч���������ṩ�������
 * 
 * �ṩ����������������������GIF��ʽ��������һһ���������б����������� �ṩ����JDK Image I/O �Ľ������(JDK̽��ʧ��)
 * 
 * @link http://hi.baidu.com/xckouy/item/872a7db4c0e0b5ed4fc7fd85
 * @author Andy
 * @see GifDecoder.class
 * @see AnimatedGifEncoder.class
 * @see BufferedImage.class
 * @see ImageIO.class
 * @see ImageReader.class
 * @since 1.0 2011.12.21
 */
public class ImageCutterUtil {

	public enum IMAGE_FORMAT {
		BMP("bmp"), JPG("jpg"), WBMP("wbmp"), JPEG("jpeg"), PNG("png");

		private String value;

		IMAGE_FORMAT(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}

	/**
	 * ��ȡͼƬ��ʽ
	 * 
	 * @param file
	 *            ͼƬ�ļ�
	 * @return ͼƬ��ʽ
	 */
	public static String getImageFormatName(File file) throws IOException {
		String formatName = null;

		ImageInputStream iis = ImageIO.createImageInputStream(file);
		Iterator<ImageReader> imageReader = ImageIO.getImageReaders(iis);
		if (imageReader.hasNext()) {
			ImageReader reader = imageReader.next();
			formatName = reader.getFormatName();
		}

		return formatName;
	}

	/************************* ����������������� *****************************/
	/**
	 * ����ͼƬ
	 * 
	 * @param source
	 *            ������ͼƬ·��
	 * @param targetPath
	 *            �ü��󱣴�·����Ĭ��ΪԴ·����
	 * @param x
	 *            ��ʼ������
	 * @param y
	 *            ��ʼ������
	 * @param width
	 *            ���п��
	 * @param height
	 *            ���и߶�
	 * 
	 * @returns �ü��󱣴�·����ͼƬ��׺����ͼƬ�����������ɣ�
	 * @throws IOException
	 */
	public static String cutImage(String sourcePath, String targetPath, int x,
			int y, int width, int height) throws IOException {
		File file = new File(sourcePath);
		if (!file.exists()) {
			throw new IOException("not found the image��" + sourcePath);
		}
		if (null == targetPath || targetPath.isEmpty())
			targetPath = sourcePath;

		String formatName = getImageFormatName(file);
		if (null == formatName)
			return targetPath;
		formatName = formatName.toLowerCase();

		// ��ֹͼƬ��׺��ͼƬ�������Ͳ�һ�µ����
		String pathPrefix = getPathWithoutSuffix(targetPath);
		targetPath = pathPrefix + formatName;

		BufferedImage image = ImageIO.read(file);
		image = image.getSubimage(x, y, width, height);
		ImageIO.write(image, formatName, new File(targetPath));

		return targetPath;
	}

	/**
	 * ѹ��ͼƬ
	 * 
	 * @param sourcePath
	 *            ��ѹ����ͼƬ·��
	 * @param targetPath
	 *            ѹ����ͼƬ·����Ĭ��Ϊ��ʼ·����
	 * @param width
	 *            ѹ�����
	 * @param height
	 *            ѹ���߶�
	 * 
	 * @returns �ü��󱣴�·����ͼƬ��׺����ͼƬ�����������ɣ�
	 * @throws IOException
	 */
	public static String zoom(String sourcePath, String targetPath, int width,
			int height) throws IOException {
		File file = new File(sourcePath);
		if (!file.exists()) {
			throw new IOException("not found the image ��" + sourcePath);
		}
		if (null == targetPath || targetPath.isEmpty())
			targetPath = sourcePath;

		String formatName = getImageFormatName(file);
		if (null == formatName)
			return targetPath;
		formatName = formatName.toLowerCase();

		// ��ֹͼƬ��׺��ͼƬ�������Ͳ�һ�µ����
		String pathPrefix = getPathWithoutSuffix(targetPath);
		targetPath = pathPrefix + formatName;

		BufferedImage image = ImageIO.read(file);
		BufferedImage zoomImage = zoom(image, width, height);
		ImageIO.write(zoomImage, formatName, new File(targetPath));

		return targetPath;
	}

	/*********************** ����JDK ������� ********************************/

	/**
	 * ��ȡͼƬ
	 * 
	 * @param file
	 *            ͼƬ�ļ�
	 * @return ͼƬ����
	 * @throws IOException
	 */
	public static BufferedImage[] readerImage(File file) throws IOException {
		BufferedImage sourceImage = ImageIO.read(file);
		BufferedImage[] images = null;
		ImageInputStream iis = ImageIO.createImageInputStream(file);
		Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(iis);
		if (imageReaders.hasNext()) {
			ImageReader reader = imageReaders.next();
			reader.setInput(iis);
			int imageNumber = reader.getNumImages(true);
			images = new BufferedImage[imageNumber];
			for (int i = 0; i < imageNumber; i++) {
				BufferedImage image = reader.read(i);
				if (sourceImage.getWidth() > image.getWidth()
						|| sourceImage.getHeight() > image.getHeight()) {
					image = zoom(image, sourceImage.getWidth(), sourceImage
							.getHeight());
				}
				images[i] = image;
			}
			reader.dispose();
			iis.close();
		}
		return images;
	}

	/**
	 * ����Ҫ����ͼƬ
	 * 
	 * @param images
	 *            ͼƬ����
	 * @param x
	 *            ������ʼλ��
	 * @param y
	 *            ������ʼλ��
	 * @param width
	 *            ���
	 * @param height
	 *            ���
	 * @return ������ͼƬ����
	 * @throws Exception
	 */
	public static BufferedImage[] processImage(BufferedImage[] images, int x,
			int y, int width, int height) throws Exception {
		if (null == images) {
			return images;
		}
		BufferedImage[] oldImages = images;
		images = new BufferedImage[images.length];
		for (int i = 0; i < oldImages.length; i++) {
			BufferedImage image = oldImages[i];
			images[i] = image.getSubimage(x, y, width, height);
		}
		return images;
	}

	/**
	 * д�봦����ͼƬ��file
	 * 
	 * ͼƬ��׺����ͼƬ��ʽ����
	 * 
	 * @param images
	 *            ������ͼƬ����
	 * @param formatName
	 *            ͼƬ��ʽ
	 * @param file
	 *            д���ļ�����
	 * @throws Exception
	 */
	public static void writerImage(BufferedImage[] images, String formatName,
			File file) throws Exception {
		Iterator<ImageWriter> imageWriters = ImageIO
				.getImageWritersByFormatName(formatName);
		if (imageWriters.hasNext()) {
			ImageWriter writer = imageWriters.next();
			String fileName = file.getName();
			int index = fileName.lastIndexOf(".");
			if (index > 0) {
				fileName = fileName.substring(0, index + 1) + formatName;
			}
			String pathPrefix = getFilePrefixPath(file.getPath());
			File outFile = new File(pathPrefix + fileName);
			ImageOutputStream ios = ImageIO.createImageOutputStream(outFile);
			writer.setOutput(ios);

			if (writer.canWriteSequence()) {
				writer.prepareWriteSequence(null);
				for (int i = 0; i < images.length; i++) {
					BufferedImage childImage = images[i];
					IIOImage image = new IIOImage(childImage, null, null);
					writer.writeToSequence(image, null);
				}
				writer.endWriteSequence();
			} else {
				for (int i = 0; i < images.length; i++) {
					writer.write(images[i]);
				}
			}

			writer.dispose();
			ios.close();
		}
	}

	/**
	 * ���и�ʽͼƬ
	 * 
	 * ����JDK Image I/O�������
	 * 
	 * @param sourceFile
	 *            ������ͼƬ�ļ�����
	 * @param destFile
	 *            �ü��󱣴��ļ�����
	 * @param x
	 *            ���к�����ʼλ��
	 * @param y
	 *            ����������ʼλ��
	 * @param width
	 *            ���п��
	 * @param height
	 *            ���п��
	 * @throws Exception
	 */
	public static void cutImage(File sourceFile, File destFile, int x, int y,
			int width, int height) throws Exception {
		// ��ȡͼƬ��Ϣ
		BufferedImage[] images = readerImage(sourceFile);
		// ����ͼƬ
		images = processImage(images, x, y, width, height);
		// ��ȡ�ļ���׺
		String formatName = getImageFormatName(sourceFile);
		destFile = new File(getPathWithoutSuffix(destFile.getPath())
				+ formatName);

		// д�봦����ͼƬ���ļ�
		writerImage(images, formatName, destFile);
	}

	/**
	 * ��ȡϵͳ֧�ֵ�ͼƬ��ʽ
	 */
	public static void getOSSupportsStandardImageFormat() {
		String[] readerFormatName = ImageIO.getReaderFormatNames();
		String[] readerSuffixName = ImageIO.getReaderFileSuffixes();
		String[] readerMIMEType = ImageIO.getReaderMIMETypes();
		System.out
				.println("========================= OS supports reader ========================");
		System.out.println("OS supports reader format name :  "
				+ Arrays.asList(readerFormatName));
		System.out.println("OS supports reader suffix name :  "
				+ Arrays.asList(readerSuffixName));
		System.out.println("OS supports reader MIME type :  "
				+ Arrays.asList(readerMIMEType));

		String[] writerFormatName = ImageIO.getWriterFormatNames();
		String[] writerSuffixName = ImageIO.getWriterFileSuffixes();
		String[] writerMIMEType = ImageIO.getWriterMIMETypes();

		System.out
				.println("========================= OS supports writer ========================");
		System.out.println("OS supports writer format name :  "
				+ Arrays.asList(writerFormatName));
		System.out.println("OS supports writer suffix name :  "
				+ Arrays.asList(writerSuffixName));
		System.out.println("OS supports writer MIME type :  "
				+ Arrays.asList(writerMIMEType));
	}

	/**
	 * ѹ��ͼƬ
	 * 
	 * @param sourceImage
	 *            ��ѹ��ͼƬ
	 * @param width
	 *            ѹ��ͼƬ�߶�
	 * @param heigt
	 *            ѹ��ͼƬ���
	 */
	private static BufferedImage zoom(BufferedImage sourceImage, int width,
			int height) {
		// FIX: Unknown image type 0
		// @link
		// http://stackoverflow.com/questions/5836128/how-do-i-make-javas-imagebuffer-to-read-a-png-file-correctly
		int imageType = sourceImage.getType();
		if (imageType == 0) {
			imageType = 5;
		}
		BufferedImage zoomImage = new BufferedImage(width, height, imageType);
		Image image = sourceImage.getScaledInstance(width, height,
				Image.SCALE_SMOOTH);
		Graphics gc = zoomImage.getGraphics();
		gc.setColor(Color.WHITE);
		gc.drawImage(image, 0, 0, null);
		return zoomImage;
	}

	/**
	 * ��ȡĳ���ļ���ǰ׺·��
	 * 
	 * �������ļ�����·��
	 * 
	 * @param file
	 *            ��ǰ�ļ�����
	 * @return
	 * @throws IOException
	 */
	public static String getFilePrefixPath(File file) throws IOException {
		String path = null;
		if (!file.exists()) {
			throw new IOException("not found the file !");
		}
		String fileName = file.getName();
		path = file.getPath().replace(fileName, "");
		return path;
	}

	/**
	 * ��ȡĳ���ļ���ǰ׺·��
	 * 
	 * �������ļ�����·��
	 * 
	 * @param path
	 *            ��ǰ�ļ�·��
	 * @return �������ļ�����·��
	 * @throws Exception
	 */
	public static String getFilePrefixPath(String path) throws Exception {
		if (null == path || path.isEmpty())
			throw new Exception("�ļ�·��Ϊ�գ�");
		int index = path.lastIndexOf(File.separator);
		if (index > 0) {
			path = path.substring(0, index + 1);
		}
		return path;
	}

	/**
	 * ��ȡ��������׺���ļ�·��
	 * 
	 * @param src
	 * @return
	 */
	public static String getPathWithoutSuffix(String src) {
		String path = src;
		int index = path.lastIndexOf(".");
		if (index > 0) {
			path = path.substring(0, index + 1);
		}
		return path;
	}

	/**
	 * ��ȡ�ļ���
	 * 
	 * @param filePath
	 *            �ļ�·��
	 * @return �ļ���
	 * @throws IOException
	 */
	public static String getFileName(String filePath) throws IOException {
		File file = new File(filePath);
		if (!file.exists()) {
			throw new IOException("not found the file !");
		}
		return file.getName();
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// ��ȡϵͳ֧�ֵ�ͼƬ��ʽ
		// ImageCutterUtil.getOSSupportsStandardImageFormat();

		try {
			// ��ʼ���꣬���д�С
			int x = 100;
			int y = 75;
			int width = 100;
			int height = 100;
			// �ο�ͼ���С
			int clientWidth = 300;
			int clientHeight = 250;

			File file = new File("D:\\PCM Project\\upload\\tmp\\1.gif");
			BufferedImage image = ImageIO.read(file);
			double destWidth = image.getWidth();
			double destHeight = image.getHeight();

			if (destWidth < width || destHeight < height)
				throw new Exception("Դͼ��СС�ڽ�ȡͼƬ��С!");

			double widthRatio = destWidth / clientWidth;
			double heightRatio = destHeight / clientHeight;

			x = Double.valueOf(x * widthRatio).intValue();
			y = Double.valueOf(y * heightRatio).intValue();
			width = Double.valueOf(width * widthRatio).intValue();
			height = Double.valueOf(height * heightRatio).intValue();

			System.out.println("�ü���С  x:" + x + ",y:" + y + ",width:" + width
					+ ",height:" + height);

			/************************ ����������������� *************************/
			String formatName = getImageFormatName(file);
			String pathSuffix = "." + formatName;
			String pathPrefix = getFilePrefixPath(file);
			String targetPath = pathPrefix + System.currentTimeMillis()
					+ pathSuffix;
			targetPath = ImageCutterUtil.cutImage(file.getPath(), targetPath,
					x, y, width, height);

			String bigTargetPath = pathPrefix + System.currentTimeMillis()
					+ pathSuffix;
			ImageCutterUtil.zoom(targetPath, bigTargetPath, 100, 100);

			String smallTargetPath = pathPrefix + System.currentTimeMillis()
					+ pathSuffix;
			ImageCutterUtil.zoom(targetPath, smallTargetPath, 50, 50);

			/************************ ����JDK Image I/O �������(JDK̽��ʧ��) *************************/
			// File destFile = new File(targetPath);
			// ImageCutterUtil.cutImage(file, destFile, x, y, width, height);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
