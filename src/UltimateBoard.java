import java.awt.Point;
import java.util.ArrayList;

public class UltimateBoard extends RegBoard {

	private QuantumGraph graph;
	
	private RegBoard[][] boards;
	
	public UltimateBoard(int dim)
	{
		super(dim);
		graph = new QuantumGraph(dim*dim);
		boards = new RegBoard[dim][dim];
	}
	
//	public ArrayList<Point> makeMove(Point one, Point two, int player)
//	{
//		graph.addEdge(one, two, player);
//		ArrayList<Point> cycle = graph.getCycle();
//		return cycle;
//	}
//	
	
	
}
