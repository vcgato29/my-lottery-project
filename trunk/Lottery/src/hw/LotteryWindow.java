package hw;

import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;



import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.beans.DesignMode;
import java.util.*;
import java.util.Timer;
import java.io.*;

import java.net.URL;

class LotteryWindow {
	JFrame frame = new JFrame();
	JLabel label_title = new JLabel("");
	JLabel label_name = new JLabel("");
	
	JLabel label_num = new JLabel("");
	ImagePanel photo = new ImagePanel();
	private JPanel imagePanel;

	int index; 
	String name = null;
	ArrayList<Chooser> array = null; 
	HashMap<String,Chooser> hsMap = null; 
	
	Timer timer;

	boolean enterFlag = false;
	boolean spaceFlag = false;
	boolean slashFlag = false;
	
	int avatarWidth=550;
	int avatarHeight=550;
	
	private String ndImg1 = null;
	private String ndImg2 = null;

	
	private String config_path = null;
	
	private String chooser_path = null;
	private String output = null;
	

	private Font getFont() {
		String fName = "FZHTJW.TTF";
		Font font = null;
		try {
			InputStream is = LotteryWindow.class.getResourceAsStream(fName);
			
			font = Font.createFont(Font.TRUETYPE_FONT, is);			
			font = font.deriveFont(12f);

			
		} catch (Exception ex) {  
			ex.printStackTrace();  
			System.err.println(fName + " not loaded. Using serif font.");
			font = new Font("serif", Font.PLAIN, 12);
		}
		
		return font;
	}
	
