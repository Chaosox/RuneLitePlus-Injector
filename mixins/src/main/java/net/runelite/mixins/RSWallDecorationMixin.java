package net.runelite.mixins;

import api.Model;
import api.Perspective;
import api.coords.LocalPoint;
import java.awt.Polygon;
import java.awt.geom.Area;
import net.runelite.api.mixins.Inject;
import net.runelite.api.mixins.Mixin;
import net.runelite.api.mixins.Shadow;
import rs.api.RSClient;
import rs.api.RSEntity;
import rs.api.RSModel;
import rs.api.RSWallDecoration;

@Mixin(RSWallDecoration.class)
public abstract class RSWallDecorationMixin implements RSWallDecoration
{
	@Shadow("client")
	private static RSClient client;

	@Inject
	private int decorativeObjectPlane;

	@Inject
	@Override
	public int getPlane()
	{
		return decorativeObjectPlane;
	}

	@Inject
	@Override
	public void setPlane(int plane)
	{
		this.decorativeObjectPlane = plane;
	}

	@Inject
	private RSModel getModel()
	{
		RSEntity renderable = getRenderable();
		if (renderable == null)
		{
			return null;
		}

		RSModel model;

		if (renderable instanceof Model)
		{
			model = (RSModel) renderable;
		}
		else
		{
			model = renderable.getModel();
		}

		return model;
	}

	@Inject
	private RSModel getModel2()
	{
		RSEntity renderable = getRenderable2();
		if (renderable == null)
		{
			return null;
		}

		RSModel model;

		if (renderable instanceof Model)
		{
			model = (RSModel) renderable;
		}
		else
		{
			model = renderable.getModel();
		}

		return model;
	}

	@Inject
	@Override
	public Area getClickbox()
	{
		Area clickbox = new Area();

		LocalPoint lp = getLocalLocation();
		Area clickboxA = Perspective.getClickbox(client, getModel(), 0,
			new LocalPoint(lp.getX() + getXOffset(), lp.getY() + getYOffset()));
		Area clickboxB = Perspective.getClickbox(client, getModel2(), 0, lp);

		if (clickboxA == null && clickboxB == null)
		{
			return null;
		}

		if (clickboxA != null)
		{
			clickbox.add(clickboxA);
		}

		if (clickboxB != null)
		{
			clickbox.add(clickboxB);
		}

		return clickbox;
	}

	@Inject
	@Override
	public Polygon getConvexHull()
	{
		RSModel model = getModel();

		if (model == null)
		{
			return null;
		}

		int tileHeight = Perspective.getTileHeight(client, new LocalPoint(getX(), getY()), client.getPlane());
		return model.getConvexHull(getX() + getXOffset(), getY() + getYOffset(), 0, tileHeight);
	}

	@Inject
	@Override
	public Polygon getConvexHull2()
	{
		RSModel model = getModel2();

		if (model == null)
		{
			return null;
		}

		int tileHeight = Perspective.getTileHeight(client, new LocalPoint(getX(), getY()), client.getPlane());
		return model.getConvexHull(getX(), getY(), 0, tileHeight);
	}
}
