import java.awt.Point;

public class OwnedEdge extends Edge{

	private int turn;
	
	
	public OwnedEdge(Point s, Point e, int o)
	{
		super(s, e);
		turn = o;
	}
	
	
	
	public int getOwner()
	{
		return (turn%2)+1;
	}
	
	public OwnedEdge inverse()
	{
		return new OwnedEdge(super.getTo(), super.getFrom(), turn);
	}
	
	public boolean equalsEdge(Edge o)
	{
		return (super.getFrom().equals(o.getFrom()) && super.getTo().equals(o.getTo()))
				|| (super.getTo().equals(o.getFrom()) && super.getFrom().equals(o.getTo()));
	}
	/**
	 * Weak equality
	 */
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
		return super.equals((Edge)o) && getOwner() == ((OwnedEdge)o).getOwner();
	}
	
	
	public int hashCode()
	{
		return 31*getOwner() + super.hashCode();
	}
	
	public String toString()
	{
		return "From: (" + super.getFrom().x + ", "+super.getFrom().y+")"+
				"\nTo: (" + super.getTo().x + ", "+super.getTo().y+")";
	}
	
	
	
	
}
