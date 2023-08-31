package com.opencvcamera;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import org.opencv.videoio.VideoCapture;
public class Camera extends JFrame 
{
	//Camera Screen 
	private JLabel cameraScreen;
	private JButton btnCapture,btnOpenPic;
	private VideoCapture capture;
	private Mat image;
	private ImageIcon icon;
	private JPanel gallery;
		
	private boolean clicked = false;
	
	public Camera() 
	{
		//Design
		setLayout(null);
		
		cameraScreen = new JLabel();
		cameraScreen.setBounds(0,0,640,480);
		add(cameraScreen);
		
		btnCapture = new JButton("Capture");
		btnCapture.setBounds(250,480,80,40);
		add(btnCapture);
	
		btnCapture.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				clicked  = true;
			}
		});
		
		btnOpenPic = new JButton("Gallery");
		btnOpenPic.setBounds(350,480,80,40);
		add(btnOpenPic);
		
		btnOpenPic.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if(e.getSource()== btnOpenPic)
				{
					
					/*	int result = chooser.showOpenDialog(null);
					if(result == JFileChooser.APPROVE_OPTION);
					{
						String name =chooser.getSelectedfile().getPath();
						image = Toolkit.getDefaultToolkit().getScaledInstance(300,300,image.SCALE_DEFAULT);
						icon = new ImageIcon(scaledImage);
						label.setIcon(icon);
					} */
				} 
			}
		});
		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				super.windowClosing(e);
				capture.release();
				image.release();
				System.exit(0);
			}
		});
		
		setSize(new Dimension(640,560));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	//Create camera
	public void startCamera()
	{
		capture = new VideoCapture(0);
		image = new Mat();
		byte[] imageData;
		
		ImageIcon icon;
		while(true)
		{
			//read image to matrix
			capture.read(image);
			//convert matrix to byte
			final MatOfByte buf = new MatOfByte();
			Imgcodecs.imencode(".jpg", image, buf);
			
			imageData = buf.toArray();
			//add to JLabel
			icon = new ImageIcon(imageData);
			cameraScreen.setIcon(icon);
			//Capture and Save To File
			if(clicked)
			{
				//Prompt for enter image name
				String name = JOptionPane.showInputDialog(this,"Enter Image Name");
			/*	if(name == null)
				{
					name = new SimpleDateFormat("yyyy-mm-dd-hh-mm-ss").format(new Date());
				}*/
				//write to file
				Imgcodecs.imwrite("images/" + name + ".jpg", image);
				
				clicked = false;
			}
		}
	}
	public static void main(String[] args) 
	{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		EventQueue.invokeLater(new Runnable() 
		{
			public void run()
			{
				Camera camera = new Camera();
				//start camera  in thread
				new Thread(new Runnable()
				{
					public void run()
					{
						camera.startCamera();
					}
				}).start();
			}
		});
	}
}
