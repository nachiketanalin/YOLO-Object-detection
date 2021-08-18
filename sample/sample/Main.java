package sample;

import org.opencv.core.Point;
import org.opencv.core.*;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;
import org.opencv.videoio.VideoCapture;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


public class Main {

    interface welcome{
        public void print();
    }
    static class welcomeMsg implements welcome{

        public void print(){
            System.out.println("WELCOME TO THE PROECT, DEVELOPER!\n\n");
        }
    }

    static class implDetails
    {
        LocalDate dt = LocalDate.now();
        String date = dt.toString();
    }
    static class timeOfExecution extends implDetails
    {
        void printDate()
        {
            System.out.println("DATE IS " + date);
        }
        void printTime()
        {
            LocalTime tm = LocalTime.now();
            System.out.println("TIME IS " + tm);
        }
    }

    static class details{
        private String student1 = "Nachiketa";
        private String student2 = "Nikhil";
        private String USN1 = "1RV19IS030";
        private String USN2 = "1RV19IS032";

        public void getDetails()
        {
            ArrayList<String> names = new ArrayList<>();
            ArrayList<String> USN = new ArrayList<>();
            names.add(student1);
            names.add(student2);
            USN.add(USN1);
            USN.add(USN2);

            System.out.println("Students are ");
            for (String x : names)
                System.out.print(x + " ");
            System.out.println("USNs are ");
            for (String x : USN)
                System.out.print(x + " ");
        }
    }

    private static List<String> getOutputNames(Net net) {
        List<String> names = new ArrayList<>();

        List<Integer> outLayers = net.getUnconnectedOutLayers().toList();
        List<String> layersNames = net.getLayerNames();

        outLayers.forEach((item) -> names.add(layersNames.get(item - 1)));
        return names;
    }

    static String filePath = "C:\\Users\\DELL\\Downloads\\testvideo2.mp4";//cars, testvideo2

