package net.runelite.client.plugins.microbot.questhelper.steps;

import net.runelite.client.plugins.microbot.questhelper.QuestHelperPlugin;
import net.runelite.client.plugins.microbot.questhelper.questhelpers.QuestHelper;
import net.runelite.client.plugins.microbot.questhelper.requirements.Requirement;
import net.runelite.api.Perspective;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.OverlayUtil;

import java.awt.*;

public class TileStep extends DetailedQuestStep
{
	public TileStep(QuestHelper questHelper, WorldPoint worldPoint, String text, Requirement... requirements)
	{
		super(questHelper, text, requirements);
		this.worldPoint = worldPoint;
	}

	@Override
	public void makeWorldOverlayHint(Graphics2D graphics, QuestHelperPlugin plugin)
	{
		super.makeWorldOverlayHint(graphics, plugin);

		LocalPoint lp = LocalPoint.fromWorld(client, worldPoint);
		if (lp == null)
		{
			return;
		}

		Polygon poly = Perspective.getCanvasTilePoly(client, lp);
		if (poly == null)
		{
			return;
		}

		OverlayUtil.renderPolygon(graphics, poly, questHelper.getConfig().targetOverlayColor());
	}
}
