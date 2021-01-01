
/* Import java Packages */
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
		import java.util.concurrent.TimeUnit;
		
		import javax.swing.*;
		import java.awt.*;
		import java.awt.image.BufferedImage;
		import java.awt.Color;
		import java.awt.Graphics.*;
		import java.awt.event.*;
		import javax.swing.UIManager;
		import javax.imageio.ImageIO;
		
		
		
		public class Server extends JFrame{
			static int[] globPt = new int[2];
			static Image globImg;
			static int[][] adjMatrix = new int[5][5];
			static int requiredPath;
			static int flushResponseFlag=0; // checks if the path length exists
			static char start;
			static char end;
			static ArrayList<Integer>[] adjList; 
			
		
		public static void main(String args[]) throws Exception{
			try {
				
				ServerSocket serverSocket = new ServerSocket(7999);
				System.out.println("Server running");
				while(true) {
					Socket socket = serverSocket.accept();
					DataInputStream ip = new DataInputStream(socket.getInputStream());
					DataOutputStream op = new DataOutputStream(socket.getOutputStream());
					
		
					start  = (char)ip.readInt();
					end = (char)ip.readInt();
					int pathLength = ip.readInt();
					requiredPath = pathLength;
					
		
					for(int i=0;i<5;++i)
						for(int j=0;j<5;++j){
							adjMatrix[i][j] = ip.readInt();
						}
					for(int i=0;i<5;++i){
						for(int j=0;j<5;++j){
		
						}
		
					}
					InitGraph(); // initialising the graph using the above information;
					
					int src = Math.abs((int)'A' - (int)start); // src to start finding paths
					int dest = Math.abs((int)'A' - (int)end);  // dest
					
		
		
		
					generatePaths(src, dest); // updates the flushResponseFlag
					if(flushResponseFlag==0){ // if 0 no path of that length exists
						op.writeChar('N');
						op.flush();
					}
					else{
						op.writeChar('Y');
						op.flush();
					}
		
					flushResponseFlag=0;
		
					JFrame frame = new Server();
					frame.setSize(1000, 1000);
					frame.setTitle("Server ");
					frame.setVisible(true);
		
					
					while(globImg==null){				
						System.out.println("Waiting for image to be created and flushed");
		
					}
					if(globImg!=null){ 
						// Got this method from the internet
						//sending the client the image
						ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
						BufferedImage temp = toBufferedImage(globImg);
						ImageIO.write(temp, "jpg", byteArrayOutputStream);
						byte[] size = ByteBuffer.allocate(4).putInt(byteArrayOutputStream.size()).array();
						op.write(size);
						op.write(byteArrayOutputStream.toByteArray());
						op.flush();
						globImg=null;
						
						
					}
					
					
				}
		
			}
			catch(IOException ex) {
				
			}
		
		
		}
		
		public static BufferedImage toBufferedImage(Image img) // used to convert Image to buffered image to send to the client
		{ 		// Code from the internet
			if (img instanceof BufferedImage)
			{
				return (BufferedImage) img;
			}
		
			// Create a buffered image with transparency
			BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		
			// Draw the image on to the buffered image
			Graphics2D bGr = bimage.createGraphics();
			bGr.drawImage(img, 0, 0, null);
			bGr.dispose();
		
			// Return the buffered image
			return bimage;
		}
		
		public void paint(Graphics g) {
			 super.paint(g);
		
			 Image img = drawGraph();
				if(img!=null){ // once img created the global img variable set to this image
					globImg = img;
					ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				}
			
			 
		 }
		
		 private Image drawGraph() {
			BufferedImage bufferedImage = new BufferedImage(700,700,BufferedImage.TYPE_INT_RGB);
			Graphics g = bufferedImage.getGraphics();
			int[][] nodeLocations = new int[5][2]; // presetting node locations
			nodeLocations[0][0] = 200;
			nodeLocations[0][1] = 100;
			
			nodeLocations[1][0] = 100;
			nodeLocations[1][1] = 260;
			
			nodeLocations[2][0] = 200;
			nodeLocations[2][1] = 320;
			
			nodeLocations[3][0] = 300;
			nodeLocations[3][1] = 260;
			
			nodeLocations[4][0] = 300;
			nodeLocations[4][1] = 100;
		
		
		
			
			
			g.setColor(Color.white);
			 g.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
			 
			 g.setColor(Color.red);
			 int xf = 50;int yf=30;
			 for(int i=0;i<5;++i) {
					 int temp = 65+i;
					 
					String a = String.valueOf((char)temp);
					g.setColor(Color.green);
					g.drawString(a,nodeLocations[i][0]+10, nodeLocations[i][1]+18);
					g.setColor(Color.black);
					g.drawOval(nodeLocations[i][0], nodeLocations[i][1], 30, 30);
			 }
			 // drawing the graph according to the adjMatrix.
			 
			 for (int i=0;i<5;++i) {
				 for(int j=0;j<5;++j) {
					 if(adjMatrix[i][j]!=0) {
						 if(adjMatrix[j][i]==0) {
							 
							 int x1 =nodeLocations[i][0];
							 int y1 =nodeLocations[i][1];
							 int x2 =nodeLocations[j][0];
							 int y2 =nodeLocations[j][1];
							 
							 float[] mid = new float[2];
							 mid[0] = ((float)x1 + (float)x2)/2;
							 mid[1] = ((float)y1 + (float)y2)/2;
							 x1 = (int) ((mid[0] + x1)/2);
							 y1 = (int) ((mid[1] + y1)/2);
							 
							 
							 x2 = (int) ((mid[0] + x2)/2);
							 y2 = (int) ((mid[1] + y2)/2);
							 
							 
							 
							 g.drawLine(x1 +10, y1+18, x2+10, y2+18);
		
							 
							 
							 
							 float[] pointPer = new float[2];
							 pointPer[0] =  (((x1+x2)/2) + x2)/2;
							 pointPer[1] =  (((y1+y2)/2) + y2)/2;
		
							 float m;
							 float[] p1 = new float[2];
							 float[] p2 = new float[2];
							 // drawing the arrows using the equation of lines perpendicular the line between the 2 nodes
							 if((y2-y1)!=0 && (x2-x1)!=0) { // if the line between 2 nodes is not completely vertical or horizontal
							 m = ((float)(y2-y1))/((float)(x2-x1));
							 m = -1/m;
							 float c = pointPer[1] - m*pointPer[0];
							 p1[0] = pointPer[0]-5;
							 p1[1]= m*(pointPer[0]-5) +c;
							 
							 p2[0] = pointPer[0]+5;
							 p2[1]= m*(pointPer[0]+5) + c;
							 
							 }
							 else if(x2-x1==0) {
								 p1[0] = pointPer[0]-5;
								 p1[1]= pointPer[1];
								 p2[0] = pointPer[0]+5;
								 p2[1]= pointPer[1];
							 }
							 else if(y2-y1==0) {
								 p1[0] = pointPer[0];
								 p1[1]= pointPer[1]-5;
								 p2[0] = pointPer[0];
								 p2[1]= pointPer[1]+5;
							 }
							 
		
							 
							 g.drawLine((int)p1[0] + 10,(int) p1[1]+18, x2+10, y2+18);
							 g.drawLine((int)p2[0] + 10,(int) p2[1]+18, x2+10, y2+18);
						 }
						 
						 
						 else { // if the line is vertical or horizontal
							 
							 
							 int x1 =nodeLocations[i][0];
							 int y1 =nodeLocations[i][1];
							 int x2 =nodeLocations[j][0];
							 int y2 =nodeLocations[j][1];
							 
							 float[] mid = new float[2];
							 mid[0] = ((float)x1 + (float)x2)/2;
							 mid[1] = ((float)y1 + (float)y2)/2;
							 x1 = (int) ((mid[0] + x1)/2);
							 y1 = (int) ((mid[1] + y1)/2);
							 
							 
							 x2 = (int) ((mid[0] + x2)/2);
							 y2 = (int) ((mid[1] + y2)/2);
							 
							 
							 g.drawLine(x1 +10, y1+18, x2+10, y2+18);
							 
							 
							 
							 float[] pointPer = new float[2];
							 pointPer[0] =  (((x1+x2)/2) + x2)/2;
							 pointPer[1] =  (((y1+y2)/2) + y2)/2;
							 float m;
							 float[] p1 = new float[2];
							 float[] p2 = new float[2];					 
							 if((y2-y1)!=0 && (x2-x1)!=0) {
							 m = ((float)(y2-y1))/((float)(x2-x1));
		
							 m = -1/m;
							 
							 float c = pointPer[1] - m*pointPer[0];
							 p1[0] = pointPer[0]-5;
							 p1[1]= m*(pointPer[0]-5) +c;
							 
							 p2[0] = pointPer[0]+5;
							 p2[1]= m*(pointPer[0]+5) + c;
							 
							 }
							 else if(x2-x1==0) {
								 p1[0] = pointPer[0]-5;
								 p1[1]= pointPer[1];
								 p2[0] = pointPer[0]+5;
								 p2[1]= pointPer[1];
							 }
							 else if(y2-y1==0) {
								 p1[0] = pointPer[0];
								 p1[1]= pointPer[1]-5;
								 p2[0] = pointPer[0];
								 p2[1]= pointPer[1]+5;
							 }
							 
							 g.drawLine((int)p1[0] + 10,(int) p1[1]+18, x2+10, y2+18);
							 g.drawLine((int)p2[0] + 10,(int) p2[1]+18, x2+10, y2+18);
							 
								 x1 =nodeLocations[j][0];
								y1 =nodeLocations[j][1];
							   x2 =nodeLocations[i][0];
								 y2 =nodeLocations[i][1];
								 
								 mid = new float[2];
								 mid[0] = ((float)x1 + (float)x2)/2;
								 mid[1] = ((float)y1 + (float)y2)/2;
								 x1 = (int) ((mid[0] + x1)/2);
								 y1 = (int) ((mid[1] + y1)/2);
								 
								 
								 x2 = (int) ((mid[0] + x2)/2);
								 y2 = (int) ((mid[1] + y2)/2);
								 
								 
								 pointPer[0] =  (((x1+x2)/2) + x2)/2;
								 pointPer[1] =  (((y1+y2)/2) + y2)/2;
		
								 
								 
								 if((y2-y1)!=0 && (x2-x1)!=0) {
								 m = ((float)(y2-y1))/((float)(x2-x1));
							
								 m = -1/m;
								 float c = pointPer[1] - m*pointPer[0];
								 p1[0] = pointPer[0]-5;
								 p1[1]= m*(pointPer[0]-5) +c;
								 
								 p2[0] = pointPer[0]+5;
								 p2[1]= m*(pointPer[0]+5) + c;
								 
								 }
								 else if(x2-x1==0) {
									 p1[0] = pointPer[0]-5;
									 p1[1]= pointPer[1];
									 p2[0] = pointPer[0]+5;
									 p2[1]= pointPer[1];
								 }
								 else if(y2-y1==0) {
									 p1[0] = pointPer[0];
									 p1[1]= pointPer[1]-5;
									 p2[0] = pointPer[0];
									 p2[1]= pointPer[1]+5;
								 }
								 
								 
		
								 
								 g.drawLine((int)p1[0] + 10,(int) p1[1]+18, x2+10, y2+18);
								 g.drawLine((int)p2[0] + 10,(int) p2[1]+18, x2+10, y2+18);
							 
						 }
						 
						 
						 
					 }
				 }
			 }
			 
		
			
			return bufferedImage;
		 }
		
			
			public static void InitGraph() 
			{ 
		
				
				adjList = new ArrayList[5]; //list initialised with 5 vertices
				for (int i=0;i<5;++i){
					adjList[i] = new ArrayList<>();
				}
				for (int i=0;i<5;++i){
					for (int j=0;j<5;++j){
						if(adjMatrix[i][j]==1){
							adjList[i].add(j);
						}
					}
				}
		
		
			} 
		
		
			public static void generatePaths(int src, int dest) // generates paths from src to dest
			{ 
				ArrayList<Integer> startPathList = new ArrayList<>(); 
				boolean[] visitedVerticesList = new boolean[5];
				startPathList.add(src); 
				recursePaths(src, dest, visitedVerticesList, startPathList); 
			} 
		
		
			static void recursePaths(Integer src, Integer dst, boolean[] visitedVerticesList, List<Integer> currentPath) 
			{ 
		
				if (src==dst) {
					
					if(currentPath.size()-1==requiredPath && flushResponseFlag==0){
						flushResponseFlag=1;// flagging so that we can send the server that path has been found
					}
			
					return; 
				} 
		
				
				visitedVerticesList[src] = true; // flagging the current node
		
				
				for (Integer i : adjList[src]) { 
					if (!visitedVerticesList[i]) { 
						currentPath.add(i); // visiting the node and add it to the current path
						recursePaths(i, dst, visitedVerticesList, currentPath); 
						currentPath.remove(i); // remove current node as we have back tracked
					} 
				} 
		
				
				visitedVerticesList[src] = false; // Visited the current node
			} 
		
			
		
		
		
		
		}