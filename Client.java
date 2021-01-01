
/* 
	author - Sisir Reddy, 1810110250
		 */
		import javax.swing.*;
		import java.awt.*;
		import java.io.*;
		import java.net.*;
		import java.nio.ByteBuffer;
		import java.util.*;
		import java.util.List;
		import javax.swing.*;
		import java.awt.*;
		import java.awt.image.BufferedImage;
		import java.awt.Color;
		import java.awt.Graphics.*;
		import java.awt.event.*;
		import javax.swing.UIManager;
		import javax.imageio.ImageIO;
		
		public class Client extends JFrame
		
		{ 
		
		
			static Image globImg;
			Client(){
				repaint();
			}
			public static void main(String args[]){		
				try
				{ 	//setting up client
					Socket client = new Socket("127.0.0.1",7999);
					DataInputStream ip = new DataInputStream(client.getInputStream());
					DataOutputStream op = new DataOutputStream(client.getOutputStream());
					Scanner input = new Scanner(System.in);
		
					System.out.println("Enter the start node between a and e");
					char start = input.next().charAt(0);
					start = Character.toUpperCase(start);
					op.writeInt(start);
					op.flush();
					
					System.out.println("Enter the end node between a and e");
					char end = input.next().charAt(0);
					end = Character.toUpperCase(end);
					op.writeInt(end);
					op.flush();
		
					System.out.println("Enter the path length");
					int pathLength = input.nextInt();
					System.out.println("Path Length "+pathLength);
					op.writeInt(pathLength);
					op.flush();
		
					int[][] adjMatrix = new int[5][5];
					System.out.println("Enter a 5X5 adj matrix only");
					for(int i=0;i<5;++i)
						for(int j=0;j<5;++j){
							adjMatrix[i][j] = input.nextInt();
							op.writeInt(adjMatrix[i][j]);
							op.flush();
						}
					
					char resp = ip.readChar(); // reading response from server
					if(resp == 'Y'){
						System.out.println("Yes, there exists a path of length " + pathLength + " from node " + start +" to node "+end);
					}
					else if(resp =='N'){
						System.out.println("No, there exists no path of length " + pathLength + " from node " + start +" to node "+end);
					}
					
		
					// getting img from server
					byte[] sizeAr = new byte[4];
					ip.read(sizeAr);
					int size = ByteBuffer.wrap(sizeAr).asIntBuffer().get();
					byte[] imageAr = new byte[size];
					ip.read(imageAr);
					BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageAr));
					globImg = image;
					JFrame frame = new Client();
					frame.setTitle("Client");
					frame.setSize(1000, 1000);
					frame.setVisible(true);
		
					client.close();
				}
				catch(IOException ex){
				System.out.println("F");
				System.out.println(ex);
				}
			}
		
			public void paint(Graphics g) {
				 super.paint(g);
				
		
				 Image img = globImg;
				 g.drawImage(img, 20,120,this);
				
				 
			 }
		
			}