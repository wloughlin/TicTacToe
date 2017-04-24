import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Game extends JFrame {

		public static final int DIM = 3;
		private UltimateBoard board;
		private int turn;
		private Container cont;
		private GraphPanel panel;
		private Point move1;
		private Point move2;
		private ArrayList<OwnedEdge> cycle;
		private ArrayList<BoardPanel> currentBoards; // Can move on
		
		public Game()
		{
			turn = 0;
			board = new UltimateBoard(DIM);
			cont = getContentPane();
			panel = new GraphPanel(DIM);
			
			move1 = null;
			move2 = null;
			cycle = null;
			currentBoards = new ArrayList<BoardPanel>();
			setup();
			
			pack();
			setVisible(true);
		}
		
		private void setup()
		{
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			cont.setLayout(new BorderLayout());
			cont.add(panel);
			
			addMouseListener(new MouseAdapter()
					{
						public void mouseClicked(MouseEvent e)
						{
							BoardPanel bp = panel.getBoard(e.getPoint());
							Point index = panel.getCell(e.getPoint());
							if(move1 == null && cycle == null && !bp.isTaken(index))
							{
								if(currentBoards.size() == 0 || currentBoards.contains(bp))
								{
									move1 = index;
									currentBoards.remove(bp);
									if(!currentBoards.contains(bp))
									{
										bp.setBorder(BorderFactory.createLineBorder(Color.MAGENTA, 2));
									}
									
								}
							}
							else if (cycle == null && !bp.isTaken(index))
							{
								move2 = index;
								if(move2.equals(move1))
								{
									move1 = null;
									currentBoards.add(bp);
									bp.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
								}
								else if(currentBoards.size() == 0 || currentBoards.contains(bp))
								{
									for(BoardPanel b : currentBoards)
									{
										b.setBorder(BorderFactory.createLineBorder(Color.MAGENTA, 2));
									}
									OwnedEdge ownedEdge = new OwnedEdge(move1, move2, turn);
									currentBoards = panel.makeMove(ownedEdge);
									cycle = panel.checkCycle(ownedEdge);
									if(cycle != null)
									{
										JOptionPane.showMessageDialog(null, "Cycle Formed");
										int owner = cycle.get(1).getOwner();
										boolean choice = false;
										for(OwnedEdge cycleEdge : cycle)
										{
											if(cycleEdge.getOwner() != owner)
											{
												choice = true;
												break;
											}
										}
										if(choice == false)
										{
											panel.collapse(cycle.get(1));
											cycle = null;
										}
									}
									move1 = null;
									move2 = null;
									turn++;
									for(BoardPanel b : currentBoards)
									{
										b.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
									}
								}
							}
							repaint();
						}
						
						public void mousePressed(MouseEvent e)
						{
							if(cycle != null)
							{
								move1 = panel.getCell(e.getPoint());
							}
						}
						
						public void mouseReleased(MouseEvent e)
						{
							if(cycle != null)
							{
								move2 = panel.getCell(e.getPoint());
								OwnedEdge selected = new OwnedEdge(move1, move2, turn);
								for(OwnedEdge edge : cycle)
								{
									if(edge.equals(selected))
									{
										panel.collapse(selected);
										cycle = null;
										move1 = null;
										move2 = null;
										for(Iterator<BoardPanel> it = currentBoards.iterator(); it.hasNext();)
										{
											BoardPanel b = it.next();
											if(b.isFull())
											{
												b.setBorder(BorderFactory.createLineBorder(Color.MAGENTA, 2));
												it.remove();
											}
										}
										break;
									}
								}
							}
							repaint();
						}
					});
				
		}
		
		
		public static void main(String[] args)
		{
			Game g = new Game();
		}
		
		

		
}
