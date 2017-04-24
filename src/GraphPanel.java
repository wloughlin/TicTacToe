import java.awt.Color;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GraphPanel extends JPanel {

	private QuantumGraph graph;
	private BoardPanel[][] boards;
	private RegBoard board;
	private int dim;
	
	private HashSet<Line> lines;
	
	public GraphPanel(int d)
	{
		super(new GridLayout(d, d, 2, 2));
		dim = d;
		graph = new QuantumGraph(d);
		boards = new BoardPanel[d][d];
		board = new RegBoard(d);
		lines = new HashSet<Line>();
		for(int i = 0; i < d; i++)
		{
			for(int j = 0; j < d; j++)
			{
				BoardPanel bp = new BoardPanel(d, i, j);
				{
					add(bp);
					boards[i][j] = bp;
				}
			}
		}
	}
	
	public Point getCell(Point point)
	{
		for(int i = 0; i < dim; i++)
		{
			for(int j = 0; j < dim; j++)
			{
				BoardPanel bp = boards[i][j];
				if(bp.getBounds().contains(point))
				{
					Point p = bp.getPoint(point);
					return new Point(dim*i+p.x, dim*j+p.y);
					
				}
			}
		}
		return null;
	}
	
	public BoardPanel getBoard(Point point)
	{
		for(int i = 0; i < dim; i++)
		{
			for(int j = 0; j < dim; j++)
			{
				BoardPanel bp = boards[i][j];
				if(bp.getBounds().contains(point))
				{
					return bp;
				}
			}
		}
		return null;
	}
	
	
	public ArrayList<BoardPanel> makeMove(OwnedEdge e)
	{
		boolean troll = !graph.addEdge(e);

		ArrayList<BoardPanel> r = new ArrayList<BoardPanel>();
		BoardPanel fullCheck = getBoardfromCell(e.getFrom());
		r.add(fullCheck);
		fullCheck = getBoardfromCell(e.getTo());
		r.add(getBoardfromCell(e.getTo()));
		lines.add(new Line(e));
		if(troll)
		{
			collapse(e);
			graph.addEdge(e.inverse());
			collapse(e.inverse());
		}
		return r;
	}
	
	
	
	private BoardPanel getBoardfromCell(Point p)
	{
		return boards[p.x%dim][p.y%dim];
	}
	
	public CellPanel getCellPanel(Point p)
	{
		return boards[p.x/dim][p.y/dim].getCell(p.x%dim, p.y%dim);
	}
	
	public ArrayList<OwnedEdge> checkCycle(OwnedEdge e)
	{
		ArrayList<OwnedEdge> cycle = graph.checkCycle(e);
		if(cycle == null)
		{
			return null;
		}
		for(Edge n : cycle)
		{
			Point node = n.getFrom();
			Point node2 = n.getTo();
			CellPanel cell = (boards[node.x/dim][node.y/dim].getCell(node.x%dim, node.y%dim));
			cell.setBorder(BorderFactory.createLineBorder(Color.CYAN));
			cell = (boards[node2.x/dim][node2.y/dim].getCell(node2.x%dim, node2.y%dim));
			cell.setBorder(BorderFactory.createLineBorder(Color.CYAN));
		}
		return cycle;
	}
	
	public void collapse(Edge e)
	{
		HashMap<Point, Integer> data = graph.collapse(e);
		for(Point p : data.keySet())
		{
		
			boards[p.x/dim][p.y/dim].setCell(p.x%dim, p.y%dim, data.get(p));
			for(Iterator<Line> it = lines.iterator(); it.hasNext();)
			{
				Line l = it.next();
				if(l.hasPoint(p))
				{
					it.remove();
				}
			}
		}
		for(int i = 0; i < dim; i++)
		{
			for(int j = 0; j < dim; j++)
			{
				if(boards[i][j].getOwner() == 0)
				{
					board.setCell(boards[i][j].checkWinner(), i, j);
				}
			}
		}
		int winner = board.checkWin();
		if(winner > 0)
		{
			JOptionPane.showMessageDialog(null, "Player " + winner + " wins");
		}
		repaint();
	}
	
	
	
	
	public void paintComponent(Graphics g)
	{
		
		BoardPanel b = boards[0][0];
		int w = b.getWidth();
		int h = b.getHeight();
		for(Line l : lines)
		{
			l.draw(g, w, h);
		}
		
	}
	
	
	
	private class Line {
		
		private CellPanel from;
		private CellPanel to;
		private Color color;
		private Edge ownedEdge;
		private int i;
		private int j;
		private int m;
		private int n;
		private int owner;
		
		public Line(OwnedEdge e)
		{
			ownedEdge = e;
			Point f = e.getFrom();
			Point t = e.getTo();
			i = f.x/dim;
			j = f.y/dim;
			m = t.x/dim;
			n = t.y/dim;
			from = boards[i][j].getCell(f.x%dim, f.y%dim);
			to = boards[m][n].getCell(t.x%dim, t.y%dim);
			owner = e.getOwner();
			switch(owner)
			{
				case 1: color = Color.RED;
				break;
				case 2: color = Color.BLUE;
				break;
				default: color = Color.BLACK;
			}
		}
		
		
		
		public void draw(Graphics g, int bwidth, int bheight)
		{
			Graphics2D g2 = (Graphics2D)g;
			g2.setColor(color);
			int inc = 0; //12*owner;
			if(owner == 1)
			{
				inc = inc*-2;
			}
			int x1 = j*bwidth+from.getX()+from.getWidth()/2-inc;
			int y1 = i*bheight+from.getY()+from.getHeight()/2-inc;
			int x2 = n*bwidth+to.getX()+to.getWidth()/2-inc;
			int y2 = m*bheight+to.getY()+to.getHeight()/2-inc;
			g2.drawLine(x1, y1, x2, y2);
			Ellipse2D p1 = new Ellipse2D.Double(x1-3, y1-3, 6, 6);
			Ellipse2D p2 = new Ellipse2D.Double(x2-3, y2-3, 6, 6);
			g2.fill(p1);
			g2.fill(p2);
			g2.draw(p1);
			g2.draw(p2);
			g.setColor(Color.BLACK);
		}
		
		public boolean equals(Object o)
		{
			if(this == o)
			{
				return true;
			}
			if(o == null)
			{
				return false;
			}
			if(getClass() != o.getClass())
			{
				return false;
			}
			return ownedEdge.equals(((Line)o).ownedEdge);
		}
		
		public boolean hasPoint(Point p)
		{
			return ownedEdge.contains(p);
		}
	}
		
}
