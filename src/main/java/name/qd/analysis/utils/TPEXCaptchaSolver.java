/*
 * CaptchaSolver.java
 *
 * Copyright (c) 2017 Yen-Chin, Lee <coldnew.tw@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */

package name.qd.analysis.utils;


import static org.bytedeco.javacpp.opencv_core.subtract;
import static org.bytedeco.javacpp.opencv_imgcodecs.IMREAD_GRAYSCALE;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import static org.bytedeco.javacpp.opencv_imgproc.CHAIN_APPROX_SIMPLE;
import static org.bytedeco.javacpp.opencv_imgproc.MORPH_RECT;
import static org.bytedeco.javacpp.opencv_imgproc.RETR_EXTERNAL;
import static org.bytedeco.javacpp.opencv_imgproc.THRESH_BINARY;
import static org.bytedeco.javacpp.opencv_imgproc.blur;
import static org.bytedeco.javacpp.opencv_imgproc.boundingRect;
import static org.bytedeco.javacpp.opencv_imgproc.erode;
import static org.bytedeco.javacpp.opencv_imgproc.findContours;
import static org.bytedeco.javacpp.opencv_imgproc.getStructuringElement;
import static org.bytedeco.javacpp.opencv_imgproc.resize;
import static org.bytedeco.javacpp.opencv_imgproc.threshold;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.MatVector;
import org.bytedeco.javacpp.opencv_core.Rect;
import org.bytedeco.javacpp.opencv_core.Size;
import org.bytedeco.javacpp.tesseract.TessBaseAPI;


public class TPEXCaptchaSolver {
	private TessBaseAPI tess;

	public TPEXCaptchaSolver() {
		tess = new TessBaseAPI();
		if (tess.Init("./bsr/tessdata/", "eng") != 0) {
            throw new IllegalStateException("Could not initialize tesseract.");
        }
	}

	public void end() {
		tess.End();
	}

	private String clean_captcha(String file) {
		Mat captcha = imread(file, IMREAD_GRAYSCALE);
		if (captcha.empty()) {
			System.out.println("Can't read captcha image '" + file + "'");
			return "";
		}

		Mat captcha_bw = new Mat();
		threshold(captcha, captcha_bw, 125, 255, THRESH_BINARY);

		Mat invertcolormatrix = new Mat(captcha_bw.rows(), captcha_bw.cols(), captcha_bw.type(), new opencv_core.Scalar(255, 255));
		subtract(invertcolormatrix, captcha_bw, captcha_bw);

		Mat captcha_erode = new Mat();
		Mat element = getStructuringElement(MORPH_RECT, new Size(4, 4));
		erode(captcha_bw, captcha_erode, element);

		Mat captcha_blur = new Mat();
		blur(captcha_erode, captcha_blur, new Size(2, 2));

		List<Rect> rectList = getContourArea(captcha_blur);
		rectList = sort(rectList);
		String result = "";
		for (int i = 0; i < rectList.size(); i++) {
			Mat mat = captcha_blur.apply(rectList.get(i));
			Mat captcha_resize = new Mat();
			resize(mat, captcha_resize,new Size(100,100));
			result += image_to_string(captcha_resize);
		}
		return result;
	}

	private String image_to_string(Mat img) {
		BytePointer outText;

		tess.SetImage(img.data(), img.cols(), img.rows(), 1, img.cols());

		outText = tess.GetUTF8Text();
		String s = outText.getString();

		outText.deallocate();

		return s.replaceAll("[^0-9-A-Z]", "");
	}

	public String solve(String file) {
		return clean_captcha(file);
	}

	private ArrayList<Rect> getContourArea(Mat mat) {
		Mat image = mat.clone();
		MatVector contours = new MatVector();
		findContours(image, contours, RETR_EXTERNAL, CHAIN_APPROX_SIMPLE);
		Rect rect = null;
		ArrayList<Rect> arr = new ArrayList<Rect>();
		for (int i = 0; i < contours.size(); i++) {
			rect = boundingRect(contours.get(i));
			if (rect.width() > 10 && rect.height() > 10) {
				arr.add(rect);
			}
		}
		return arr;
	}

	private List<Rect> sort(List<Rect> rectList) {
		Map<Integer, Rect> xMap = new TreeMap<>();
		for (Rect rect : rectList) {
			xMap.put(rect.x(), rect);
		}
		List<Rect> rects = new ArrayList<Rect>();
		rects.addAll(xMap.values());
		return rects;
	}
}