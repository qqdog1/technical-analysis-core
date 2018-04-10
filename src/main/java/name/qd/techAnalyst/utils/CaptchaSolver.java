package name.qd.techAnalyst.utils;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Size;
import org.bytedeco.javacpp.tesseract.TessBaseAPI;
import static org.bytedeco.javacpp.opencv_imgcodecs.IMREAD_GRAYSCALE;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import static org.bytedeco.javacpp.opencv_imgproc.CV_THRESH_OTSU;
import static org.bytedeco.javacpp.opencv_imgproc.MORPH_RECT;
import static org.bytedeco.javacpp.opencv_imgproc.THRESH_BINARY;
import static org.bytedeco.javacpp.opencv_imgproc.erode;
import static org.bytedeco.javacpp.opencv_imgproc.getStructuringElement;
import static org.bytedeco.javacpp.opencv_imgproc.threshold;
import static org.bytedeco.javacpp.opencv_photo.fastNlMeansDenoising;

public class CaptchaSolver {
	private TessBaseAPI tess;
	
	public CaptchaSolver() {
		tess = new TessBaseAPI();
		if (tess.Init("./bsr/tessdata/", "eng") != 0) {
            throw new IllegalStateException("Could not initialize tesseract.");
        }
	}
	
	public void end() {
        tess.End();
    }
	
	private Mat clean_captcha(String file) {
        Mat captcha = imread(file, IMREAD_GRAYSCALE);
        if (captcha.empty()) {
            System.out.println("Can't read captcha image '" + file + "'");
            return captcha;
        }

        Mat captcha_bw = new Mat();
        threshold(captcha, captcha_bw, 128, 255, THRESH_BINARY | CV_THRESH_OTSU);

        Mat captcha_erode = new Mat();
        Mat element = getStructuringElement(MORPH_RECT, new Size(3, 3));
        erode(captcha_bw, captcha_erode, element);

        // Some cosmetic
        Mat captcha_denoise = new Mat();
        fastNlMeansDenoising(captcha_erode, captcha_denoise, 7, 7, 21);

        return captcha_denoise;
    }

    private String image_to_string(Mat img) {
        BytePointer outText;

        tess.SetImage(img.data(), img.cols(), img.rows(), 1, img.cols());

        outText = tess.GetUTF8Text();
        String s = outText.getString();

        // Destroy used object and release memory
        outText.deallocate();

        return s.replaceAll("[^0-9-A-Z]", "");
    }

    public String solve(String file) {
        return image_to_string(clean_captcha(file));
    }
}
