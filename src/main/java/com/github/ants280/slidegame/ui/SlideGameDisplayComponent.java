package com.github.ants280.slidegame.ui;

import com.github.ants280.slidegame.logic.Grid;
import com.github.ants280.slidegame.logic.Tile;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JComponent;

public class SlideGameDisplayComponent extends JComponent
{
	private static final long serialVersionUID = 1L;
	private final transient Grid grid;
	private static final RenderingHints ANTIALIAS_ON_RENDERING_HINT
			= new RenderingHints(
					RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
	private Font tileFont;
	private int xOffset;
	private int yOffset;
	private int minDimension;

	public SlideGameDisplayComponent(Grid grid)
	{
		super();

		this.grid = grid;

		this.init();
	}

	private void init()
	{
		this.tileFont = new Font("times", Font.PLAIN, 12);
		this.xOffset = 0;
		this.yOffset = 0;
		this.minDimension = -1;
		this.addComponentListener(
				new SlideGameComponentListener(
						componentEvent -> this.componentResized()));
	}

	@Override
	public void paintComponent(Graphics g)
	{
		((Graphics2D) g).setRenderingHints(ANTIALIAS_ON_RENDERING_HINT);
		g.setFont(tileFont);

		int gridLength = grid.getLength();
		double cellSize = minDimension / (gridLength + 0d);
		double tileSize = round(cellSize * 0.90d);
		double spacerSize = cellSize - tileSize;
		double halfSpacerSize = spacerSize / 2d;

		this.paintGrid(
				gridLength,
				g,
				minDimension,
				cellSize,
				spacerSize,
				halfSpacerSize);
		this.paintTiles(
				gridLength,
				g,
				cellSize,
				tileSize,
				halfSpacerSize);
	}

	private void componentResized()
	{
		int width = this.getWidth();
		int height = this.getHeight();
		minDimension = Math.min(width, height);
		xOffset = (width - minDimension) / 2;
		yOffset = (height - minDimension) / 2;
		float fontSize = (float) (minDimension / (grid.getLength() * 3d));

		if (tileFont.getSize2D() != fontSize)
		{
			tileFont = tileFont.deriveFont(fontSize);
			this.repaint();
		}
	}

	private void paintGrid(
			int gridLength,
			Graphics g,
			double minDimension,
			double cellSize,
			double spacerSize,
			double halfSpacerSize)
	{
		g.setColor(SlideGameColors.SPACER_COLOR);
		for (int c = 0; c <= gridLength; c++)
		{
			int x = round(xOffset + (c * cellSize) - halfSpacerSize);
			int y = round(yOffset);
			int verticalLineWidth = round(spacerSize);
			int height = round(minDimension);
			g.fillRect(
					x, y,
					verticalLineWidth, height);
		}
		for (int r = 0; r <= gridLength; r++)
		{
			int x = round(xOffset);
			int y = round(yOffset + (r * cellSize) - halfSpacerSize);
			int width = round(minDimension);
			int horizontalLineHeight = round(spacerSize);
			g.fillRect(
					x, y,
					width, horizontalLineHeight);
		}
	}

	private void paintTiles(
			int gridLength,
			Graphics g,
			double cellSize,
			double tileSize,
			double halfSpacerSize)
	{
		for (int c = 0; c < gridLength; c++)
		{
			for (int r = 0; r < gridLength; r++)
			{
				this.paintTile(
						g,
						c,
						r,
						cellSize,
						tileSize,
						halfSpacerSize);
			}
		}
	}

	private void paintTile(
			Graphics g,
			int c,
			int r,
			double cellSize,
			double tileSize,
			double halfSpacerSize)
	{
		Tile tile = grid.getTile(c, r);

		this.paintTileBackground(
				tile == null
						? SlideGameColors.EMPTY_TILE_COLOR
						: SlideGameColors.getColor(tile),
				g,
				c,
				r,
				cellSize,
				tileSize,
				halfSpacerSize);

		if (tile != null)
		{
			this.paintTileText(
					tile.getDisplayValue(),
					g,
					c,
					r,
					cellSize);
		}
	}

	private void paintTileBackground(
			Color tileColor,
			Graphics g,
			int c,
			int r,
			double cellSize,
			double tileSize,
			double halfSpacerSize)
	{
		g.setColor(tileColor);
		int x = round(xOffset + (c * cellSize) + halfSpacerSize);
		int y = round(yOffset + (r * cellSize) + halfSpacerSize);
		int width = round(tileSize);
		int height = round(tileSize);
		g.fillRect(x, y, width, height);
	}

	private void paintTileText(
			String tileText,
			Graphics g,
			int c,
			int r,
			double cellSize)
	{
		g.setColor(SlideGameColors.TILE_TEXT_COLOR);
		double fontHeight = tileFont.getSize2D() * 0.75d;
		FontMetrics fontMetrics = g.getFontMetrics();
		int textWidth = fontMetrics.stringWidth(tileText);
		int x = round(xOffset + ((c + 0.5d) * cellSize - textWidth / 2d));
		int y = round(yOffset + ((r + 0.5d) * cellSize + fontHeight / 2d));
		g.drawString(tileText, x, y);
	}

	private static int round(double value)
	{
		return Math.toIntExact(Math.round(value));
	}
}
