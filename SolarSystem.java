//very useful answer to arrays and null reference provided by https://answers.unity.com/questions/720055/array-has-length-but-is-null.html
import java.util.*;
import java.util.List;
import java.io.*;
import java.nio.file.*;
//import processing.core.*;
import java.awt.*;
import java.awt.Color;
import java.awt.geom.*;
import javax.swing.*;
public class SolarSystem 
{
	Body[] bodies;
	Scanner sc;
	File file;
	public SolarSystem(int n) 
	{
		bodies = new Body[n];
		file = new File("solarsystem.dat");
		try{
			sc=new Scanner(file);
		}catch(FileNotFoundException e){
			System.out.println("File not found");
		}
		for(int i=0;i<n;i++)
			bodies[i] = new Body(null,null,0,0,0,0,0,0,0,0);
	}
	private void toString(Body body)
	{
		System.out.println(String.format("%-15s %-15s %15s %15s %20s %15s %30s %30s %30s %30s","Name","Orbits","Mass (kg)","Diameter (m)","Perihelion (m)","Aphelion (m)","Orbital Period (days)","Rotational Period (hours)","Axial Tilt (degrees)","Orbital Inclincation (degrees)"));
		System.out.println(String.format("%-15s %-15s %15s %15s %20s %15s %30s %30s %30s %30s",body.name,body.orbits,body.mass,body.diam,body.peri,body.aph,body.orbp,body.rotp,body.axial,body.orbi));
	}
	
	//experimental section
	
	private void createSolarSystem(SolarSystem ss)
	{
		int numoo = ss.bodies.length; //number of objects
		Body centre = new Body(null,null,0,0,0,0,0,0,0,0);
		for(int i=0;i<numoo;i++)
		{
			if(ss.bodies[i].orbits.equals("NaN"))
			{
				centre=ss.bodies[i];
				break;
			}
		}
		ArrayList<Integer> mainbody = new ArrayList<Integer>();
		ArrayList<ArrayList<Integer>> satellites= new ArrayList<>();
		for(int i=0;i<numoo;i++)
		{
			if(ss.bodies[i].orbits.equals(centre.name))
			{
				mainbody.add(i);
				//satellites.add(new ArrayList<Integer>());
			}
		}
		for(int i=0;i<numoo;i++)
		{
			if((!(ss.bodies[i].orbits.equals(centre.name)))&&(!(ss.bodies[i].orbits.equals("NaN"))))
				for(int j=0;j<mainbody.size();j++)
				{
					//System.out.println(ss.bodies[i].name);
					if(ss.bodies[i].orbits.equals(ss.bodies[mainbody.get(j)].name))
					{
						//System.out.println(ss.bodies[i].name);
						satellites.add(new ArrayList());
						satellites.get(satellites.size()-1).add(i);	
						satellites.get(satellites.size()-1).add(mainbody.get(j));
					}				
				}
			else
				continue;
		}
		
		//test
		//ArrayList<Integer> temp;
		System.out.println("Centre of the system is: " + centre.name);
		for(int i=0;i<mainbody.size();i++)
		{
			System.out.println("Body "+ss.bodies[mainbody.get(i)].name+" orbits "+centre.name);
		}
		for(int i=0;i<satellites.size();i++)
		{
			System.out.println("Satellite "+ss.bodies[satellites.get(i).get(0)].name+" orbits "+ss.bodies[satellites.get(i).get(1)].name);
		}
		
	}
	
	//experimental section end
	