    public static void main(String[] args) throws InterruptedException {

        // interface
        welcome obj = new welcomeMsg();
        obj.print();

        //encapsulation
//        details student = new details();
//        student.getDetails();


        // Load the openCV 4.5.2 dll //
        System.load("C:\\Users\\DELL\\Downloads\\OpenCV\\opencv\\build\\java\\x64\\opencv_java452.dll");
        // Load weights
        String modelWeights = "C:\\Users\\DELL\\Downloads\\yolov3.weights";
        // Load CFG file
        String modelConfiguration = "C:\\Users\\DELL\\Downloads\\yolov3.cfg.txt";
//        String filePath = "C:\\Users\\DELL\\Downloads\\cars.mp4"; //My video  file to be analysed//


        VideoCapture cap = new VideoCapture(filePath);// Loading video using the videocapture method//
        Mat frame = new Mat(); // define a matrix to extract and store pixel info from video//
        Mat dst = new Mat ();
        //cap.read(frame);
        JFrame jframe = new JFrame("DETECTION IN VIDEO"); // the lines below create a frame to display the resultant video with object detection and localization//
        JLabel vidpanel = new JLabel();
        jframe.setContentPane(vidpanel);
        jframe.setSize(600, 600);

//        jframe.setVisible(true);// we instantiate the frame here//

        JFrame parent = new JFrame(("WELCOME TO OUR PROJECT"));
        JLabel vidpanel2 = new JLabel();
        parent.setContentPane(vidpanel2);
        parent.setSize(800, 600);
        parent.setVisible(true);
//        parent.setBackground(Color.);

        TextArea ta = new TextArea();
        ta.setBounds(150,100,500,50);
        parent.add(ta);

        //creating buttons
        JButton analyse, open;
        JLabel txt = new JLabel("HELLO AND WELCOME");
        txt.setFont(new Font("Times New Roman", Font.BOLD, 20));
        txt.setBounds(300, 10, 500, 50);
        parent.add(txt);

        JLabel txt2 = new JLabel("NACHIKETA NALIN     1RV19IS030");
        txt2.setFont(new Font("Times New Roman", Font.BOLD, 20));
        txt2.setBounds(230, 325, 500, 50);
        parent.add(txt2);

        JLabel txt3 = new JLabel("NIKHIL SANDILYA      1RV19IS032");
        txt3.setFont(new Font("Times New Roman", Font.BOLD, 20));
        txt3.setBounds(230, 355, 500, 50);
        parent.add(txt3);
//        txt.setText("HELLO AND WELCOME");

        JLabel txt4 = new JLabel("HOPE YOU LIKED OUR PROJECT! COME AGAIN!");
        txt4.setFont(new Font("Times New Roman", Font.BOLD, 20));
        txt4.setForeground(Color.ORANGE);
        txt4.setBounds(180, 355, 500, 180);
        parent.add(txt4);

        //OPEN BUTTON
        open = new JButton("OPEN");
        open.setBounds(285,190,100,30);
        open.addActionListener(actionEvent -> {

            if(actionEvent.getSource()==open){
                JFileChooser fc=new JFileChooser();
                int i=fc.showOpenDialog(open);
                if(i==JFileChooser.APPROVE_OPTION){
                    File f=fc.getSelectedFile();
                    String x = f.getPath();
                    filePath=x;
                    try{
                        ta.setText(filePath);
                        System.out.println("SUCCESSFULLY ACCESSED VIDEO FILE at " + filePath+"\n");
//                        System.out.println(filePath);
                    }catch (Exception ex) {ex.printStackTrace();  }
                }
            }
//                analyse.setEnabled(false);
        });
        parent.add(open);
//        System.out.println(filePath);
//        VideoCapture cap = new VideoCapture(filePath);// Loading video using the videocapture method//


        analyse = new JButton("ANALYSE");
        analyse.setBounds(390,190,100,30);
        parent.add(analyse);

        Net net = Dnn.readNetFromDarknet(modelConfiguration, modelWeights);

        Size sz = new Size(288,288);

        List<Mat> result = new ArrayList<>();
        List<String> outBlobNames = getOutputNames(net);
        analyse.addActionListener(e -> {
//            System.out.println(filePath);
            jframe.setVisible(true);
            int x = 1;
            while (x++<3) {
//
                if (cap.read(frame)) {

                    Mat blob = Dnn.blobFromImage(frame, 0.00392, sz, new Scalar(0), true, false); // We feed one frame of video into the network at a time, we have to convert the image to a blob. A blob is a pre-processed image that serves as the input.//
                    net.setInput(blob);

                    net.forward(result, outBlobNames); //Feed forward the model to get output //

                    // outBlobNames.forEach(System.out::println);
                    // result.forEach(System.out::println);

                    float confThreshold = 0.6f; //Insert thresholding beyond which the model will detect objects//
                    List<Integer> clsIds = new ArrayList<>();
                    List<Float> confs = new ArrayList<>();
                    List<Rect2d> rects = new ArrayList<>();

                    for (int i = 0; i < result.size(); ++i)
                    {
                        // each row is a candidate detection, the 1st 4 numbers are
                        // [center_x, center_y, width, height], followed by (N-4) class probabilities
                        Mat level = result.get(i);
                        for (int j = 0; j < level.rows(); ++j)
                        {
                            Mat row = level.row(j);
                            Mat scores = row.colRange(5, level.cols());
                            Core.MinMaxLocResult mm = Core.minMaxLoc(scores);
                            float confidence = (float)mm.maxVal;
                            Point classIdPoint = mm.maxLoc;
                            if (confidence > confThreshold)
                            {
                                int centerX = (int)(row.get(0,0)[0] * frame.cols()); //scaling for drawing the bounding boxes//
                                int centerY = (int)(row.get(0,1)[0] * frame.rows());
                                int width   = (int)(row.get(0,2)[0] * frame.cols());
                                int height  = (int)(row.get(0,3)[0] * frame.rows());
                                int left    = centerX - width  / 2;
                                int top     = centerY - height / 2;

                                clsIds.add((int)classIdPoint.x);
                                confs.add((float)confidence);
                                rects.add(new Rect2d(left, top, width, height));
                            }
                        }
                    }
                    float nmsThresh = 0.5f;
                    MatOfFloat confidences = new MatOfFloat(Converters.vector_float_to_Mat(confs));
                    Rect2d[] boxesArray = rects.toArray(new Rect2d[0]);
                    MatOfRect2d boxes = new MatOfRect2d(boxesArray);
                    MatOfInt indices = new MatOfInt();
                    Dnn.NMSBoxes(boxes, confidences, confThreshold, nmsThresh, indices); //We draw the bounding boxes for objects here//

                    int [] ind = indices.toArray();
                    int j=0;
                    for (int i = 0; i < ind.length; ++i)
                    {
                        int idx = ind[i];
                        Rect2d box = boxesArray[idx];
                        Imgproc.rectangle(frame, box.tl(), box.br(), new Scalar(0,0,255), 2);
                        //i=j;

                        System.out.println(idx);
                    }
        //             Imgcodecs.imwrite("D://out.png", image);
        //            System.out.println("Image Loaded");
                    ImageIcon image = new ImageIcon(Mat2bufferedImage(frame)); //setting the results into a frame and initializing it //
                    vidpanel.setIcon(image);
                    vidpanel.repaint();
                    // System.out.println(j);
                    // System.out.println("Done");

                }
            }
        });
    }


    //	}
    private static BufferedImage Mat2bufferedImage(Mat image) {   // The class described here  takes in matrix and renders the video to the frame  //
        MatOfByte bytemat = new MatOfByte();
        Imgcodecs.imencode(".jpg", image, bytemat);
        byte[] bytes = bytemat.toArray();
        InputStream in = new ByteArrayInputStream(bytes);
        BufferedImage img = null;
        try {
            img = ImageIO.read(in);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return img;
    }
}