	private String readProperties(String key)  {
		config_path = System.getProperty("user.dir") + "\\config.properties";
		Properties props = new Properties();
		File file = new File(config_path);
		FileInputStream inputStream1 = null;
		String value = "";
		try {
			inputStream1 = new FileInputStream(file);
			props.load(inputStream1);
			value = props.getProperty(key);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  finally {
			return value;
		}
		
		
	}
	
	private boolean existFile(String filename) {		 
		File file=new File(chooser_path+filename);    
		if(file.exists())    
		{    
		   return true;
		} else {
			return false;
		}
	}
	private  void transferFile(String oldPath,String newPath) throws Exception {  
        
        int byteread = 0;  
        File oldFile = new File(oldPath);  
        FileInputStream fin = null;  
        FileOutputStream fout = null;  
        try{  
            if(oldFile.exists()){  
                fin = new FileInputStream(oldFile);  
                fout = new FileOutputStream(newPath);  
                byte[] buffer = new byte[fin.available()];  
                while( (byteread = fin.read(buffer)) != -1){  
                    fout.write(buffer,0,byteread);  
                }  
                if(fin != null){  
                    fin.close();  
                    delFile(oldFile);  
                }  
            }
//            else{  
//            	System.out.println(oldFile);
//                throw new Exception("需要转移的文件不存在!");  
//            }  
        }catch(Exception e){  
            e.printStackTrace();  
            throw e;  
        }finally{  
            if(fin != null){  
                fin.close();  
            }  
        }  
    }  
	
	private void delFile(File file) throws Exception {  
        if(!file.exists()) {  
            throw new Exception("文件"+file.getName()+"不存在,请确认!");  
        }  
        if(file.isFile()){  
            if(file.canWrite()){  
                file.delete();  
            }else{  
                throw new Exception("文件"+file.getName()+"只读,无法删除,请手动删除!");  
            }  
        }else{  
            throw new Exception("文件"+file.getName()+"不是一个标准的文件,有可能为目录,请确认!");  
        }  
    }  
	
	private void writeOutput(String oldfile, String newfile)  {
		
		
		try {
			transferFile(oldfile,newfile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public LotteryWindow() {
		array = new ArrayList<Chooser>();
		
		hsMap = new HashMap<String,Chooser>(); 
		try {
			chooser_path = System.getProperty("user.dir") + "\\images\\";
			
			ndImg1 = readProperties("nd1");
			
			ndImg2 = readProperties("nd2");
			
			//ndImg3 = readProperties("nd3");
			
			File file = new File(chooser_path);
			
			File[] files = file.listFiles();

			int num = 0;
			for(File f:files) {
				name = f.getName();
				
				Chooser chooser = new Chooser(name, ++num);
				//array.add(chooser);
				hsMap.put(name, chooser);
			}
			array.addAll(hsMap.values());
		} catch (Exception e) {
			e.printStackTrace();
		}

		URL bgImageUrl = getClass().getResource("/images/bg.jpg");

		
		ImageIcon background = new ImageIcon(bgImageUrl);
		
		JLabel imgLabel = new JLabel(background);
		
		imgLabel.setBounds(0, 0, 1024,
				768);
		
		//imgLabel.setBackground(Color.WHITE);
		
		imagePanel = (JPanel) frame.getContentPane();
		imagePanel.setOpaque(false);
				
		// 内容窗格默认的布局管理器为BorderLayout
		imagePanel.setLayout(new BoxLayout(imagePanel,BoxLayout.Y_AXIS));
		
		imagePanel.setBackground(Color.WHITE);
		
		JLabel blank_label = new JLabel(" ");
		blank_label.setFont(new Font("宋体",Font.PLAIN,100));		
		frame.add(blank_label);
		
		Font font = getFont();
		


		photo.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		

		frame.addKeyListener(new Key());
	
		photo.setVisible(false);
		frame.add(photo);
		
		
		label_title.setFont(getFont());

		label_title.setForeground(Color.WHITE);
		
		label_title.setAlignmentX(Component.CENTER_ALIGNMENT);
		label_title.setBounds(0, 0,frame.getWidth(), frame.getHeight());
		
		//label_title.setText(getLabel("WILL.jpg"));
		label_title.setVisible(false);
		
		frame.add(label_title);
		
//		JLabel blank_label1 = new JLabel(" ");
//		blank_label1.setFont(new Font("宋体",Font.PLAIN,20));		
//		frame.add(blank_label1);
		
		
		
		blank_label.setFont(new Font("宋体",Font.PLAIN,20));		
		frame.add(blank_label);
		
		
		
		
		frame.getLayeredPane().setLayout(null);
		frame.getLayeredPane().add(imgLabel, new Integer(Integer.MIN_VALUE));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setUndecorated(true); 
		frame.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
		
		
		frame.setBackground(Color.BLACK);
		//frame.setState(Frame.MAXIMIZED_BOTH);
		frame.setSize(1024,768);
		//frame.setSize(background.getIconWidth(), background.getIconHeight());
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setTitle("抽奖");
		
		
		
		index = 0;
	}

	boolean run = false;
	class Key implements KeyListener {
        
		public void keyPressed(KeyEvent arg0) {
			int keycode =arg0.getKeyCode();
			output = System.getProperty("user.dir") + "\\output\\";
			switch(keycode) {
				 case KeyEvent.VK_SPACE:
					if (!name.equals(ndImg1)) {
						 if (!name.equals(ndImg2)) {
							 if (!spaceFlag) {	
								label_title.setText(getLabel(name));		

								
								run = false;

								
								frame.repaint();
								spaceFlag = true;
								enterFlag = false;
								slashFlag = false;
								//System.out.println(name);
							 }
						 }
					}
									
					break;
				 case KeyEvent.VK_ENTER:
					 //label_title.setText("");
					 photo.setVisible(true);
					 label_title.setVisible(true);
					
					 if (!enterFlag) {	
						
						// mytask = new MyTask();
						// mytask.start();
						 
						 new Thread() {
								public void run() {
									run = true;
									while (run) {
										int a = (int) (Math.random() * array.size());
										if (array.get(a)==null) break;
										
										name = array.get(a).getName();
										
										//index = i;
										 
										label_title.setText(getLabel(name));
										
										frame.repaint();
										
										try {
											Thread.sleep(200);
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								}
							}.start();
						//timer.scheduleAtFixedRate(mytask, 0,500);
						
					 }
					
					enterFlag = true;
					slashFlag = false;
					spaceFlag = false;
					break;
				 case KeyEvent.VK_SLASH:
					if (!slashFlag) {
						 //timer.cancel();	
//						mytask.stop();
						run = false;
						 if (existFile(ndImg1)) {
							 name = ndImg1;
						 } else if (existFile(ndImg2)){
							 name = ndImg2;
						 }
						 
						// System.out.println("内定============"+name);
						frame.repaint();
						 
						label_title.setText(getLabel(name));
						 
						slashFlag = true;
						enterFlag = false;
						spaceFlag = false;
					}
					break;
				 case KeyEvent.VK_ESCAPE:
					 System.exit(0);
					break;
			}
			
		}

		public void keyReleased(KeyEvent arg0) {

			
		}

		public void keyTyped(KeyEvent arg0) {
			
		}
		
	}
	
	private String getLabel(String label) {
		return "              "+label.substring(0,label.lastIndexOf("."))+"              ";
	}
	



	private Dimension getImageSize(String filepath) {
		avatarHeight=550;
		avatarWidth = 550;
		int imageWidth,imageHeight;
		
		Image srcImage = null;
		try {
			srcImage = ImageIO.read(new File(filepath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        //得到图片的原始大小， 以便按比例压缩。  
        imageWidth = srcImage.getWidth(null);  
        imageHeight = srcImage.getHeight(null);
//		imageWidth = 1024;
//		imageHeight = 768;
        
        if (imageWidth>imageHeight) {
        	
        	avatarHeight = imageHeight*avatarWidth/imageWidth;
        } else if (imageWidth<imageHeight) {
        	
        	avatarWidth = imageWidth*avatarHeight/imageHeight;
        } 
        
        return new Dimension(avatarWidth,avatarHeight);
	}
	class ImagePanel extends JPanel {
		
		
		public void paintComponent(Graphics g) {
			 
			
			String imagePath =  System.getProperty("user.dir")+"\\images\\"+name;
						
			
			Dimension d = getImageSize(imagePath);
			avatarWidth = d.width;
			avatarHeight = d.height;
			
			Image image = new ImageIcon(imagePath).getImage();

			g.drawImage(image, 
							(this.getWidth()-avatarWidth)/2+5, 
							(this.getHeight()-avatarHeight)/2+80,
							avatarWidth,avatarHeight, 
							this);
			
			
			if (spaceFlag||slashFlag) {
				
				writeOutput(chooser_path+name,output+name);
				hsMap.remove(name);
				
				array.clear();
				array.addAll(hsMap.values());

			}
			
		}
		

	}
}

class Chooser {
	private String name;
	private int num;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public Chooser(String name, int num) {
		this.name = name;
		this.num = num;
	}

	public void print() {
		System.out.println(num + " " + name);
	}
}
