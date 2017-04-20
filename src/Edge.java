import java.awt.Point;

public class Edge {

	private Point start;
	private Point end;
	
	public Edge(Point s, Point e)
	{
		start = s;
		end = e;
	}

	public Point getTo() {
		return end;
	}

	public Point getFrom() {
		return start;
	}
	
	public Edge inverse()
	{
		return new Edge(end, start);
	}
	
	public boolean contains(Point p)
	{
		return p.equals(start) || p.equals(end);
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
		return (start.equals(((Edge)o).start) && end.equals(((Edge)o).end))
				|| (end.equals(((Edge)o).start) && start.equals(((Edge)o).end));
	}
	
	public int hashCode()
	{
		return start.hashCode() + end.hashCode();
	}

}