	public static void main(String[] args) 
	{
		int limit;
		Path path = Paths.get("solarsystem.dat");
		try{
			limit = (int)Files.lines(path).parallel().count();
		}catch(IOException e){
			System.err.println("Error occurred");
			limit=15;
		}
		SolarSystem solarSystem = new SolarSystem(limit-1);		
		for(int index = 0; index < limit; index++)
		{
			if(index!=0)
			{
				solarSystem.bodies[index-1].name=solarSystem.sc.next();
				solarSystem.bodies[index-1].orbits=solarSystem.sc.next();
				solarSystem.bodies[index-1].mass=solarSystem.sc.nextDouble();
				solarSystem.bodies[index-1].diam=solarSystem.sc.nextDouble();
				solarSystem.bodies[index-1].peri=solarSystem.sc.nextDouble();
				solarSystem.bodies[index-1].aph=solarSystem.sc.nextDouble();
				solarSystem.bodies[index-1].orbp=solarSystem.sc.nextDouble();
				solarSystem.bodies[index-1].rotp=solarSystem.sc.nextDouble();
				solarSystem.bodies[index-1].axial=solarSystem.sc.nextDouble();
				solarSystem.bodies[index-1].orbi=solarSystem.sc.nextDouble();
			}
			else{
				solarSystem.sc.nextLine();
			}
		}
		solarSystem.sc.close();
		for(int index = 0; index < limit-1; index++)
		{
			solarSystem.toString(solarSystem.bodies[index]);
		}
		solarSystem.createSolarSystem(solarSystem);
		JFrame mainFrame;
		//JPanel mainPanel;
		mainFrame = new JFrame();		
		//mainPanel = new JPanel();
		mainFrame.setContentPane(new drawSystem(solarSystem.bodies));
		mainFrame.getContentPane().setBackground(Color.BLACK);
		mainFrame.setSize(1280,700);
		
		mainFrame.setVisible(true);
	}
}
class Body 
{
	String name, orbits;
	double mass, diam, peri, aph, orbp, rotp, axial, orbi;
	public Body(String name, String orbits, double mass, double diam, double peri, double aph, double orbp, double rotp, double axial, double orbi)
	{
		this.name = name;//inname;
		this.orbits=orbits;
		this.mass = mass;
		this.diam = diam;
		this.peri = peri;
		this.aph = aph;
		this.orbp = orbp;
		this.rotp = rotp;
		this.axial= axial;
		this.orbi = orbi;
	}
}

//second experimental section starts


class drawSystem extends JPanel
{
	Body[] bodies;
	Body centre;
	private List<Object> shapes = new ArrayList<>();
	
	int x=0,y=350,r=10;
	double diameter=0, orbitRadius=0;
	public drawSystem(Body[] bodies)
	{
		this.bodies=bodies;	
		for(Body b : bodies)
		{
			System.out.println(b.diam);
			if(b.diam > diameter)
				diameter=b.diam;
			if((b.peri+b.aph)>orbitRadius)
				orbitRadius=(b.peri+b.aph)/2;
		}
		System.out.println(diameter);
		for(Body b : bodies)
		{
			if(b.orbits.equals("NaN"))
			{
				centre=b;
				System.out.println(b.diam/diameter);
				addCircle(x,y,(int)((b.diam/diameter)*500));
				x+=((b.diam/diameter)*500);
				break;
			}
		}	
		for(Body b : bodies)
		{			
			if(b.orbits.equals(centre.name))
			{
				x=(int)((((b.peri+b.aph)/(2*orbitRadius))*1000))+350;
				//x+=100;
				System.out.println(x);
				addCircle(x,y,(int)((b.diam/diameter)*5000));
			}			
		}
	}
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);		
		for(Object s : shapes)
		{			
			((Circle)s).draw(g);			
		}
	}
	private void addCircle(int x, int y,int r)
	{
		//int x=0,y=0;
		//int r = 100;
		shapes.add(new Circle(x,y,r));
		repaint();
	}
}
class Circle
{
	int x,y,r;
	public Circle(int x, int y, int r){
		this.x=x;
		this.y=y;
		this.r=r;
	}
	public void draw(Graphics g)
	{
		Graphics2D g2d = (Graphics2D)g;
		Ellipse2D.Double circle = new Ellipse2D.Double(x,y,r,r);
		g2d.setColor(Color.WHITE);
		g2d.fill(circle);
	}
}


//second experimental section ends
