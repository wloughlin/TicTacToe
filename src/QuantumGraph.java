import java.awt.Point;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class QuantumGraph {

	private adjList[] matrix;
	private int dim;
	
	public QuantumGraph(int d)
	{
		dim = d;
		matrix = new adjList[d*d*d*d];
		for(int i = 0; i < d*d*d*d; i++)
		{
			matrix[i] = new adjList();
		}
	}
	
	public boolean addEdge(OwnedEdge e)
	{
		if(matrix[e.getFrom().x*dim*dim+e.getFrom().y].list.contains(e))
		{
			return false;
		}
		matrix[e.getFrom().x*dim*dim+e.getFrom().y].list.add(e); 
		matrix[e.getTo().x*dim*dim+e.getTo().y].list.add(e.inverse());
		return true;
	}
	
	public void removeEdge(OwnedEdge e)
	{
		Boolean first = matrix[e.getFrom().x*dim*dim+e.getFrom().y].list.remove(e); 
		Boolean second = matrix[e.getTo().x*dim*dim+e.getTo().y].list.remove(e.inverse());
	}
	
	public ArrayList<OwnedEdge> checkCycle(OwnedEdge e)
	{
		ArrayDeque<ArrayDeque<OwnedEdge>> queue = new ArrayDeque<ArrayDeque<OwnedEdge>>();
		ArrayDeque<OwnedEdge> path;
		for(OwnedEdge v : matrix[e.getTo().x*dim*dim+e.getTo().y].list)
		{
			if(!v.equals(e))
			{
				path = new ArrayDeque<OwnedEdge>();
				path.addFirst(e);
				path.addFirst(v);
				queue.addFirst(path);
			}
		}
		while(true)
		{
			if(queue.isEmpty())
			{
				return null;
			}
			ArrayDeque<OwnedEdge> nextpath = queue.pollLast();
			OwnedEdge node = nextpath.peekFirst();
			if(node.equals(e))
			{
				//nextpath.pollFirst();
				return new ArrayList<OwnedEdge>(nextpath);
			}
			for(OwnedEdge v : matrix[node.getTo().x*dim*dim+node.getTo().y].list)
			{
				if(!node.equals(v))
				{
					ArrayDeque<OwnedEdge> append = new ArrayDeque<OwnedEdge>(nextpath);
					
					append.addFirst(v);
					queue.addFirst(append);
					
				}
			}
		}
	}
	
	public HashMap<Point, Integer> collapse(Edge e)
	{
		HashMap<Point, Integer> data = new HashMap<Point, Integer>();
		HashSet<OwnedEdge> ownedEdges = matrix[e.getFrom().x*dim*dim+e.getFrom().y].list;
		OwnedEdge selected = null;
		for(OwnedEdge oe : ownedEdges)
		{
			if(oe.equalsEdge(e))
			{
				selected = oe;
			}
		}
		
		removeEdge(selected);
		
		setPoint(selected.getTo(), selected.getOwner(), data);
		return data;
	}
	
	private void setPoint(Point p, int player, HashMap<Point, Integer> data)
	{
		data.put(p, player);
		HashSet<OwnedEdge> ownedEdges = matrix[p.x*dim*dim+p.y].list;
		for(Iterator<OwnedEdge> it = ownedEdges.iterator(); it.hasNext();)
		{
			OwnedEdge e = it.next();
			it.remove();
			removeEdge(e);
			
			if(e.getFrom().equals(p))
			{
				setPoint(e.getTo(), e.getOwner(), data);
			}
			
			else
			{
				setPoint(e.getFrom(), e.getOwner(), data);
			}
			
		}	
		
	}
	
	public boolean nodeHasPlayer(Point p, int player)
	{
		for(OwnedEdge e : matrix[p.x*dim*dim+p.y].list)
		{
			if(e.getOwner() == player)
			{
				return true;
			}
		}
		return false;
	}
	
	public void printEdges()
	{
		for(int i = 0; i < dim*dim*dim*dim; i++)
		{
			System.out.println("Node: x = "+ i/(dim*dim) + ", y = "+ i%(dim*dim));
			for(OwnedEdge e : matrix[i].list)
			{
				System.out.println(e.toString());
			}
			System.out.println("");
		}
	}
	
	
	
	
	private class adjList
	{
		private HashSet<OwnedEdge> list;
		
		public adjList()
		{
			list = new HashSet<OwnedEdge>();
		}
	}
	 
	
}
