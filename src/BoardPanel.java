import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class BoardPanel extends JPanel {

	private RegBoard board;
	
	private CellPanel[][] panels;
	
	private int dim;
	
	private int owner;
	private int row;
	private int col;
	
	public BoardPanel(int d, int r, int c)
	{
		super(new GridLayout(d, d));
		row = r;
		col = c;
		panels = new CellPanel[d][d];
		owner = 0;
		dim = d;
		setBorder(BorderFactory.createLineBorder(Color.MAGENTA, 2));
		board = new RegBoard(d);
		for(int i = 0; i < d; i++)
		{
			for(int j = 0; j < d; j++)
			{
				CellPanel cp = new CellPanel(0);
				add(cp);
				panels[i][j] = cp;
			}
		}
	}
	
	public boolean isFull()
	{
		return board.isFull();
	}
	
	public Point getPoint(Point point)
	{
		for(int i = 0; i < dim; i++)
		{
			for(int j = 0; j < dim; j++)
			{
				CellPanel cp = panels[i][j];
				Point loc = cp.getLocation();
				Rectangle rect = new Rectangle(loc.x+col*getWidth(), loc.y+row*getHeight(), 
						cp.getWidth(), cp.getHeight());
				if(rect.contains(point))
				{
					return new Point(i, j);
				}
			}
		}
		return null;
	}
	
	public CellPanel getCell(int i, int j)
	{
		return panels[i][j];
	}
	
	public int checkWinner()
	{
		owner = board.checkWin();
		return owner;
	}
	
	
	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;
		g2.setStroke(new BasicStroke(5));
		int height = getHeight();
		int width = getWidth();
		if(owner == 1)
		{
			//g2.setColor(Color.RED);
			g2.drawLine(0, 0, width, height);
			g2.drawLine(width, 0, 0, height);
		}
		if(owner == 2)
		{
			//g2.setColor(Color.BLUE);
			g2.drawOval(5, 5, width-5, height-5);
		}
		
		if(owner == 3)
		{
			Ellipse2D dot = new Ellipse2D.Double(1, 1, width-1, height-1);
			g2.fill(dot);
			g2.draw(dot);
		}
		
		
		//g.setColor(Color.BLACK);
		
	}
	
	public int getOwner()
	{
		return owner;
	}
	
	public boolean isTaken(Point p)
	{
		return board.getCell(p.x%dim, p.y%dim) != 0;
	}

	public void setCell(int i, int j, int owner)
	{
		panels[i][j].setOwner(owner);
		panels[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
		board.setCell(owner, i, j);
	}
	
	
	
}
