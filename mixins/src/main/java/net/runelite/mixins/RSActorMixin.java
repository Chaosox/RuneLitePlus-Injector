package net.runelite.mixins;

import api.Actor;
import api.NPC;
import api.NPCDefinition;
import api.Perspective;
import api.Player;
import api.Point;
import api.Sprite;
import api.coords.LocalPoint;
import api.coords.WorldArea;
import api.coords.WorldPoint;
import api.events.AnimationChanged;
import api.events.SpotAnimationChanged;
import api.events.InteractingChanged;
import api.events.OverheadTextChanged;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import net.runelite.api.mixins.FieldHook;
import net.runelite.api.mixins.Inject;
import net.runelite.api.mixins.Mixin;
import net.runelite.api.mixins.Shadow;
import rs.api.RSActor;
import rs.api.RSClient;
import rs.api.RSHealthBar;
import rs.api.RSHealthBarDefinition;
import rs.api.RSHealthBarUpdate;
import rs.api.RSIterableNodeDeque;
import rs.api.RSNode;

@Mixin(RSActor.class)
public abstract class RSActorMixin implements RSActor
{
	@Shadow("client")
	private static RSClient client;

	@Inject
	@Override
	public Actor getInteracting()
	{
		int index = getRSInteracting();
		if (index == -1 || index == 65535)
		{
			return null;
		}

		if (index < 32768)
		{
			NPC[] npcs = client.getCachedNPCs();
			return npcs[index];
		}

		index -= 32768;
		Player[] players = client.getCachedPlayers();
		return players[index];
	}

	@Inject
	@Override
	public int getHealthRatio()
	{
		RSIterableNodeDeque healthBars = getHealthBars();
		if (healthBars != null)
		{
			RSNode current = healthBars.getCurrent();
			RSNode next = current.getNext();
			if (next instanceof RSHealthBar)
			{
				RSHealthBar wrapper = (RSHealthBar) next;
				RSIterableNodeDeque updates = wrapper.getUpdates();

				RSNode currentUpdate = updates.getCurrent();
				RSNode nextUpdate = currentUpdate.getNext();
				if (nextUpdate instanceof RSHealthBarUpdate)
				{
					RSHealthBarUpdate update = (RSHealthBarUpdate) nextUpdate;
					return update.getHealthRatio();
				}
			}
		}
		return -1;
	}

	@Inject
	@Override
	public int getHealth()
	{
		RSIterableNodeDeque healthBars = getHealthBars();
		if (healthBars != null)
		{
			RSNode current = healthBars.getCurrent();
			RSNode next = current.getNext();
			if (next instanceof RSHealthBar)
			{
				RSHealthBar wrapper = (RSHealthBar) next;
				RSHealthBarDefinition definition = wrapper.getDefinition();
				return definition.getHealthScale();
			}
		}
		return -1;
	}

	@Inject
	@Override
	public WorldPoint getWorldLocation()
	{
		return WorldPoint.fromLocal(client,
			this.getPathX()[0] * Perspective.LOCAL_TILE_SIZE + Perspective.LOCAL_TILE_SIZE / 2,
			this.getPathY()[0] * Perspective.LOCAL_TILE_SIZE + Perspective.LOCAL_TILE_SIZE / 2,
			client.getPlane());
	}

	@Inject
	@Override
	public LocalPoint getLocalLocation()
	{
		return new LocalPoint(getX(), getY());
	}

	@Inject
	@Override
	public Polygon getCanvasTilePoly()
	{
		return Perspective.getCanvasTilePoly(client, getLocalLocation());
	}

	@Inject
	@Override
	public Point getCanvasTextLocation(Graphics2D graphics, String text, int zOffset)
	{
		return Perspective.getCanvasTextLocation(client, graphics, getLocalLocation(), text, zOffset);
	}

	@Inject
	@Override
	public Point getCanvasImageLocation(BufferedImage image, int zOffset)
	{
		return Perspective.getCanvasImageLocation(client, getLocalLocation(), image, zOffset);
	}

	@Inject
	@Override
	public Point getCanvasSpriteLocation(Sprite sprite, int zOffset)
	{
		return Perspective.getCanvasSpriteLocation(client, getLocalLocation(), sprite, zOffset);
	}

	@Inject
	@Override
	public Point getMinimapLocation()
	{
		return Perspective.localToMinimap(client, getLocalLocation());
	}

	@FieldHook("sequence")
	@Inject
	public void animationChanged(int idx)
	{
		AnimationChanged animationChange = new AnimationChanged();
		animationChange.setActor(this);
		client.getCallbacks().post(animationChange);
	}

	@FieldHook("spotAnimation")
	@Inject
	public void spotAnimationChanged(int idx)
	{
		SpotAnimationChanged spotAnimationChanged = new SpotAnimationChanged();
		spotAnimationChanged.setActor(this);
		client.getCallbacks().post(spotAnimationChanged);
	}

	@FieldHook("targetIndex")
	@Inject
	public void interactingChanged(int idx)
	{
		InteractingChanged interactingChanged = new InteractingChanged(this, getInteracting());
		client.getCallbacks().post(interactingChanged);
	}

	@FieldHook("overheadText")
	@Inject
	public void overheadTextChanged(int idx)
	{
		String overheadText = getOverheadText();
		if (overheadText != null)
		{
			OverheadTextChanged overheadTextChanged = new OverheadTextChanged(this, overheadText);
			client.getCallbacks().post(overheadTextChanged);
		}
	}

	@Inject
	@Override
	public WorldArea getWorldArea()
	{
		int size = 1;
		if (this instanceof NPC)
		{
			NPCDefinition composition = ((NPC)this).getDefinition();
			if (composition != null && composition.getConfigs() != null)
			{
				composition = composition.transform();
			}
			if (composition != null)
			{
				size = composition.getSize();
			}
		}

		return new WorldArea(this.getWorldLocation(), size, size);
	}
}
