import java.awt.BasicStroke;
import java.awt.Color;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class CellPanel extends JPanel{

	private int owner;
	
	public CellPanel(int o)
	{
		owner = o;
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setPreferredSize(new Dimension(100, 100));
	}
	
	public void setOwner(int o)
	{
		owner = o;
	}
	
	public void paintComponent(Graphics g)
	{
		
		Graphics2D g2 = (Graphics2D)g;
		g2.setStroke(new BasicStroke(2));
		int height = getHeight();
		int width = getWidth();
		if(owner == 1)
		{
			g2.setColor(Color.RED);
			g2.drawLine(0, 0, width, height);
			g2.drawLine(width, 0, 0, height);
		}
		if(owner == 2)
		{
			g2.setColor(Color.BLUE);
			g2.drawOval(1, 1, width-3, height-3);
		}
		
	}
	
	
}
