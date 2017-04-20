import java.util.Arrays;


import javafx.scene.Node;

public class RegBoard {

	private int[][] board;
	private int dim;
	private int size;
	
	public RegBoard(int d)
	{
		dim = d;
		board = new int[d][d];
		size = 0;
	}
	
	public int getCell(int r, int c)
	{
		return board[r][c];
	}
	
	public void setCell(int value, int r, int c)
	{
		board[r][c] = value;
		size++;
	}
	
	public int checkWin()
	{
		boolean winone = false;
		boolean wintwo = false;
		boolean maind = true;
		boolean offd = true;
		for(int i = 0; i < dim; i++)
		{
			boolean horz = true;
			boolean vert = true;
			for(int j = 0; j < dim; j++)
			{
				if(board[j][i] != board[0][i])
				{
					horz = false;
				}
				if(board[i][j] != board[i][0])
				{
					vert = false;
				}
			}
			if(board[i][i] != board[0][0])
			{
				maind = false;
			}
			if(board[dim-1-i][i] != board[dim-1][0])
			{
				offd = false;
			}
			if(horz)
			{
				if(board[0][i] == 1)
				{
					winone = true;
				}
				else if(board[0][i] == 2)
				{
					wintwo = true;
				}
			}
			if(vert)
			{
				if(board[i][0] == 1)
				{
					winone = true;
				}
				else if(board[i][0] == 2)
				{
					wintwo = true;
				}
			}
		}
		if(maind)
		{
			if(board[0][0] == 1)
			{
				winone = true;
			}
			else if(board[0][0] == 2)
			{
				wintwo = true;
			}
		}
		if(offd)
		{
			if(board[dim-1][0] == 1)
			{
				winone = true;
			}
			else if(board[dim-1][0] == 2)
			{
				wintwo = true;
			}
		}
		if(winone && wintwo)
		{
			return 3;
		}
		else if(winone)
		{
			return 1;
		}
		else if(wintwo)
		{
			return 2;
		}
		return 0;
		
	}
	
	public boolean isFull()
	{
		return size == dim*dim;
	}
	
	
}
